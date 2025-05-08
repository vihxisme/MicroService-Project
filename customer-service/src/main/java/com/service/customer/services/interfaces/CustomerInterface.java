package com.service.customer.services.interfaces;

import java.util.List;
import java.util.UUID;

import com.service.customer.dtos.GrowthResult;
import com.service.customer.dtos.StatsNewCustomerDTO;
import com.service.customer.entities.Customer;
import com.service.customer.requests.AddInfoCustomerRequest;
import com.service.customer.requests.AvatarRequest;
import com.service.customer.requests.PaginationRequest;
import com.service.customer.requests.UpdateInfoCustomerRequest;
import com.service.customer.resources.AvatarResource;
import com.service.customer.resources.CustomerProfileResource;
import com.service.customer.resources.CustomerTransactionResource;
import com.service.customer.resources.ProfileResource;
import com.service.customer.responses.PaginationResponse;

public interface CustomerInterface {

    Long countCustomer();

    Customer updateInfoCustomer(UpdateInfoCustomerRequest request);

    Customer addInfoCustomer(AddInfoCustomerRequest request);

    Customer getCustomer(UUID id);

    AvatarResource updateAvatar(AvatarRequest request);

    ProfileResource getProfileById(UUID id);

    UUID getIdByAuthUserId(UUID authUserId);

    PaginationResponse<CustomerProfileResource> getAllCustomer(PaginationRequest request);

    PaginationResponse<CustomerTransactionResource> getCustomerTransaction(PaginationRequest request);

    GrowthResult<StatsNewCustomerDTO> getStatsNewCustomerByRangeType(String rangeType);

    List<CustomerProfileResource> getNewCustomerStatsByRangeType(Integer limit, String rangeType);

    List<CustomerTransactionResource> getNewCustomerWTransactionStatsByRangeType(Integer limit, String rangeType);
}
