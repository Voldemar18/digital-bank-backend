package com.digitalbank.user.mapper;

import com.digitalbank.user.model.dto.CreateUserRq;
import com.digitalbank.user.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(CreateUserRq createUserRq);
}