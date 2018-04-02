package com.luolei.template.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.luolei.template.support.Constants;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;

/**
 * 用户
 * 数据库实体对象只需要使用数据库相关的注解，
 * 校验注解和json注解也只针对数据库实体的操作
 *
 * @author luolei
 * @createTime 2018-03-28 22:19
 */
@Data
@Entity
@Table(name = "t_user")
public class User extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(length = 50, unique = true, nullable = false)
    private String username;

    /**
     * 只用set 不用get 也就是说只反序列化，不序列化
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)

    @Column(name = "password_hash", length = 60)
    private String password;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(length = 100, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean activated = false;

    @Column(name = "lang_key", length = 6)
    private String langKey;

    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @Column(name = "activation_key", length = 20)
    @Size(max = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;

    @Column(name = "reset_date")
    private Instant resetDate = null;
}
