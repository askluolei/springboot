package com.luolei.template.module.dynamic.domain;

import com.luolei.template.domain.AbstractAuditingEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 15:04
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "t_meta_relation_ship")
public class MetaRelationShip extends AbstractAuditingEntity {

    /**
     * 对象
     */
    private MetaObject object;

    /**
     * 租户
     */
    private MetaTenant tenant;

    /**
     * 数据id
     */
    private Long dataId;

    /**
     * 这个是什么还得思考一下
     */
    @Enumerated(EnumType.STRING)
    private RelationType relationType;

    /**
     * 目标对象id
     */
    private Long targetObjId;
}
