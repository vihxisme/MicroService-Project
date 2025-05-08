package com.service.customer.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.customer.dtos.GrowthResult;
import com.service.customer.dtos.StatsNewCustomerDTO;
import com.service.customer.entities.Customer;
import com.service.customer.mappers.CustomerMapper;
import com.service.customer.repositories.ApiClient;
import com.service.customer.repositories.CustomerRepository;
import com.service.customer.requests.AddInfoCustomerRequest;
import com.service.customer.requests.AvatarRequest;
import com.service.customer.requests.PaginationRequest;
import com.service.customer.requests.UpdateInfoCustomerRequest;
import com.service.customer.resources.AvatarResource;
import com.service.customer.resources.CustomerProfileResource;
import com.service.customer.resources.CustomerTransactionResource;
import com.service.customer.resources.ProfileResource;
import com.service.customer.responses.PaginationResponse;
import com.service.customer.services.interfaces.CustomerInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CustomerService implements CustomerInterface {

    private Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ApiClient apiClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public Customer updateInfoCustomer(UpdateInfoCustomerRequest request) {

        Optional<Customer> existingCustomerOptional = customerRepository
                .findByAuthUserId(request.getAuthUserId());

        if (existingCustomerOptional.isPresent()) {
            Customer existingCustomer = existingCustomerOptional.get();

            customerMapper.updateCustomerFromDto(request, existingCustomer);

            return customerRepository.save(existingCustomer);
        } else {
            throw new EntityNotFoundException(
                    "Customer with authUserId " + request.getAuthUserId() + " not found");
        }
    }

    public String generateCustomerCode(String prefix) {
        Boolean isUnique;
        Random random = new Random();
        String code;
        do {
            int randomNumberFive = random.nextInt(10000, 99999);
            int randomNumberThree = random.nextInt(100, 999);
            code = String.format("%s-%05d-%03d", prefix, randomNumberFive, randomNumberThree);
            isUnique = !customerRepository.existsByCustomerCode(code);
        } while (!isUnique);

        return code;
    }

    @Override
    @Transactional
    public AvatarResource updateAvatar(AvatarRequest request) {
        Boolean updAvatar = customerRepository.updateAvatar(request.getId(), request.getAvatar()) > 0;

        return new AvatarResource(updAvatar);
    }

    @Override
    @Transactional
    public Customer addInfoCustomer(AddInfoCustomerRequest request) {

        Optional<Customer> existingCustomerOptional = customerRepository
                .findByAuthUserId(request.getAuthUserId());

        if (!existingCustomerOptional.isPresent()) {

            String customerCode = generateCustomerCode("PGX");

            Customer customer = customerMapper.toCustomer(request);
            customer.setCustomerCode(customerCode);

            return customerRepository.save(customer);
        } else {
            throw new EntityNotFoundException(
                    "Customer with authUserId " + request.getAuthUserId() + " already exists");
        }
    }

    @Override
    public Customer getCustomer(UUID id) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (!customer.isPresent()) {
            throw new EntityNotFoundException("Customer with id " + customer.get().getId() + " not found");
        }

        return customer.get();
    }

    @Override
    public UUID getIdByAuthUserId(UUID authUserId) {
        Optional<UUID> id = customerRepository.findIdByAuthUserId(authUserId);

        if (id.isPresent()) {
            return id.get();
        } else {
            throw new EntityNotFoundException("Customer not found");
        }
    }

    @Override
    public PaginationResponse<CustomerProfileResource> getAllCustomer(PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Customer> page = customerRepository.findAll(pageable);

        Page<CustomerProfileResource> customerProfilePage = page.map(customerMapper::toProfileResource);

        return PaginationResponse.<CustomerProfileResource>builder()
                .content(customerProfilePage.getContent())
                .pageNumber(customerProfilePage.getNumber())
                .pageSize(customerProfilePage.getSize())
                .totalPages(customerProfilePage.getTotalPages())
                .totalElements(customerProfilePage.getTotalElements())
                .build();

    }

    @Override
    public ProfileResource getProfileById(UUID id) {
        ProfileResource profileResource = customerRepository.findProfileById(id);

        if (profileResource == null) {
            throw new EntityNotFoundException("Not found");
        }

        return profileResource;
    }

    @Override
    public PaginationResponse<CustomerTransactionResource> getCustomerTransaction(PaginationRequest request) {
        ResponseEntity<?> response = apiClient.getCustomerTransaction(request.getPage(), request.getSize());

        PaginationResponse<CustomerTransactionResource> customerTransactions = objectMapper.convertValue(
                response.getBody(),
                new TypeReference<PaginationResponse<CustomerTransactionResource>>() {

        });

        return customerTransactions;
    }

    @Override
    public Long countCustomer() {
        return customerRepository.countCustomer();
    }

    @Override
    public GrowthResult<StatsNewCustomerDTO> getStatsNewCustomerByRangeType(String rangeType) {
        switch (rangeType.toLowerCase()) {
            case "day" -> {
                return getStatsNewCustomerByPeriod(Duration.ofDays(1));
            }
            case "week" -> {
                return getStatsNewCustomerByPeriod(Duration.ofDays(7));
            }
            case "month" -> {
                return getStatsNewCustomerByPeriod(Period.ofMonths(1));
            }
            case "quarter" -> {
                return getStatsNewCustomerByPeriod(Period.ofMonths(3));
            }
            case "year" -> {
                return getStatsNewCustomerByPeriod(Period.ofYears(1));
            }
            default ->
                throw new IllegalArgumentException(String.format("Invalid Range: %s", rangeType));
        }
    }

    private GrowthResult<StatsNewCustomerDTO> getStatsNewCustomerByPeriod(TemporalAmount period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentStart = now.minus(period);
        LocalDateTime previousStart = currentStart.minus(period);
        LocalDateTime previousEnd = currentStart;

        // Current period
        StatsNewCustomerDTO current = customerRepository.countNewCustomerBetween(currentStart, now);
        StatsNewCustomerDTO previous = customerRepository.countNewCustomerBetween(previousStart, previousEnd);

        return new GrowthResult<StatsNewCustomerDTO>(
                current,
                previous,
                calculateGrowth(BigDecimal.valueOf(previous.getTotalCustomers()), BigDecimal.valueOf(current.getTotalCustomers()))
        );
    }

    private BigDecimal calculateGrowth(BigDecimal prev, BigDecimal curr) {
        if (prev == null || prev.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return curr.subtract(prev)
                .divide(prev, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    private List<CustomerProfileResource> getNewCustomerStatsByPeriod(Integer limit, TemporalAmount period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentStart = now.minus(period);

        Pageable pageable = PageRequest.of(0, limit);

        List<CustomerProfileResource> customerProfileResources = customerRepository.findNewCustomerByRangeType(pageable, currentStart, now);

        return customerProfileResources;
    }

    @Override
    public List<CustomerProfileResource> getNewCustomerStatsByRangeType(Integer limit, String rangeType) {
        switch (rangeType.toLowerCase()) {
            case "day" -> {
                return getNewCustomerStatsByPeriod(limit, Duration.ofDays(1));
            }
            case "week" -> {
                return getNewCustomerStatsByPeriod(limit, Duration.ofDays(7));
            }
            case "month" -> {
                return getNewCustomerStatsByPeriod(limit, Period.ofMonths(1));
            }
            case "quarter" -> {
                return getNewCustomerStatsByPeriod(limit, Period.ofMonths(3));
            }
            case "year" -> {
                return getNewCustomerStatsByPeriod(limit, Period.ofYears(1));
            }
            default ->
                throw new IllegalArgumentException(String.format("Invalid Range: %s", rangeType));
        }
    }

    @Override
    public List<CustomerTransactionResource> getNewCustomerWTransactionStatsByRangeType(Integer limit,
            String rangeType) {
        ResponseEntity<?> response = apiClient.getNewCustomerWithTransactionByRangeType(limit, rangeType);

        List<CustomerTransactionResource> customerTransactionResources = objectMapper.convertValue(response.getBody(),
                new TypeReference<List<CustomerTransactionResource>>() {
        });

        return customerTransactionResources;
    }

}
