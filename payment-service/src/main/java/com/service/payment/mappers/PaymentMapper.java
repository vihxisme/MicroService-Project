package com.service.payment.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.payment.entities.Payment;
import com.service.payment.requests.PaymentRequest;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentMapper {

    Payment toPayment(PaymentRequest request);

    void updatePaymentFromRequest(PaymentRequest request, @MappingTarget Payment payment);
}
