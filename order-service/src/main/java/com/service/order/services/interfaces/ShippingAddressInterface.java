package com.service.order.services.interfaces;

import com.service.order.entities.ShippingAddress;
import com.service.order.requests.ShippingAddressRequest;

public interface ShippingAddressInterface {

    ShippingAddress createShippingAddress(ShippingAddressRequest request);

    ShippingAddress updateShippingAddress(ShippingAddressRequest request);

    Boolean deleteShippingAddress(Integer id);
}
