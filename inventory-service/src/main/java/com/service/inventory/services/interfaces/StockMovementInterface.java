package com.service.inventory.services.interfaces;

import java.util.List;

import com.service.inventory.entities.StockMovement;
import com.service.inventory.requests.PaginationRequest;
import com.service.inventory.requests.StockMovementRequest;
import com.service.inventory.resources.StockMovementResource;
import com.service.inventory.resources.StockMvmInProdResource;
import com.service.inventory.resources.StockMvmOutProdResource;
import com.service.inventory.responses.PaginationResponse;

public interface StockMovementInterface {

    List<StockMovement> createStockMovements(List<StockMovementRequest> requests);

    StockMovement updateStockMovement(StockMovementRequest request);

    PaginationResponse<StockMovementResource> getPaginationMovementTypeIN(PaginationRequest request);

    PaginationResponse<StockMovementResource> getPaginationMovementTypeOUT(PaginationRequest request);

    PaginationResponse<StockMvmInProdResource> getStockMovementTypeIN_Product(PaginationRequest request);

    PaginationResponse<StockMvmOutProdResource> getStockMovementTypeOUT_Product(PaginationRequest request);
}
