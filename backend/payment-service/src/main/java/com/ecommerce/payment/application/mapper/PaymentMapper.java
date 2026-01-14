package com.ecommerce.payment.application.mapper;

import com.ecommerce.payment.application.dto.response.PaymentResponse;
import com.ecommerce.payment.domain.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for Payment entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    PaymentResponse toResponse(Payment payment);

    List<PaymentResponse> toResponseList(List<Payment> payments);
}
