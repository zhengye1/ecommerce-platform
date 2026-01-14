package com.ecommerce.notification.infrastructure.persistence;

import com.ecommerce.notification.domain.model.Notification;
import com.ecommerce.notification.infrastructure.persistence.entity.NotificationJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationEntityMapper {

    NotificationJpaEntity toEntity(Notification domain);

    Notification toDomain(NotificationJpaEntity entity);
}
