package com.aca.acaonlinewallet.service;

import com.aca.acaonlinewallet.dto.UserDto;
import com.aca.acaonlinewallet.dto.WalletDto;
import com.aca.acaonlinewallet.entity.Card;
import com.aca.acaonlinewallet.entity.User;
import com.aca.acaonlinewallet.entity.Wallet;
import com.aca.acaonlinewallet.repository.WalletRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private UserService userService;

    @Mock
    private CardService cardService;

    @InjectMocks
    private WalletService walletService;

    @Test
    public void testGetWallet_ValidId() {
        Long walletId = 1L;
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        wallet.setBalance(100.0);


        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        WalletDto result = walletService.getWallet(walletId);

        assertNotNull(result);
        assertEquals(walletId, result.getId());
        assertEquals(wallet.getBalance(), result.getBalance(), 0.0);
    }

    @Test
    public void testGetWallet_WalletNotFound() {
        Long walletId = 1L;

        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> walletService.getWallet(walletId));
    }

    @Test
    public void tesAddWallet() {
        WalletDto walletDto = new WalletDto();
        walletDto.setBalance(500.0);
        Wallet wallet = WalletDto.mapDtoToEntity(walletDto);
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        WalletDto result = walletService.addWallet(walletDto);
        assertNotNull(result);
        assertEquals(walletDto.getBalance(), result.getBalance(), 0.0);
    }

    @Test
    void testAddWallet_NullWalletDto() {
        assertThrows(RuntimeException.class, () -> walletService.addWallet(null));
    }

    @Test
    void testDeleteWallet_ExistingWallet() {
        Long walletId = 1L;
        when(walletRepository.existsById(walletId)).thenReturn(true);
        walletService.deleteWallet(walletId);
        verify(walletRepository, times(1)).deleteById(walletId);
    }

    @Test
    void testDeleteWallet_NotExistingWallet() {
        Long walletId = 1L;
        when(walletRepository.existsById(walletId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> walletService.deleteWallet(walletId));
    }

    @Test
    void testUpdateWallet_WithNullId() {
        assertThrows(IllegalArgumentException.class, () -> walletService.updateWallet(null, new WalletDto()));
    }

    @Test
    void testUpdateWallet_WithNullDto() {
        assertThrows(IllegalArgumentException.class, () -> walletService.updateWallet(1L, null));
    }

    @Test
    void testUpdateWallet_WalletNotFound() {
        Long walletId = 1L;
        WalletDto walletDto = new WalletDto();

        when(walletRepository.findById(walletId)).thenReturn(java.util.Optional.empty());
        assertThrows(RuntimeException.class, () -> walletService.updateWallet(walletId, walletDto));
    }

    @Test
    void testUpdateWallet_ValidUpdate() {
        Long walletId = 1L;
        WalletDto walletDto = new WalletDto();
        walletDto.setBalance(500.0);

        Wallet wallet = new Wallet();
        wallet.setId(walletId);

        when(walletRepository.findById(walletId)).thenReturn(java.util.Optional.of(wallet));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        WalletDto updatedWalletDto = walletService.updateWallet(walletId, walletDto);
        assertNotNull(updatedWalletDto);
        assertEquals(walletDto.getBalance(), updatedWalletDto.getBalance(), 0.0);
    }

    @Test
    void testMoneyTransfer_InvalidAmount() {
        assertThrows(IllegalArgumentException.class, () -> walletService.moneyTransfer(1L, "123", -100.0));
    }

    @Test
    void testMoneyTransfer_WalletNotFound() {
        Long userId = 1L;
        when(walletRepository.findByNumber(anyString())).thenReturn(java.util.Optional.empty());
        assertThrows(RuntimeException.class, () -> walletService.moneyTransfer(userId, "123", 100.0));
    }

    @Test
    void testMoneyTransfer_ValidTransfer() {
        Long userId = 1L;
        String walletNumber = "123";
        Double amount = 100.0;
        Wallet wallet = new Wallet();
        Wallet targetWallet = new Wallet();
        when(walletRepository.findByNumber(walletNumber)).thenReturn(java.util.Optional.of(wallet));
        when(userService.getUser(userId)).thenReturn(new UserDto());
        doNothing().when(walletRepository).save(any());
        walletService.moneyTransfer(userId, walletNumber, amount);
        assertEquals(wallet.getBalance(), -amount, 0.0);
        assertEquals(targetWallet.getBalance(), amount, 0.0);
    }

    @Test
    void testTransferToWallet_EnoughBalance() {
        Wallet user1Wallet = new Wallet();
        user1Wallet.setBalance(100.0);

        Wallet user2Wallet = new Wallet();
        user2Wallet.setBalance(0.0);

        Double amount = 50.0;

        walletService.transferToWallet(user1Wallet, user2Wallet, amount);
        assertEquals(user1Wallet.getBalance(), 50.0, 0.0);
        assertEquals(user2Wallet.getBalance(), 50.0, 0.0);
    }

    @Test
    void testTransferToWallet_InsufficientBalance() {
        Wallet user1Wallet = new Wallet();
        user1Wallet.setBalance(100.0);
        Wallet user2Wallet = new Wallet();
        user2Wallet.setBalance(0.0);
        Double amount = 150.0;
        assertThrows(RuntimeException.class, () -> walletService.transferToWallet(user1Wallet, user2Wallet, amount));
    }

    @Test
    void testTransferToDefaultCard_EnoughBalance() {
        Long userId = 1L;
        Double amount = 50.0;

        Wallet wallet = new Wallet();
        wallet.setBalance(amount * 2);

        List<Card> cards = new ArrayList<>();
        Card defaultCard = new Card();
        defaultCard.setIsDefault(true);
        defaultCard.setAccount(0.0);
        cards.add(defaultCard);

        User user = new User();
        user.setWallet(wallet);
        user.setListOfCards(cards);

        when(userService.getUser(userId)).thenReturn(UserDto.mapEntityToDto(user));

        walletService.transferToDefaultCard(userId, amount);

        assertEquals(wallet.getBalance(), amount, 0.0);
        assertEquals(defaultCard.getAccount(), amount, 0.0);
    }

    @Test
    void testTransferToDefaultCard_InsufficientBalance() {
        Long userId = 1L;
        Double amount = 50.0;

        Wallet wallet = new Wallet();
        wallet.setBalance(amount - 1);

        User user = new User();
        user.setWallet(wallet);

        when(userService.getUser(userId)).thenReturn(UserDto.mapEntityToDto(user));

        assertThrows(RuntimeException.class, () -> walletService.transferToDefaultCard(userId, amount));
    }


    @Test
    void testTransferToCard_EnoughBalance() {
        Long userId = 1L;
        Long cardNumber = 1234567890L;
        double amount = 50.0;

        Card card = new Card();
        card.setCardNumber(cardNumber);
        card.setAccount(0.0);

        Wallet wallet = new Wallet();
        wallet.setBalance(amount * 2);

        User user = new User();
        user.setWallet(wallet);

        when(cardService.getCardByCardNumber(cardNumber)).thenReturn(card);
        when(userService.getUser(userId)).thenReturn(UserDto.mapEntityToDto(user));
        walletService.transferToCard(userId, cardNumber, amount);
        assertEquals(wallet.getBalance(), amount, 0.0);
        assertEquals(card.getAccount(), amount, 0.0);
    }

    @Test
    void testTransferToCard_InsufficientBalance() {
        Long userId = 1L;
        Long cardNumber = 1234567890L;
        double amount = 50.0;

        Card card = new Card();
        card.setCardNumber(cardNumber);
        card.setAccount(0.0);

        Wallet wallet = new Wallet();
        wallet.setBalance(amount - 1);

        User user = new User();
        user.setWallet(wallet);

        when(cardService.getCardByCardNumber(cardNumber)).thenReturn(card);
        when(userService.getUser(userId)).thenReturn(UserDto.mapEntityToDto(user));

        assertThrows(RuntimeException.class, () -> walletService.transferToCard(userId, cardNumber, amount));
    }
}




