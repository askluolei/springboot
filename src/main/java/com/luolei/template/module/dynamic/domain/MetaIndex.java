package com.luolei.template.module.dynamic.domain;

import com.luolei.template.domain.AbstractAuditingEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 14:58
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "t_meta_index")
public class MetaIndex extends AbstractAuditingEntity {

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
     */

    private String stringValue;

    private Integer integerValue;

    private Long longValue;

    private Date dateValue;

    private BigDecimal decimalValue;

    private Boolean booleanValue;

    private LocalDate localDateValue;

    private LocalTime localTimeValue;

    private LocalDateTime localDateTimeValue;
}
