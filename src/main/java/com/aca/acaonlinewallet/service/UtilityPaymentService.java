package com.aca.acaonlinewallet.service;

import com.aca.acaonlinewallet.dto.UserDto;
import com.aca.acaonlinewallet.dto.UtilityPaymentDto;
import com.aca.acaonlinewallet.dto.WalletDto;
import com.aca.acaonlinewallet.entity.UtilityPayment;
import com.aca.acaonlinewallet.repository.UtilityPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilityPaymentService {
    private final UtilityPaymentRepository utilityPaymentRepository;
    private final UserService userService;
    private final WalletService walletService;

    public List<UtilityPaymentDto> getUserUtilities(Long userId) {
        List<UtilityPayment> utilityPaymentsByUserId = utilityPaymentRepository.getUtilityPaymentsByUser_Id(userId);
        if (CollectionUtils.isEmpty(utilityPaymentsByUserId)) {
            throw new IllegalArgumentException(String.format("No utilities exist of user: '%s'", userId));
        }

        return UtilityPaymentDto.mapEntitiesToDtos(utilityPaymentsByUserId);
    }

    public UtilityPaymentDto getUtilityPayment(Long id) {
        UtilityPayment utilityPayment = utilityPaymentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format("No utility exists with id: '%s'", id)));
        return UtilityPaymentDto.mapEntityToDto(utilityPayment);
    }

    @Transactional
    public UtilityPaymentDto addUtilityPayment(UtilityPaymentDto utilityPaymentDto, Long userId) {
        UserDto user = userService.getUser(userId);
        utilityPaymentDto.setUserDto(user);
        utilityPaymentDto.setAddedDate(new Date());
        UtilityPayment savedUtility = utilityPaymentRepository.save(UtilityPaymentDto.mapDtoToEntity(utilityPaymentDto));
        return UtilityPaymentDto.mapEntityToDto(savedUtility);
    }


    @Transactional
    public UtilityPaymentDto updateUtilityPayment(Long id, UtilityPaymentDto updatedUtilityPayment) {
        utilityPaymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(String.format("No utility exists with id: '%s'", id)));
        updatedUtilityPayment.setId(id);
        return UtilityPaymentDto.mapEntityToDto(utilityPaymentRepository.save(UtilityPaymentDto.mapDtoToEntity(updatedUtilityPayment)));
    }

    @Transactional
    public void removeUtility(Long id) {
        boolean existsById = utilityPaymentRepository.existsById(id);
        if (!existsById) {
            throw new IllegalArgumentException(String.format("Not utility exists with id: '%s'", id));
        }

        utilityPaymentRepository.deleteById(id);
    }

    @Transactional
    public UtilityPaymentDto payUtility(String type, Double amount, Long userId, Long walletId) {
        UtilityPayment utilityPayment = utilityPaymentRepository.getUtilityPaymentByTypeAndUser_Id(type, userId).orElseThrow(() ->
                new IllegalArgumentException(String.format("No utilities found by type: '%s' and user: '%s'", type, userId)));
        Double paidAmount = utilityPayment.getPaidAmount();
        Double amountDue = utilityPayment.getAmountDue();
        if (Boolean.TRUE.equals(utilityPayment.getIsPaid())) {
            throw new RuntimeException("Utility is already paid");
        }

        WalletDto wallet = walletService.getWallet(walletId);
        if (wallet.getBalance() < amount) {
            throw new RuntimeException("insufficient funds");
        }

        if (amountDue < (paidAmount + amount)) {
            throw new RuntimeException(
                    String.format("Paid amount shouldn't be more than amountDue. paidAmount: '%s' amountDue: '%s'",
                            paidAmount + amount, amountDue));
        }

        wallet.setBalance(wallet.getBalance() - amount);
        utilityPayment.setPaidAmount(paidAmount + amount);
        if (paidAmount.equals(amountDue)) {
            utilityPayment.setIsPaid(true);
        }
        walletService.updateWallet(walletId, wallet);
        utilityPayment.setPaidDate(new Date());
        return UtilityPaymentDto.mapEntityToDto(utilityPaymentRepository.save(utilityPayment));
    }

}