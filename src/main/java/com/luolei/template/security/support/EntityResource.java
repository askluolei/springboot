package com.luolei.template.security.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体资源
 * @author luolei
 * @createTime 2018-04-07 15:11
 */
@Target( {ElementType.TYPE} )
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityResource {
}
