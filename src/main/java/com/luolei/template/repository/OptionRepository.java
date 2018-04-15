package com.luolei.template.repository;

import com.luolei.template.domain.Option;

import java.util.Optional;

/**
 * @author luolei
 * @createTime 2018-04-14 23:25
 */
public interface OptionRepository extends BaseRepository<Option, Long> {

    Optional<Option> findOneByKey(String key);
}
