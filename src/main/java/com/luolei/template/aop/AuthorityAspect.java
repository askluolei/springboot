package com.luolei.template.aop;

import com.luolei.template.error.AuthorizationException;
import com.luolei.template.error.BaseException;
import com.luolei.template.security.SecurityUtils;
import com.luolei.template.security.support.HasPermission;
import com.luolei.template.security.support.HasRole;
import com.luolei.template.security.support.JoinType;
import com.luolei.template.support.Constants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;
import java.util.Objects;

/**
 * 权限切面
 *
 * @author luolei
 * @createTime 2018-04-06 09:49
 */
@Slf4j
@Aspect
public class AuthorityAspect implements AspectPointcuts {

    /**
     * 角色注解
     */
    @Pointcut("@annotation(com.luolei.template.security.support.HasRole)")
    public void hasRole() {}

    /**
     * 权限注解
     */
    @Pointcut("@annotation(com.luolei.template.security.support.HasPermission)")
    public void hasPermission() {}

    /**
     * 前置角色检查
     */
    @Before("applicationPackagePointcut() && springBeanPointcut() && hasRole() && @annotation(role)")
    public void preRoleCheck(HasRole role) {
        if (Objects.isNull(role)) {
            throw new BaseException("应用 角色 AOP 实现异常");
        }
        String[] value = role.value();
        if (Objects.isNull(value) || value.length == 0) {
            return;
        }
        JoinType joinType = role.joinType();
        if (Objects.isNull(joinType)) {
            joinType = JoinType.OR;
        }
        if (joinType == JoinType.OR) {
            for (String roleValue : value) {
                boolean hasRole = SecurityUtils.isCurrentUserInAuthority(Constants.ROLE_PREFIX + roleValue);
                if (hasRole) {
                    return;
                }
            }
            throw new AuthorizationException("权限不足, 需要角色之一:" + Arrays.toString(value));
        } else {
            for (String roleValue : value) {
                boolean hasRole = SecurityUtils.isCurrentUserInAuthority(Constants.ROLE_PREFIX + roleValue);
                if (!hasRole) {
                    throw new AuthorizationException("权限不足, 需要角色:" + Arrays.toString(value));
                }
            }
        }
    }

    /**
     * 前置权限检查
     */
    @Before("applicationPackagePointcut() && springBeanPointcut() && hasPermission() && @annotation(permission)")
    public void prePermissionCheck(HasPermission permission) {
        if (Objects.isNull(permission)) {
            throw new BaseException("应用 权限 AOP 实现异常");
        }
        String[] value = permission.value();
        JoinType joinType = permission.joinType();
        if (Objects.isNull(joinType)) {
            joinType = JoinType.OR;
        }
        if (joinType == JoinType.OR) {
            for (String permissionValue : value) {
                boolean hasPermission = SecurityUtils.isCurrentUserInAuthority(permissionValue);
                if (hasPermission) {
                    return;
                }
            }
            throw new AuthorizationException("权限不足, 需要权限之一:" + Arrays.toString(value));
        } else {
            for (String permissionValue : value) {
                boolean hasPermission = SecurityUtils.isCurrentUserInAuthority(permissionValue);
                if (!hasPermission) {
                    throw new AuthorizationException("权限不足, 需要权限:" + Arrays.toString(value));
                }
            }
        }
    }
}
