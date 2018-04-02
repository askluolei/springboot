package com.luolei.template.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author luolei
 * @createTime 2018-04-01 21:22
 */
@Entity
@Table(name = "jhi_authority")
@Data
public class Authority extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthorityType type = AuthorityType.URL;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String authority;

    @NotBlank
    @Column(length = 50, nullable = false)
    private String authorityCn;

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
        Authority authority = (Authority) o;
        return !(authority.getId() == null || getId() == null) && Objects.equals(getId(), authority.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
