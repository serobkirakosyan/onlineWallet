package com.aca.acaonlinewallet.service;

import com.aca.acaonlinewallet.dto.UserDto;
import com.aca.acaonlinewallet.dto.WalletDto;
import com.aca.acaonlinewallet.entity.Card;
import com.aca.acaonlinewallet.entity.User;
import com.aca.acaonlinewallet.entity.Wallet;
import com.aca.acaonlinewallet.exception.InsufficientFundsException;
import com.aca.acaonlinewallet.exception.NullDtoException;
import com.aca.acaonlinewallet.exception.NullIdException;
import com.aca.acaonlinewallet.exception.WalletNotFoundException;
import com.aca.acaonlinewallet.repository.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {
    private final Logger logger = LoggerFactory.getLogger(WalletService.class);
    private final WalletRepository walletRepository;
    private final UserService userService;
    private final CardService cardService;

    @Autowired
    public WalletService(WalletRepository walletRepository, UserService userService, CardService cardService) {
        this.walletRepository = walletRepository;
        this.userService = userService;
        this.cardService = cardService;
    }

    public WalletDto getWallet(Long id) {
        logger.info("Getting wallet by id: {}", id);
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new WalletNotFoundException("Wallet by id " + id + " is not found"));
        logger.info("Wallet found: {}", wallet);
        return WalletDto.mapEntityToDto(wallet);
    }

    @Transactional
    public WalletDto addWallet(WalletDto walletDto) {
        logger.info("Adding a new wallet: {}", walletDto);

        if (walletDto == null) {
            throw new NullDtoException("Wallet can't be null");
        }

        Wallet wallet = WalletDto.mapDtoToEntity(walletDto);

        return WalletDto.mapEntityToDto(walletRepository.save(wallet));
    }

    @Transactional
    public void deleteWallet(Long id) {
        logger.info("Deleting wallet by id: {}", id);
        boolean existsById = walletRepository.existsById(id);
        if (!existsById) {
            throw new WalletNotFoundException("Wallet by id" + id + "does not exist");
        }
        walletRepository.deleteById(id);
    }

    @Transactional
    public WalletDto updateWallet(Long id, WalletDto walletDto) {
        logger.info("Updating wallet with id: {}", id);
        if (id == null) {
            throw new NullIdException("id cannot be null");
        }

        if (walletDto == null) {
            throw new NullDtoException("walletDto cannot be null");
        }

        walletRepository.findById(id)
                .orElseThrow(() -> new WalletNotFoundException("Wallet by id " + id + " does not exist"));

        Wallet updatedWallet = WalletDto.mapDtoToEntity(walletDto);
        updatedWallet.setId(id);
        updatedWallet = walletRepository.save(updatedWallet);
        logger.info("Wallet updated with id {}: {}", id, updatedWallet);
        return WalletDto.mapEntityToDto(updatedWallet);
    }

    @Transactional
    public void moneyTransfer(Long userId, String walletNumber, Double amount) {
        logger.info("Transferring money for user with id: {} to wallet number: {} amount: {}", userId, walletNumber, amount);
        if (amount <= 0) {
            throw new InsufficientFundsException("Amount should be more than zero");

        }
        Wallet walletByNumber = walletRepository.findByNumber(walletNumber).orElseThrow(() -> new WalletNotFoundException("Wallet with number " + walletNumber + " does not exist"));
        User user = UserDto.mapDtoToEntity(userService.getUser(userId));
        transferToWallet(user.getWallet(), walletByNumber, amount);
        walletRepository.save(walletByNumber);
        walletRepository.save(user.getWallet());
        logger.info("Money transferred successfully");
    }

    public void transferToWallet(Wallet user1Wallet, Wallet user2Wallet, Double amount) {
        if (user1Wallet.getBalance() < amount) {
            throw new InsufficientFundsException("There is not enough money to transfer");
        }
        user1Wallet.setBalance(user1Wallet.getBalance() - amount);
        user2Wallet.setBalance(user2Wallet.getBalance() + amount);
    }

    @Transactional
    public void transferToDefaultCard(Long userId, Double amount) {
        logger.info("Transferring money for user with id: {} to default card amount: {}", userId, amount);
        User user = UserDto.mapDtoToEntity(userService.getUser(userId));
        if (user.getWallet().getBalance() < amount) {
            throw new InsufficientFundsException("There is not enough money in wallet");
        }
        user.getWallet().setBalance(user.getWallet().getBalance() - amount);
        for (Card card : user.getListOfCards()) {
            if (card.getIsDefault()) {
                card.setAccount(card.getAccount() + amount);
                break;
            }
        }
        userService.updateUser(userId, UserDto.mapEntityToDto(user));
        logger.info("Money transferred to default card successfully");
    }

    @Transactional
    public void transferToCard(Long userId, Long cardNumber, Double amount) {
        logger.info("Transferring money for user with id: {} to card number: {} amount: {}", userId, cardNumber, amount);
        Card card = cardService.getCardByCardNumber(cardNumber);
        User user = UserDto.mapDtoToEntity(userService.getUser(userId));
        if (user.getWallet().getBalance() < amount) {
            logger.info("Not enough balance in the wallet for money transfer");
            throw new InsufficientFundsException("There is not enough money in wallet");
        }
        user.getWallet().setBalance(user.getWallet().getBalance() - amount);
        card.setAccount(card.getAccount() + amount);
        user.getWallet().setUser(user);
        userService.updateUser(userId, UserDto.mapEntityToDto(user));
        logger.info("Money transferred to card successfully");
    }
}


