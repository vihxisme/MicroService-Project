package com.service.payment.controllers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.payment.entities.Payment;
import com.service.payment.repositories.PaymentRepository;
import com.service.payment.requests.PaginationRequest;
import com.service.payment.requests.PaymentRequest;
import com.service.payment.requests.VNPayTransactionRequest;
import com.service.payment.resources.VNPayResource;
import com.service.payment.responses.SuccessResponse;
import com.service.payment.services.interfaces.PaymentInterface;
import com.service.payment.services.interfaces.VNPayTransactionInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentInterface paymentInterface;

    @Autowired
    private VNPayTransactionInterface vnPayTransactionInterface;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        paymentInterface.createPayment(request))
        );
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updatePayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        paymentInterface.updatePayment(request))
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable String id) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        paymentInterface.deletePayment(UUID.fromString(id)))
        );
    }

    @GetMapping("/list")
    public ResponseEntity<?> getPagiPayment(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        paymentInterface.getPagiPayment(request))
        );
    }

    @GetMapping("/status-statistics")
    public ResponseEntity<?> getPaymentStatusCount() {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        paymentInterface.getPaymentStatusCount())
        );
    }

    @PostMapping("/create-vnpay-url")
    public ResponseEntity<?> createVnPayUrl(@RequestBody PaymentRequest request, HttpServletRequest httpRequest) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        paymentInterface.createVnPayUrl(request, httpRequest))
        );
    }

    // @GetMapping("/vnpay-callback")
    // @Transactional
    // public ResponseEntity<?> vnpayCallbackHandler(HttpServletRequest request) throws ParseException {
    //     String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
    //     String vnp_TxnRef = request.getParameter("vnp_TxnRef");
    //     String vnp_Amount = request.getParameter("vnp_Amount");
    //     String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
    //     String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
    //     String vnp_TransactionStatus = request.getParameter("vnp_TransactionStatus");
    //     String vnp_BankCode = request.getParameter("vnp_BankCode");
    //     String vnp_CardType = request.getParameter("vnp_CardType");
    //     String vnp_PayDate = request.getParameter("vnp_PayDate");
    //     String vnp_IpAddr = request.getParameter("vnp_IpAddr");
    //     SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    //     Date date = dateFormat.parse(vnp_PayDate);
    //     if (vnp_ResponseCode != null && "00".equals(vnp_ResponseCode)) {
    //         VNPayResource vnPayResource = VNPayResource.builder()
    //                 .vnp_txnRef(vnp_TxnRef)
    //                 .vnp_orderInfo(vnp_OrderInfo)
    //                 .vnp_amount(vnp_Amount)
    //                 .vnp_ipAddr(vnp_IpAddr)
    //                 .vnp_transactionStatus(vnp_TransactionStatus)
    //                 .vnp_bankCode(vnp_BankCode)
    //                 .vnp_cardType(vnp_CardType)
    //                 .vnp_responseCode(vnp_ResponseCode)
    //                 .vnp_transactionNo(vnp_TransactionNo)
    //                 .vnp_payDate(new Timestamp(date.getTime()))
    //                 .build();
    //         Payment payment = paymentRepository.findByPaymentCode(vnp_TxnRef);
    //         if (payment == null) {
    //             throw new EntityNotFoundException("Payment not found");
    //         }
    //         VNPayTransactionRequest vnPayTransactionRequest = VNPayTransactionRequest.builder()
    //                 .paymentId(payment.getId())
    //                 .transactionNo(vnp_TransactionNo)
    //                 .amount(Long.parseLong(vnp_Amount) / 100)
    //                 .status(vnp_TransactionStatus)
    //                 .responseCode(vnp_ResponseCode)
    //                 .payDate(new Timestamp(date.getTime()))
    //                 .bankCode(vnp_BankCode)
    //                 .cardType(vnp_CardType)
    //                 .orderInfo(vnp_OrderInfo)
    //                 .build();
    //         vnPayTransactionInterface.createVNPayTransaction(vnPayTransactionRequest);
    //         payment.setPaymentStatus(2);
    //         payment.setMessage("Thanh toán thành công");
    //         paymentRepository.save(payment);
    //         return ResponseEntity.ok(
    //                 new SuccessResponse<>(
    //                         "SUCCESS",
    //                         vnPayResource)
    //         );
    //     } else {
    //         return ResponseEntity.ok(
    //                 new SuccessResponse<>(
    //                         "FAILED",
    //                         vnp_ResponseCode));
    //     }
    // }
}
