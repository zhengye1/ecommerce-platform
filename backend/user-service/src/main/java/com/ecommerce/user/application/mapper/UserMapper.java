package com.ecommerce.user.application.mapper;

import com.ecommerce.user.application.dto.response.UserResponse;
import com.ecommerce.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * MapStruct mapper for User entity.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);
}
