package com.aca.acaonlinewallet.repository;

import com.aca.acaonlinewallet.entity.UtilityPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilityPaymentRepository extends JpaRepository<UtilityPayment, Long> {

    List<UtilityPayment> getUtilityPaymentsByUser_Id(Long userId);

    Optional<UtilityPayment> getUtilityPaymentByTypeAndUser_Id(String type, Long userId);
}