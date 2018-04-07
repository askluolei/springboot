package com.luolei.template.security.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author luolei
 * @createTime 2018-04-06 09:57
 */
@Target( {ElementType.TYPE, ElementType.METHOD} )
@Retention(RetentionPolicy.RUNTIME)
public @interface HasPermission {

    /**
     * 权限描述符
     * @return
     */
    String[] value() default {};

    /**
     * 如果权限有多个,连接时与 还是 或
     * @return
     */
    JoinType joinType() default JoinType.OR;
}
