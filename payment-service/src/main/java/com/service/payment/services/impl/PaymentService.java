package com.service.payment.services.impl;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.service.payment.configurations.VNPAYConfig;
import com.service.payment.entities.Payment;
import com.service.payment.enums.PaymentStatus;
import com.service.payment.mappers.PaymentMapper;
import com.service.payment.repositories.PaymentRepository;
import com.service.payment.requests.PaginationRequest;
import com.service.payment.requests.PaymentRequest;
import com.service.payment.responses.PaginationResponse;
import com.service.payment.services.interfaces.PaymentInterface;
import com.service.payment.utils.VNPayUtil;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentInterface {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private RabbitService rabbitService;

    private final VNPAYConfig vnpayConfig;

    private Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @RabbitListener(queues = "create-payment:queue")
    public void createPaymentListener(Message message) {
        Optional<PaymentRequest> request = rabbitService.deserializeToObject(message.getBody(),
                PaymentRequest.class);

        if (request.isPresent()) {
            createPayment(request.get());
        }
    }

    @Override
    @Transactional
    public Payment createPayment(PaymentRequest request) {
        if (request.getPaymentCode() == null) {
            request.setPaymentCode(generatePaymentCode());
        }

        logger.info("payment Method: {}", request.getPaymentMethod());

        Payment payment = paymentMapper.toPayment(request);

        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public Payment updatePayment(PaymentRequest request) {
        Payment existPayment = paymentRepository.findById(request.getId()).orElseThrow(()
                -> new EntityNotFoundException("Payment not found"));

        paymentMapper.updatePaymentFromRequest(request, existPayment);

        return paymentRepository.save(existPayment);
    }

    @Override
    @Transactional
    public Boolean deletePayment(UUID id) {
        Payment existPayment = paymentRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Payment not found"));

        paymentRepository.delete(existPayment);

        return true;
    }

    private String generatePaymentCode() {
        Boolean isUnique;
        Random random = new Random();
        String code;
        do {
            int randomNumberStart = random.nextInt(100, 999);
            int randomNumberMiddle = random.nextInt(1000, 9999);
            int randomNumberEnd = random.nextInt(1000, 9999);
            code = String.format("%03d-%05d-%05d", randomNumberStart, randomNumberMiddle, randomNumberEnd);
            isUnique = !paymentRepository.existsByPaymentCode(code);
        } while (!isUnique);

        return code;
    }

    @Override
    public PaginationResponse<Payment> getPagiPayment(PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("updatedAt").descending());

        Page<Payment> paymentPage = paymentRepository.findAll(pageable);

        return PaginationResponse.<Payment>builder()
                .content(paymentPage.getContent())
                .pageNumber(paymentPage.getNumber())
                .pageSize(paymentPage.getSize())
                .totalPages(paymentPage.getTotalPages())
                .totalElements(paymentPage.getTotalElements())
                .build();
    }

    @Override
    public Map<PaymentStatus, Long> getPaymentStatusCount() {
        List<Object[]> result = paymentRepository.countByPaymentStatus();

        // Tạo map kết quả từ truy vấn
        Map<PaymentStatus, Long> resultMap = result.stream()
                .collect(Collectors.toMap(
                        row -> PaymentStatus.fromCode((Integer) row[0]), // ✅ convert từ int → enum
                        row -> (Long) row[1]
                ));

        // Đảm bảo có đủ tất cả các trạng thái, nếu thiếu thì thêm vào với giá trị 0
        for (PaymentStatus status : PaymentStatus.values()) {
            resultMap.putIfAbsent(status, 0L);
        }

        return resultMap;
    }

    @Override
    public String createVnPayUrl(PaymentRequest request, HttpServletRequest httpRequest) {
        String hostIp = null;
        try {
            hostIp = InetAddress.getByName("host.docker.internal").getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Map<String, String> vnp_Params = vnpayConfig.getVNPayConfig();
        vnp_Params.put("vnp_TxnRef", request.getOrderCode()); // Mã giao dịch (mã đơn hàng)
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang: #" + request.getOrderCode());
        vnp_Params.put("vnp_Amount", request.getTotalAmount().multiply(BigDecimal.valueOf(100L)).toPlainString()); // VNPAY yêu cầu số tiền tính bằng đồng
        vnp_Params.put("vnp_IpAddr", hostIp); // Lấy địa chỉ IP của người dùng (có thể thay đổi theo yêu cầu)
        vnp_Params.put("vnp_BankCode", "VNPAY"); // Mã ngân hàng (có thể thay đổi theo yêu cầu)

        String queryUrl = VNPayUtil.getPaymentURL(vnp_Params, true);
        String hashData = VNPayUtil.getPaymentURL(vnp_Params, false);
        String signData = VNPayUtil.hmacSHA512(vnpayConfig.getVnp_secretKey(), hashData);
        String paymentUrl = vnpayConfig.getVnp_payUrl() + "?" + queryUrl + "&vnp_SecureHash=" + signData;

        return paymentUrl;
    }

    public String getClientIp(HttpServletRequest request) {
        String clientIp;
        // Lấy IP từ header X-Forwarded-For (nếu có, trong trường hợp qua proxy)
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // Xử lý khi có nhiều IP trong header X-Forwarded-For
            clientIp = xForwardedFor.split(",")[0];
        } else {
            // Lấy IP thực của client nếu không có header X-Forwarded-For
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

    @RabbitListener(queues = "cancel-payment:queue")
    public void cancelPaymentListener(Message message) {
        Optional<UUID> orderId = rabbitService.deserializeToObject(message.getBody(),
                UUID.class);

        if (orderId.isPresent()) {
            Payment payment = paymentRepository.findByOrderId(orderId.get());

            if (payment == null) {
                throw new EntityNotFoundException("Payment not found");
            } else {
                if (payment.getPaymentStatus() == 2) {
                    payment.setPaymentStatus(4);
                    paymentRepository.save(payment);
                } else {
                    payment.setPaymentStatus(5);
                }
            }
        }
    }
}
