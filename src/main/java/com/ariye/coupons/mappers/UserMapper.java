package com.ariye.coupons.mappers;

import com.ariye.coupons.dto.UserDto;
import com.ariye.coupons.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "id", target = "id")
    User userDtoToUser(UserDto userDto);
}
