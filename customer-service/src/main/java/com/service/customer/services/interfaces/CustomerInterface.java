package com.service.customer.services.interfaces;

import java.util.UUID;

import com.service.customer.entities.Customer;
import com.service.customer.requests.AddInfoCustomerRequest;
import com.service.customer.requests.AvatarRequest;
import com.service.customer.requests.PaginationRequest;
import com.service.customer.requests.UpdateInfoCustomerRequest;
import com.service.customer.resources.AvatarResource;
import com.service.customer.resources.CustomerProfileResource;
import com.service.customer.responses.PaginationResponse;

public interface CustomerInterface {
  Customer updateInfoCustomer(UpdateInfoCustomerRequest request);

  Customer addInfoCustomer(AddInfoCustomerRequest request);

  Customer getCustomer(UUID id);

  AvatarResource updateAvatar(AvatarRequest request);

  UUID getIdByAuthUserId(UUID authUserId);

  PaginationResponse<CustomerProfileResource> getAllCustomer(PaginationRequest request);
}
