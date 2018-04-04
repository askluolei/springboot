package com.luolei.template.web.dto;

import lombok.Data;

import java.time.Instant;

/**
 * @author luolei
 * @createTime 2018-04-03 23:42
 */
@Data
public class TokenDto {

    /**
     * 请求凭证
     */
    private String accessToken;

    /**
     * rememberMe 后 兑换请求凭证的凭证
     */
    private String refreshToken;

    /**
     * 过期时间，这个只是一个预估时间，具体过期时间在 token 里面
     */
    private Instant expireTime;

    /**
     * 当前登录帐号同时在线人数（有效的accessToken数量）
     */
    private int onlineCount;

    /**
     * 一个有效期很短的临时token，凭此token可以踢出本帐号在其他地方的登录（token失效）
     */
    private String offlineToken;
}
