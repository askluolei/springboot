package com.luolei.template.module.dynamic.repository;

import com.luolei.template.module.dynamic.domain.MetaTenant;
import com.luolei.template.repository.BaseRepository;

import java.util.Optional;

/**
 * @author 罗雷
 * @date 2018/5/8 0008
 * @time 16:07
 */
public interface MetaTenantRepository extends BaseRepository<MetaTenant, Long> {

    Optional<MetaTenant> findOneByName(String name);
}
