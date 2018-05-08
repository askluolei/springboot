package com.luolei.template.module.dynamic.domain;

import com.luolei.template.domain.AbstractAuditingEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 9:07
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "t_meta_object")
public class MetaObject extends AbstractAuditingEntity {

    /**
     * 租户id
     */
    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private MetaTenant tenant;

    /**
     * 对象名
     */
    @Column(name = "object_name", nullable = false, length = 32)
    private String objectName;

    /**
     * 对象自然名
     */
    @Column(name = "object_natural_name", length = 128)
    private String objectNaturalName;
}
