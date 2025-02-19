package com.service.customer.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.service.customer.entities.Address;
import com.service.customer.requests.AddAddressRequest;
import com.service.customer.requests.UpdateAddrerssRequest;

public interface AddressInterface {
  Address addAddress(AddAddressRequest request);

  List<Address> getAllAddressByCustomer(UUID customerId);

  Address updateAddress(UpdateAddrerssRequest request);

  Boolean deleteAddress(Long id);
}
