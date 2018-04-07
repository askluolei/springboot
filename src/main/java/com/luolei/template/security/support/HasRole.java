package com.luolei.template.security.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 角色
 * @author luolei
 * @createTime 2018-04-06 09:50
 */
@Target( {ElementType.TYPE, ElementType.METHOD} )
@Retention(RetentionPolicy.RUNTIME)
public @interface HasRole {

    /**
     * 角色名
     * @return
     */
    String[] value() default {};

    /**
     * 如果角色有多个,连接时与 还是 或
     * @return
     */
    JoinType joinType() default JoinType.OR;

}
