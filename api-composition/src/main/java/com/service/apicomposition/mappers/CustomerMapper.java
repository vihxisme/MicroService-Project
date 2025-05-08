package com.service.apicomposition.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.apicomposition.resources.CustomerProfileResource;
import com.service.apicomposition.resources.CustomerTransactionResources;
import com.service.apicomposition.resources.TransactionResource;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    @Mapping(target = "id", source = "customerProfileResource.id")
    @Mapping(target = "totalSpent", source = "transactionResource.totalSpent", defaultValue = "0")
    @Mapping(target = "successfulTransactions", source = "transactionResource.successfulTransactions", defaultValue = "0L")
    CustomerTransactionResources toCustomerTransactionResources(
            CustomerProfileResource customerProfileResource,
            TransactionResource transactionResource);
}
