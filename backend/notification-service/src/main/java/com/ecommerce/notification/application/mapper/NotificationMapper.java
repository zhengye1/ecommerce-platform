package com.ecommerce.notification.application.mapper;

import com.ecommerce.notification.application.dto.response.NotificationResponse;
import com.ecommerce.notification.domain.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    NotificationResponse toResponse(Notification notification);
}
