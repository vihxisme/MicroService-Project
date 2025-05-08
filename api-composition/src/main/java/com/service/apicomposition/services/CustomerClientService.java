package com.service.apicomposition.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.service.apicomposition.mappers.CustomerMapper;
import com.service.apicomposition.requests.PaginationRequest;
import com.service.apicomposition.resources.CustomerProfileResource;
import com.service.apicomposition.resources.CustomerTransactionResources;
import com.service.apicomposition.resources.TransactionResource;
import com.service.apicomposition.responses.PaginationResponse;

import jakarta.validation.constraints.Future;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerClientService {

    @Autowired
    @Qualifier("customerWebClient")
    private WebClient customerClient;

    @Autowired
    @Qualifier("orderWebClient")
    private WebClient orderClient;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private RedisService redisService;

    private Logger logger = LoggerFactory.getLogger(CustomerClientService.class);

    private final long DURATION_TTL = 3600;

    private Mono<PaginationResponse<CustomerProfileResource>> fetchCustomerPageMono(String path, int page, int size) {
        return customerClient.get()
                .uri(uriBuilder -> uriBuilder
                .path(path)
                .queryParam("page", page)
                .queryParam("size", size)
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PaginationResponse<CustomerProfileResource>>() {
                });
    }

    private Mono<List<CustomerProfileResource>> fetchNewCustomerListMono(String path, Integer limit, String rangeType) {
        return customerClient.get()
                .uri(uriBuilder -> uriBuilder
                .path(path)
                .queryParam("limit", limit)
                .queryParam("rangeType", rangeType)
                .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CustomerProfileResource>>() {
                });
    }

    private Mono<Map<UUID, TransactionResource>> transactionMono(List<CustomerProfileResource> customerList, String path) {
        return customerList.isEmpty() ? Mono.just(Collections.emptyMap())
                : orderClient.get()
                        .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("userIds", customerList.stream()
                                .map(CustomerProfileResource::getId)
                                .collect(Collectors.toList()))
                        .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<UUID, TransactionResource>>() {
                        });
    }

    private Mono<PaginationResponse<CustomerTransactionResources>> fetchCustomerTransaction(
            Mono<PaginationResponse<CustomerProfileResource>> customerListMono
    ) {
        return customerListMono.flatMap(customerPage -> {
            List<CustomerProfileResource> customerList = customerPage.getContent();

            Mono<Map<UUID, TransactionResource>> transactionMono = transactionMono(customerList, "/internal/caculate-transaction");

            return transactionMono.map(transaction -> {
                List<CustomerTransactionResources> customerTransactionList = customerList.stream()
                        .map(customer -> {
                            TransactionResource transactionResources = transaction.getOrDefault(customer.getId(), null);
                            return customerMapper.toCustomerTransactionResources(customer, transactionResources);
                        })
                        .collect(Collectors.toList());

                return PaginationResponse.<CustomerTransactionResources>builder()
                        .content(customerTransactionList)
                        .pageNumber(customerPage.getPageNumber())
                        .pageSize(customerPage.getPageSize())
                        .totalPages(customerPage.getTotalPages())
                        .totalElements(customerPage.getTotalElements())
                        .build();
            });
        });
    }

    private Mono<List<CustomerTransactionResources>> fetchNewCustomerTransaction(
            Mono<List<CustomerProfileResource>> customerListMono
    ) {
        return customerListMono.flatMap(customerList -> {

            Mono<Map<UUID, TransactionResource>> transactionMono = transactionMono(customerList, "/internal/caculate-transaction");

            return transactionMono.map(transaction -> {
                List<CustomerTransactionResources> customerTransactionList = customerList.stream()
                        .map(customer -> {
                            TransactionResource transactionResources = transaction.getOrDefault(customer.getId(), null);
                            return customerMapper.toCustomerTransactionResources(customer, transactionResources);
                        })
                        .collect(Collectors.toList());

                return customerTransactionList;
            })
                    .doOnError(error -> logger.error("Error: ", error));
        });
    }

    public Mono<PaginationResponse<CustomerTransactionResources>> getCustomerTransaction(PaginationRequest request) {
        String cacheKey = String.format("customer:transaction:%d:%d", request.getPage(), request.getSize());

        return redisService.getData(cacheKey, new ParameterizedTypeReference<PaginationResponse<CustomerTransactionResources>>() {
        }).switchIfEmpty(fetchCustomerTransaction(
                fetchCustomerPageMono("/internal/profile/info/all", request.getPage(), request.getSize())
        )).flatMap(data -> redisService.saveData(cacheKey, data, DURATION_TTL).thenReturn(data));
    }

    public Mono<List<CustomerTransactionResources>> getNewCustomerTransaction(Integer limit, String rangeType) {
        return fetchNewCustomerTransaction(
                fetchNewCustomerListMono("/internal/profile/new-customer/by-rangetype", limit, rangeType)
        );
    }
}
