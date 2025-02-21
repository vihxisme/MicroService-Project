package com.service.customer.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.service.customer.entities.Customer;
import com.service.customer.requests.AddInfoCustomerRequest;
import com.service.customer.requests.UpdateInfoCustomerRequest;
import com.service.customer.resources.CustomerProfileResource;

@Mapper(componentModel = "spring", uses = {
    GenderConverter.class }, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

  Customer toCustomer(AddInfoCustomerRequest request);

  @Mapping(target = "gender", source = "gender", qualifiedByName = "fromString")
  @Mapping(target = "dateOfBirth", source = "dob")
  void updateCustomerFromDto(UpdateInfoCustomerRequest dto, @MappingTarget Customer customer);

  CustomerProfileResource toProfileResource(Customer customer);
}
