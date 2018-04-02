package com.luolei.template.service.mapper;

import com.luolei.template.domain.User;
import com.luolei.template.web.dto.UserDto;
import org.mapstruct.Mapper;

/**
 * @author luolei
 * @createTime 2018-04-02 23:41
 */
@Mapper
public interface UserMapper {

    UserDto fromUser(User user);

    User toUser(UserDto userDto);
}
