package com.aca.acaonlinewallet.serviceTest;

import com.aca.acaonlinewallet.dto.UserDto;
import com.aca.acaonlinewallet.dto.UtilityPaymentDto;
import com.aca.acaonlinewallet.dto.WalletDto;
import com.aca.acaonlinewallet.entity.UtilityPayment;
import com.aca.acaonlinewallet.repository.UtilityPaymentRepository;
import com.aca.acaonlinewallet.service.UserService;
import com.aca.acaonlinewallet.service.UtilityPaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UtilityPaymentServiceTests {

    @Mock
    UtilityPaymentRepository utilityPaymentRepository;

    @Mock
    UserService userService;

    @InjectMocks
    UtilityPaymentService utilityPaymentService;

    @Test
    void getUserUtilitiesWhenIdIsIncorrect() {

        when(utilityPaymentRepository.getUtilityPaymentsByUser_Id(1L)).thenReturn(Collections.emptyList());
        assertThrows(RuntimeException.class, () -> utilityPaymentService.getUserUtilities(1L));
    }

    @Test
    void getUserUtilitiesWhenIdIsCorrect() {
        List<UtilityPayment> utilityPayments = new ArrayList<>();
        UtilityPayment utilityPayment = new UtilityPayment();

        utilityPayment.setType(UtilityPaymentDto.UtilityPaymentType.NATURAL_GAS.getValue());
        utilityPayments.add(utilityPayment);
        when(utilityPaymentRepository.getUtilityPaymentsByUser_Id(1L)).thenReturn(utilityPayments);
        List<UtilityPaymentDto> resultList = utilityPaymentService.getUserUtilities(1L);
        assertEquals(resultList.get(0).getId(), utilityPayments.get(0).getId());
    }

    @Test
    void getUtilityPaymentTestWhenIdIsIncorrect() {
        when(utilityPaymentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> utilityPaymentService.getUtilityPayment(1L));
    }

    @Test
    void getUtilityPaymentTestWhenIdIsCorrect() {
        UtilityPayment utilityPayment = new UtilityPayment();
        utilityPayment.setId(1L);
        utilityPayment.setType(UtilityPaymentDto.UtilityPaymentType.NATURAL_GAS.getValue());
        when(utilityPaymentRepository.findById(1L)).thenReturn(Optional.of(utilityPayment));
        UtilityPaymentDto utilityPaymentDto = utilityPaymentService.getUtilityPayment(1L);
        assertEquals(utilityPaymentDto.getId(), utilityPayment.getId());
    }

    @Test
    void addUtilityPaymentTest() {
        UserDto userDto = new UserDto();
        userDto.setWalletDto(new WalletDto());
        userDto.setId(1L);
        when(userService.getUser(1L)).thenReturn(userDto);
        UtilityPaymentDto utilityPaymentDto = new UtilityPaymentDto();
        utilityPaymentDto.setUserDto(userDto);
        utilityPaymentDto.setType(UtilityPaymentDto.UtilityPaymentType.NATURAL_GAS);
        when(utilityPaymentRepository.save(any(UtilityPayment.class))).thenReturn(UtilityPaymentDto.mapDtoToEntity(utilityPaymentDto));
        UtilityPaymentDto result = utilityPaymentService.addUtilityPayment(utilityPaymentDto, 1L);
        assertEquals(result.getId(), utilityPaymentDto.getId());
    }

    @Test
    void updateUtilityPaymentWhenIdIsIncorrect() {
        when(utilityPaymentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> utilityPaymentService.updateUtilityPayment(1L, new UtilityPaymentDto()));
    }

    @Test
    void updateUtilityPaymentWhenIdIsCorrect() {
        UtilityPaymentDto utilityPaymentDto = new UtilityPaymentDto();
        utilityPaymentDto.setId(2L);
        utilityPaymentDto.setType(UtilityPaymentDto.UtilityPaymentType.NATURAL_GAS);
        when(utilityPaymentRepository.findById(2L)).thenReturn(Optional.of(UtilityPaymentDto.mapDtoToEntity(utilityPaymentDto)));
        when(utilityPaymentRepository.save(any(UtilityPayment.class))).thenReturn(UtilityPaymentDto.mapDtoToEntity(utilityPaymentDto));
        UtilityPaymentDto result = utilityPaymentService.updateUtilityPayment(2L, utilityPaymentDto);
        assertEquals(result.getId(), utilityPaymentDto.getId());
    }

    @Test
    void removeUtilityWhenIdIsNotFound() {
        when(utilityPaymentRepository.existsById(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> utilityPaymentService.removeUtility(1L));
    }

    @Test
    void removeUtilityWhenIdIsCorrect() {
        when(utilityPaymentRepository.existsById(1L)).thenReturn(true);
        utilityPaymentService.removeUtility(1L);
        verify(utilityPaymentRepository, times(1)).deleteById(1L);
    }

}
