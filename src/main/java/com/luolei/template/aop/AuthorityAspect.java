package com.luolei.template.aop;

import com.luolei.template.error.BizException;
import com.luolei.template.security.SecurityUtils;
import com.luolei.template.security.support.HasPermission;
import com.luolei.template.security.support.HasRole;
import com.luolei.template.security.support.JoinType;
import com.luolei.template.support.Constants;
import com.luolei.template.support.R;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
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
     * 在方法上
     */
    @Pointcut("@annotation(com.luolei.template.security.support.HasRole)")
    public void hasRoleOnMethod() {}

    /**
     * 角色注解
     * 在类上
     */
    @Pointcut("@within(com.luolei.template.security.support.HasRole)")
    public void hasRoleOnType() {}

    /**
     * 权限注解
     * 在方法上
     */
    @Pointcut("@annotation(com.luolei.template.security.support.HasPermission)")
    public void hasPermissionOnMethod() {}

    /**
     * 权限注解
     * 在类上
     */
    @Pointcut("@within(com.luolei.template.security.support.HasPermission)")
    public void hasPermissionOnType() {}

    /**
     * 实体资源
     */
    @Pointcut("@annotation(com.luolei.template.security.support.EntityResource)")
    public void entityResource() {}

    /**
     * 实体资源权限
     */
    @Before("applicationPackagePointcut() && withinAnnotationRepository() && entityResource()")
    public void preEntityResourceCheck(JoinPoint joinPoint) {
        log.info("&(&()U&)*_ 测试");
    }

    /**
     * 前置角色检查
     */
    @Before("applicationPackagePointcut() && springBeanPointcut() && hasRoleOnMethod() && @annotation(role)")
    public void preRoleCheckOnMethod(HasRole role) {
        preRoleCheckCommon(role);
    }

    @Before("applicationPackagePointcut() && springBeanPointcut() && hasRoleOnType() && @within(role)")
    public void preRoleCheckOnType(HasRole role) {
        preRoleCheckCommon(role);
    }

    /**
     * 前置权限检查
     */
    @Before("applicationPackagePointcut() && springBeanPointcut() && hasPermissionOnMethod() && @annotation(permission)")
    public void prePermissionCheckOnMethod(HasPermission permission) {
        prePermissionCheckCommon(permission);
    }

    @Before("applicationPackagePointcut() && springBeanPointcut() && hasPermissionOnType() && @within(permission)")
    public void prePermissionCheckOnType(HasPermission permission) {
        prePermissionCheckCommon(permission);
    }

    private void prePermissionCheckCommon(HasPermission permission) {
        if (Objects.isNull(permission)) {
            throw new BizException("应用 权限 AOP 实现异常");
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
            throw new BizException("权限不足, 需要权限之一:" + Arrays.toString(value)).withCode(R.AUTHORIZATION_ERROR);
        } else {
            for (String permissionValue : value) {
                boolean hasPermission = SecurityUtils.isCurrentUserInAuthority(permissionValue);
                if (!hasPermission) {
                    throw new BizException("权限不足, 需要权限:" + Arrays.toString(value)).withCode(R.AUTHORIZATION_ERROR);
                }
            }
        }
    }

    private void preRoleCheckCommon(HasRole role) {
        if (Objects.isNull(role)) {
            throw new BizException("应用 角色 AOP 实现异常");
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
            throw new BizException("权限不足, 需要角色之一:" + Arrays.toString(value)).withCode(R.AUTHORIZATION_ERROR);
        } else {
            for (String roleValue : value) {
                boolean hasRole = SecurityUtils.isCurrentUserInAuthority(Constants.ROLE_PREFIX + roleValue);
                if (!hasRole) {
                    throw new BizException("权限不足, 需要角色:" + Arrays.toString(value)).withCode(R.AUTHORIZATION_ERROR);
                }
            }
        }
    }
}
