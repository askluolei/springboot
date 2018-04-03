package com.luolei.template.web.dto;

import lombok.Data;

import java.time.Instant;

/**
 * @author luolei
 * @createTime 2018-04-03 23:42
 */
@Data
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private Instant expireTime;
}
