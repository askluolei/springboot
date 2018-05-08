package com.luolei.template.module.dynamic.domain;

import com.luolei.template.domain.AbstractAuditingEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 15:02
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "t_meta_unique_index")
public class MetaUniqueIndex extends AbstractAuditingEntity {

    /**
     * 对象
     */
    private MetaObject object;

    /**
     * 租户
     */
    private MetaTenant tenant;

    /**
     * 字段在哪列
     */
    private Integer fieldNum;

    /**
     * 数据id
     */
    private Long dataId;

    /**
     * 下面是各种实际类型的 value
     * 唯一索引 一般不可能是复杂对象
     */

    private String stringValue;

    private Integer integerValue;

    private Long longValue;
}
