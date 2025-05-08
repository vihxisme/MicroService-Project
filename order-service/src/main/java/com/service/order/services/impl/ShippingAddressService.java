package com.service.order.services.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.service.order.entities.ShippingAddress;
import com.service.order.mappers.ShippingAddressMapper;
import com.service.order.repositories.ShippingAddressRepository;
import com.service.order.requests.ShippingAddressRequest;
import com.service.order.services.interfaces.ShippingAddressInterface;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class ShippingAddressService implements ShippingAddressInterface {

    @Autowired
    private ShippingAddressRepository shippingAddressRepository;

    @Autowired
    private ShippingAddressMapper shippingAddressMapper;

    @RabbitListener(queues = "create-shipping:queue")
    public void createShippingAddressListener(ShippingAddressRequest request) {
        createShippingAddress(request);
    }

    @Override
    public ShippingAddress createShippingAddress(ShippingAddressRequest request) {
        ShippingAddress shippingAddress = shippingAddressMapper.toShippingAddress(request);

        return shippingAddressRepository.save(shippingAddress);
    }

    @Override
    @Transactional
    public ShippingAddress updateShippingAddress(ShippingAddressRequest request) {
        ShippingAddress existShippingAddress = shippingAddressRepository.findById(request.getId()).orElseThrow(()
                -> new EntityNotFoundException("ShippingAddress not found"));

        shippingAddressMapper.updateShippingAddressFromRequest(request, existShippingAddress);

        return shippingAddressRepository.save(existShippingAddress);
    }

    @Override
    @Transactional
    public Boolean deleteShippingAddress(Integer id) {
        ShippingAddress existShippingAddress = shippingAddressRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("ShippingAddress not found"));

        shippingAddressRepository.delete(existShippingAddress);

        return true;
    }

}
