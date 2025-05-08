package com.service.payment.services.interfaces;

import com.service.payment.entities.VNPayTransaction;
import com.service.payment.requests.VNPayTransactionRequest;

public interface VNPayTransactionInterface {

    VNPayTransaction createVNPayTransaction(VNPayTransactionRequest request);
}
