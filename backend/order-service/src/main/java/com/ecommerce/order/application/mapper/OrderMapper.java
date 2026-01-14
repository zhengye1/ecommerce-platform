package com.ecommerce.order.application.mapper;

import com.ecommerce.order.application.dto.request.AddressRequest;
import com.ecommerce.order.application.dto.response.AddressResponse;
import com.ecommerce.order.application.dto.response.OrderItemResponse;
import com.ecommerce.order.application.dto.response.OrderResponse;
import com.ecommerce.order.domain.model.Address;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for Order entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> orders);

    OrderItemResponse toItemResponse(OrderItem item);

    AddressResponse toAddressResponse(Address address);

    Address toAddress(AddressRequest request);
}
