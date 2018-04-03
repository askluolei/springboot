package com.luolei.template.repository;

import com.luolei.template.domain.Role;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

/**
 * @author luolei
 * @createTime 2018-04-02 21:16
 */
public interface RoleRepository extends BaseRepository<Role, Long> {

    /**
     * 根据角色名查询
     * @param name
     * @return
     */
    Optional<Role> findOneByName(String name);

    /**
     * 同上，就是不使用延时加载，直接连接查询（外联）
     * 如果使用延时加载，那就是 n 次 id 查询了
     * 具体的可以看测试中打印日志
     * @param name
     * @return
     */
    @EntityGraph(attributePaths = {"authorities"})
    Optional<Role> findOneWithAuthoritiesByName(String name);

    /**
     * 给findOne 也加上个级联查询
     * @param id
     * @return
     */
    @EntityGraph(attributePaths = {"authorities"})
    Optional<Role> findOneWithAuthoritiesById(Long id);
}
