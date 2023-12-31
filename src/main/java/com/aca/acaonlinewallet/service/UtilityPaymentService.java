package com.aca.acaonlinewallet.service;

import com.aca.acaonlinewallet.dto.UserDto;
import com.aca.acaonlinewallet.dto.UtilityPaymentDto;
import com.aca.acaonlinewallet.dto.WalletDto;
import com.aca.acaonlinewallet.entity.UtilityPayment;
import com.aca.acaonlinewallet.repository.UtilityPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilityPaymentService {
    private final Logger logger = LoggerFactory.getLogger(UtilityPaymentService.class);
    private final UtilityPaymentRepository utilityPaymentRepository;
    private final UserService userService;
    private final WalletService walletService;

    public List<UtilityPaymentDto> getUserUtilities(Long userId) {
        logger.info("Getting utilityPayments for user with id: {}", userId);
        List<UtilityPayment> utilityPaymentsByUserId = utilityPaymentRepository.getUtilityPaymentsByUser_Id(userId);
        if (CollectionUtils.isEmpty(utilityPaymentsByUserId)) {
            logger.info("No utilityPayments exist for user with id: {}", userId);
            throw new IllegalArgumentException(String.format("No utilities exist of user: '%s'", userId));
        }

        logger.info("UtilityPayments retrieved for user with id {}: {}", userId, utilityPaymentsByUserId);
        return UtilityPaymentDto.mapEntitiesToDtos(utilityPaymentsByUserId);
    }

    public UtilityPaymentDto getUtilityPayment(Long id) {
        logger.info("Getting utility payment with id: {}", id);
        UtilityPayment utilityPayment = utilityPaymentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format("No utility exists with id: '%s'", id)));
        logger.info("Utility payment retrieved with id {}: {}", id, utilityPayment);
        return UtilityPaymentDto.mapEntityToDto(utilityPayment);
    }

    @Transactional
    public UtilityPaymentDto addUtilityPayment(UtilityPaymentDto utilityPaymentDto, Long userId) {
        logger.info("Adding utility payment for user with id: {}", userId);
        UserDto user = userService.getUser(userId);
        utilityPaymentDto.setUserDto(user);
        utilityPaymentDto.setAddedDate(new Date());
        UtilityPayment savedUtility = utilityPaymentRepository.save(UtilityPaymentDto.mapDtoToEntity(utilityPaymentDto));
        logger.info("Utility payment added: {}", savedUtility);

        return UtilityPaymentDto.mapEntityToDto(savedUtility);
    }


    @Transactional
    public UtilityPaymentDto updateUtilityPayment(Long id, UtilityPaymentDto updatedUtilityPayment) {
        logger.info("Updating utility payment with id: {}", id);
        utilityPaymentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(String.format("No utility exists with id: '%s'", id)));
        updatedUtilityPayment.setId(id);
        UtilityPayment updatedUtility = utilityPaymentRepository.save(UtilityPaymentDto.mapDtoToEntity(updatedUtilityPayment));
        logger.info("Utility payment updated with id {}: {}", id, updatedUtility);
        return UtilityPaymentDto.mapEntityToDto(utilityPaymentRepository.save(UtilityPaymentDto.mapDtoToEntity(updatedUtilityPayment)));
    }

    @Transactional
    public void removeUtility(Long id) {
        logger.info("Removing utility payment with id: {}", id);
        boolean existsById = utilityPaymentRepository.existsById(id);
        if (!existsById) {
            throw new IllegalArgumentException(String.format("Not utility exists with id: '%s'", id));
        }

        utilityPaymentRepository.deleteById(id);
        logger.info("Utility payment removed with id: {}", id);
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