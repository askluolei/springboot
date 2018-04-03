package com.luolei.template.repository;

import com.luolei.template.domain.Token;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * token 仓库
 * @author 罗雷
 * @date 2018/4/3 0003
 * @time 14:42
 */
public interface TokenRepository extends BaseRepository<Token, Long> {

    /**
     * accessToken 查询
     * @param accessToken
     * @return
     */
    Optional<Token> findOneByAccessToken(String accessToken);

    /**
     * refreshToken 查询
     * @param refreshToken
     * @return
     */
    Optional<Token> findOneByRefreshToken(String refreshToken);

    /**
     * random 查询
     * @param random
     * @return
     */
    List<Token> findByRandom(Long random);

    /**
     * expireTime 过期时间查询
     * @param expireTime
     * @return
     */
    List<Token> findAllByExpireTimeBefore(Instant expireTime);

    /**
     * 用户id查询
     * @param username
     * @return
     */
    List<Token> findAllByUsername(Long username);
}
