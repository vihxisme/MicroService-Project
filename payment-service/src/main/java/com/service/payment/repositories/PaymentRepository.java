package com.service.payment.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.payment.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    boolean existsByPaymentCode(String code);

}
