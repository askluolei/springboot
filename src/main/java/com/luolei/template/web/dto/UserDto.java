package com.luolei.template.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.luolei.template.support.Constants;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;

/**
 * 数据传输对象，这里只有前端可能传的字段，当如，如果没有 VO 对象，那么可以加上响应字段，
 * 可以在这里加上校验注解
 * 可以进行分组
 *
 * @author luolei
 * @createTime 2018-04-02 23:14
 */
@Data
public class UserDto {

    private Long id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 4, max = 60)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    @NotNull
    private boolean activated = false;

    @Size(min = 2, max = 6)
    private String langKey;

    @Size(max = 256)
    private String imageUrl;

    private String resetKey;

    private Instant resetDate = null;
}
