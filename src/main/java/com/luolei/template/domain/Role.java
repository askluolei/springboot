package com.luolei.template.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author luolei
 * @createTime 2018-04-01 21:06
 */
@Data
@Table(name = "t_role")
@Entity
public class Role extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String name;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String nameCn;

    @Column(name = "c_explanation")
    private String explanation;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return !(role.getId() == null || getId() == null) && Objects.equals(getId(), role.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
