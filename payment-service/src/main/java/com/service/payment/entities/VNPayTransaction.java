package com.service.payment.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vnpay_transactions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class VNPayTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment; // payment_id

    @Column(name = "transaction_no", nullable = false)
    private String transactionNo; // vnp_TransactionNo

    @Column(name = "amount", nullable = false)
    private Long amount; // vnp_Amount / 100

    @Column(name = "status", nullable = false)
    private String status; // "PAID" hoặc "FAILED"

    @Column(name = "response_code", nullable = false)
    private String responseCode; // vnp_ResponseCode

    @Column(name = "pay_date", nullable = false)
    private LocalDateTime payDate; // vnp_PayDate

    @Column(name = "bank_code")
    private String bankCode; // vnp_BankCode

    @Column(name = "card_type")
    private String cardType; // vnp_CardType

    @Column(name = "bank_tran_no")
    private String bankTranNo; // vnp_BankTranNo

    @Column(name = "order_info")
    private String orderInfo; // vnp_OrderInfo
}
