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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletService {
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
        Wallet wallet = walletRepository.findById(id).orElseThrow(() -> new WalletNotFoundException("Wallet by id " + id + " is not found"));
        return WalletDto.mapEntityToDto(wallet);
    }

    @Transactional
    public WalletDto addWallet(WalletDto walletDto) {

        if (walletDto == null) {
            throw new NullDtoException("Wallet can't be null");
        }

        Wallet wallet = WalletDto.mapDtoToEntity(walletDto);

        return WalletDto.mapEntityToDto(walletRepository.save(wallet));
    }

    @Transactional
    public void deleteWallet(Long id) {
        boolean existsById = walletRepository.existsById(id);
        if (!existsById) {
            throw new WalletNotFoundException("Wallet by id" + id + "does not exist");
        }
        walletRepository.deleteById(id);
    }

    @Transactional
    public WalletDto updateWallet(Long id, WalletDto walletDto) {
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
        return WalletDto.mapEntityToDto(updatedWallet);
    }

    @Transactional
    public void moneyTransfer(Long userId , String walletNumber, Double amount) {
        if (amount <= 0) {
            throw new InsufficientFundsException("Amount should be more than zero");

        }
        Wallet walletByNumber = walletRepository.findByNumber(walletNumber).orElseThrow(() -> new WalletNotFoundException("Wallet with number " + walletNumber + " does not exist"));
        User user = UserDto.mapDtoToEntity(userService.getUser(userId));
        transferMoneyBetweenWallets(user.getWallet(), walletByNumber, amount);
        walletRepository.save(walletByNumber);
        walletRepository.save(user.getWallet());
    }
    private void transferMoneyBetweenWallets(Wallet user1Wallet, Wallet user2Wallet, Double amount){
        if (user1Wallet.getBalance() < amount){
            throw new InsufficientFundsException("There is not enough money to transfer");
        }
        user1Wallet.setBalance(user1Wallet.getBalance() - amount);
        user2Wallet.setBalance(user2Wallet.getBalance() + amount);
    }
    @Transactional
    public void transferMoneyToDefaultCard(Long userId, Double amount){
        User user = UserDto.mapDtoToEntity(userService.getUser(userId));
        if (user.getWallet().getBalance() < amount) {
            throw new InsufficientFundsException("There is not enough money in wallet");
        }
        user.getWallet().setBalance(user.getWallet().getBalance() - amount);
        for(Card card: user.getListOfCards()){
            if(card.getIsDefault()){
                card.setAccount(card.getAccount() + amount);
                break;
            }
        }
        userService.updateUser(userId, UserDto.mapEntityToDto(user));
    }
    @Transactional
    public void transferMoneyFromWalletToCard(Long userId, Long cardNumber, Double amount){
        Card card = cardService.getCardByCardNumber(cardNumber);
        User user = UserDto.mapDtoToEntity(userService.getUser(userId)) ;
        if (user.getWallet().getBalance() < amount) {
            throw new InsufficientFundsException("There is not enough money in wallet");
        }
        user.getWallet().setBalance(user.getWallet().getBalance()-amount);
        card.setAccount(card.getAccount() + amount);
        user.getWallet().setUser(user);
        userService.updateUser(userId, UserDto.mapEntityToDto(user));
    }

}
