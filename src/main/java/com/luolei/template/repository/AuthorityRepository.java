package com.luolei.template.repository;

import com.luolei.template.domain.Authority;
import com.luolei.template.domain.support.AuthorityType;

import java.util.List;
import java.util.Optional;

/**
 * 通常权限是角色级联查询出去的
 *
 * @author luolei
 * @createTime 2018-04-02 21:16
 */
public interface AuthorityRepository extends BaseRepository<Authority, Long> {

    /**
     * 查询一个类型的全部权限
     * @param type
     * @return
     */
    List<Authority> findAllByType(AuthorityType type);

    /**
     * 根据权限字符串查询
     * @param authority
     * @return
     */
    Optional<Authority> findOneByAuthority(String authority);

}
