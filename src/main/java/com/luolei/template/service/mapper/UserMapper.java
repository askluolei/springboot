package com.luolei.template.service.mapper;

import com.luolei.template.domain.User;
import com.luolei.template.web.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author luolei
 * @createTime 2018-04-02 23:41
 */
@Mapper(componentModel =  "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto fromUser(User user);

    User toUser(UserDto userDto);
}
