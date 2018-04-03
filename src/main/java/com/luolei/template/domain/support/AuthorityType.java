package com.luolei.template.domain.support;

/**
 * @author luolei
 * @createTime 2018-04-01 21:21
 */
public enum AuthorityType {
    /**
     * 实体权限 （表操作，增删改查）
     */
    ENTITY,

    /**
     * 业务接口权限
     */
    SERVICE,

    /**
     * 接口调用权限 （请求的url）
     */
    URL,

    // 下面的权限都是要前端配合，而且只是展示时候使用
    /**
     * 目录权限 用来控制菜单显示
     */
    MENU,

    /**
     * 页面级别的权限
     */
    PAGE,

    /**
     * 组件级别权限（例如按钮），
     */
    COMPONENT

    ;
}
