package com.service.customer.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.customer.entities.Address;
import com.service.customer.requests.AddAddressRequest;
import com.service.customer.requests.UpdateAddrerssRequest;

@Mapper(componentModel = "spring", uses = {
    CustomerConverter.class }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {
  @Mapping(target = "customerID", source = "customerID", qualifiedByName = "UUIDtoCustomer")
  Address toAddress(AddAddressRequest request);

  @Mapping(target = "customerID", source = "customerID", qualifiedByName = "UUIDtoCustomer")
  void updateAddressFromDto(UpdateAddrerssRequest dto, @MappingTarget Address address);
}
