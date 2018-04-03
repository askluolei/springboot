package com.luolei.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * 公共repository
 * 这里可以定义一些通用方法接口
 * 使用 JPA 方法的时候
 * 查询方法统一以
 * findOne 返回 Optional
 * findAll
 * 开头
 *
 * @author luolei
 * @createTime 2018-03-28 23:00
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}
