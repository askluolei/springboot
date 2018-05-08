package com.luolei.template.module.dynamic.domain;

import com.luolei.template.domain.AbstractAuditingEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 租户（用户）
 *
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 9:06
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "t_meta_tenant")
public class MetaTenant extends AbstractAuditingEntity {

    @Column(name = "name", nullable = false, unique = true, length = 16)
    private String name;
}
