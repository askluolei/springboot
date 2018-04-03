package com.luolei.template.domain;

import com.luolei.template.domain.support.AuthType;
import com.luolei.template.domain.support.RequestPlatform;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

/**
 * token信息
 *
 * @author 罗雷
 * @date 2018/4/2 0002
 * @time 14:25
 */
@Getter
@Setter
@Entity
@Table(name = "t_token")
public class Token extends AbstractAuditingEntity {

    /**
     * accessToken
     * 过期时间较短 （半个小时）
     * 用户认证成功后返回
     * 以后的每次请求头中要带上这个token
     */
    @Column(name = "access_token", length = 500, nullable = false, unique = true)
    private String accessToken;

    /**
     * refreshToken 相当于 rememberMe
     * 过期时间较长 7天或者更长
     * 在前端判断出accessToken快过期的时候(过期也无所谓，只要refreshToken没过期就行)
     * 使用这个token 调用刷新 accessToken
     */
    @Column(name = "refresh_token", length = 500, unique = true)
    private String refreshToken;

    /**
     * 随机数
     * 每个token内都生成一个随机数
     */
    @Column(name = "token_random", unique = true)
    private Long random;

    /**
     * 过期时间
     * 不是必须的，token中有过期时间信息
     */
    @Column(name = "expire_time")
    private Instant expireTime;

    /**
     * sessionID
     * 绑定用户登录相关信息的
     * 暂时不启用
     */
    @Column(name = "session_id")
    private String sessionID;

    /**
     * 登录方式
     * 目前只有 用户名密码模式 userPass
     */
    @Column(name = "auth_type")
    @Enumerated(EnumType.STRING)
    private AuthType authType;

    /**
     * 哪个平台登录的
     * 目前只有 PC
     */
    @Column(name = "platform")
    @Enumerated(EnumType.STRING)
    private RequestPlatform platform;

    /**
     * 哪个ip发起的登录请求
     * 不一定是ip，只要可以标识一个平台即可
     * 最好不要自定义参数,能够用http头中的信息最好
     */
    @Column(name = "login_ip")
    private String loginIP;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;
}
