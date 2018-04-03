package com.luolei.template.service.mapper;

import com.luolei.template.domain.Token;
import com.luolei.template.web.dto.TokenDto;
import org.mapstruct.Mapper;

/**
 * @author luolei
 * @createTime 2018-04-04 00:00
 */
@Mapper(componentModel =  "spring")
public interface TokenMapper {

    TokenDto fromToken(Token token);

    Token toToken(TokenDto tokenDto);
}
