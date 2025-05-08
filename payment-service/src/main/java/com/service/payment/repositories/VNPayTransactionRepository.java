package com.service.payment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.payment.entities.VNPayTransaction;

@Repository
public interface VNPayTransactionRepository extends JpaRepository<VNPayTransaction, Long> {

}
