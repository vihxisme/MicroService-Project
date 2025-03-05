package com.service.cart.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.cart.entities.CartItem;
import com.service.cart.mappers.CartItemMapper;
import com.service.cart.repositories.ApiClient;
import com.service.cart.repositories.CartItemRepository;
import com.service.cart.requests.CartItemRequest;
import com.service.cart.resources.CartItemProdResource;
import com.service.cart.resources.CartItemResource;
import com.service.cart.services.interfaces.CartItemInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CartItemService implements CartItemInterface {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Autowired
    private ApiClient apiClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    @Transactional
    public CartItem createCartItem(CartItemRequest request) {
        CartItem cartItem = cartItemMapper.toCartItem(request);

        return cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public CartItem updateCartItem(CartItemRequest request) {
        CartItem existCartItem = cartItemRepository.findById(request.getId()).orElseThrow(()
                -> new EntityNotFoundException("CartItem not found"));

        cartItemMapper.updateCartItemToRequest(request, existCartItem);

        return cartItemRepository.save(existCartItem);
    }

    @Override
    @Transactional
    public Boolean deleteCartItem(Integer id) {
        CartItem existCartItem = cartItemRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("CartItem not found"));

        cartItemRepository.delete(existCartItem);

        return true;
    }

    @Override
    public List<CartItemResource> getCartItemByUserId(UUID userId) {
        List<CartItemResource> cartItemLists = cartItemRepository.findCartItemByUserId(userId);

        if (cartItemLists == null || cartItemLists.isEmpty()) {
            throw new EntityNotFoundException("NOT FOUND");
        }

        return cartItemLists;
    }

    @Override
    public List<CartItemProdResource> getCartItemProdByUserId(String userId) {
        ResponseEntity<?> response = apiClient.getCartItemProd(userId);

        List<CartItemProdResource> cartItemProdLists = objectMapper.convertValue(response.getBody(),
                new TypeReference<List<CartItemProdResource>>() {
        });
        return cartItemProdLists;
    }

}
