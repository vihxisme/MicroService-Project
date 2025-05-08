package com.service.payment.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.payment.entities.VNPayTransaction;
import com.service.payment.requests.VNPayTransactionRequest;

@Mapper(componentModel = "spring", uses = {Convert.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VNPayTransactionMapper {

    @Mapping(target = "payment", source = "paymentId", qualifiedByName = "paymentIdToPayment")
    VNPayTransaction toVNPayTransaction(VNPayTransactionRequest request);
}
