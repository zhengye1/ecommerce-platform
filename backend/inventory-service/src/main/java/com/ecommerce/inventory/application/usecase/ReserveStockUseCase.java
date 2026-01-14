package com.ecommerce.inventory.application.usecase;

import com.ecommerce.inventory.application.dto.request.ReserveStockRequest;
import com.ecommerce.inventory.application.dto.response.ReservationResponse;

/**
 * Use case for reserving stock for an order.
 * This is a key step in the order saga.
 */
public interface ReserveStockUseCase {

    /**
     * Reserve stock for order items.
     *
     * @param request the reservation request
     * @return the reservation response
     */
    ReservationResponse execute(ReserveStockRequest request);
}
