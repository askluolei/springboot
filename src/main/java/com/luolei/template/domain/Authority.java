package com.luolei.template.domain;

import com.luolei.template.domain.support.AuthorityType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 权限
 * 要注意的是，权限在应用启动的时候就是固定的，不能添加，删除
 * 因为，添加一个内部压根就没定义的权限啥用没有，要是删除权限了，那有权限定义的地方就再也木有权限可以访问了，
 * 修改也只能修改描述啥的
 *
 * @author luolei
 * @createTime 2018-04-01 21:22
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "jhi_authority")
public class Authority extends AbstractAuditingEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthorityType type = AuthorityType.URL;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String authority;

    @Column(length = 50, nullable = false)
    private String authorityCn;

    @Column(name = "c_explanation")
    private String explanation;

}
