package com.luolei.template.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * 角色
 * @author luolei
 * @createTime 2018-04-01 21:06
 */
@Getter
@Setter
@ToString
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

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "t_role_authority",
            joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "id")})
    private Set<Authority> authorities;

}
