package com.luolei.template.repository;

import com.luolei.template.domain.User;
import com.luolei.template.security.support.EntityResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * 用户仓库
 * @author luolei
 * @createTime 2018-03-28 22:22
 */
@EntityResource
public interface UserRepository extends BaseRepository<User, Long> {

    /**
     * 根据用户名查询
     * @param username
     * @return
     */
    Optional<User> findOneByUsername(String username);

    /**
     * 同时，级联查询角色
     * @param username
     * @return
     */
    @EntityGraph(attributePaths = "roles")
    Optional<User> findOneWithRolesByUsername(String username);

    /**
     * 根据email 查询，忽略大小写
     * @param email
     * @return
     */
    Optional<User> findOneByEmailIgnoreCase(String email);

    /**
     * 同上，级联查询角色
     * @param email
     * @return
     */
    @EntityGraph(attributePaths = "roles")
    Optional<User> findOneWithRolesByEmailIgnoreCase(String email);

    /**
     * 根据激活key查询
     * @param activationKey
     * @return
     */
    Optional<User> findOneByActivationKey(String activationKey);

    /**
     * 查询未激活的，并且创建时间在给定参数之前的用户
     * @param dateTime
     * @return
     */
    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    /**
     * 根据重置key查询
     * @param resetKey
     * @return
     */
    Optional<User> findOneByResetKey(String resetKey);

    /**
     * 根据id查询，记录角色
     * @param id
     * @return
     */
    @EntityGraph(attributePaths = "roles")
    Optional<User> findOneWithRolesById(Long id);

    /**
     * 分页查询，用户名不是给定参数的用户
     * @param pageable
     * @param username
     * @return
     */
    Page<User> findAllByUsernameNot(Pageable pageable, String username);
}
