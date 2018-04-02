package com.luolei.template.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * 借助 java8 接口方法的默认实现，
 * 这里定义好可能会用到的 pointcut
 *
 * @author luolei
 * @createTime 2018-03-24 12:22
 */
public interface AspectPointcuts {

    /**
     * 横切点 匹配所有标记 @Repository @Service @RestController 注解的类
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" +
            " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.web.bind.annotation.RestController *)")
    default void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * FIXME 只有这里才有可能修改，注意项目的包名
     * 横切点 匹配所有在项目包下（repository, service, web.rest）的 spring beans
     */
    @Pointcut("within(com.luolei.template..*)")
    default void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * 任意public 方法
     */
    @Pointcut("execution(public * *(..))")
    default void anyPublic() {
    }

    /**
     * 目标对象上有 @RestController 注解的类
     */
    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    default void withinAnnotationRestController() {
    }

    /**
     * 目标对象上有 @Controller 注解的类
     */
    @Pointcut("@within(org.springframework.stereotype.Controller)")
    default void withinAnnotationController() {
    }

    /**
     * 目标对象上有 @Service 注解的类
     */
    @Pointcut("@within(org.springframework.stereotype.Service)")
    default void withinAnnotationService() {
    }

    /**
     * 目标对象上有 @Repository 注解的类
     */
    @Pointcut("@within(org.springframework.stereotype.Repository)")
    default void withinAnnotationRepository() {
    }

    /**
     * 目标对象上有 @Component 注解的类
     */
    @Pointcut("@within(org.springframework.stereotype.Component)")
    default void withinAnnotationComponent() {
    }

    /**
     * 方法上有 @GetMapping 注解
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    default void annotationGetMappingMethod() {
    }

    /**
     * 方法上有 @PostMapping 注解
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    default void annotationPostMappingMethod() {
    }

    /**
     * 方法上有 @PutMapping 注解
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    default void annotationPutMappingMethod() {
    }

    /**
     * 方法上有 @DeleteMapping 注解
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    default void annotationDeleteMappingMethod() {
    }

    /**
     * 方法上有 @PatchMapping 注解
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    default void annotationPatchMappingMethod() {
    }
}
