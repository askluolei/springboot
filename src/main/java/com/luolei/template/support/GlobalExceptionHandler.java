package com.luolei.template.support;

import cn.hutool.core.util.StrUtil;
import com.luolei.template.error.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import static com.luolei.template.support.R.*;

/**
 * 全局异常处理
 * 这里能够捕捉到的异常 只有请求进入到 拦截器后（包括controller）抛出的
 * 如果是再 spring 框架层面就出现异常了(404 之类的)，就直接走spring的异常处理了
 * 解决办法是重写 spring 的 ErrorController 或者自己定义 /error 的处理
 * 将返回的数据 改造成符合系统返回的格式
 *
 * @author luolei
 * @createTime 2018-03-24 12:17
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(BizException.class)
    @ResponseBody
    public R handleEBException(BizException e, WebRequest request) {
        log.error("系统内部异常", e);
        String code = e.getCode();
        if (isDebug(request)) {
            return R.error(StrUtil.isEmpty(code) ? KNOWN_BIZ_ERROR : code, e);
        } else {
            return R.error(StrUtil.isEmpty(code) ? KNOWN_BIZ_ERROR : code, e.getMessage());
        }
    }

    /**
     * 参数不合法异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public R handleIllegalArgument(IllegalArgumentException e, WebRequest request) {
        log.error("参数不合法", e);
        if (isDebug(request)) {
            return R.error(ILLEGAL_ARGUMENT, e);
        } else {
            return R.error(ILLEGAL_ARGUMENT, e.getMessage());
        }
    }

    /**
     * 使用 javax.validation 校验注解，校验失败抛出的异常
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public R handleIMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
        log.error("请求参数校验异常", e);
        if (isDebug(request)) {
            return R.error(ILLEGAL_ARGUMENT, e);
        } else {
            return R.error(ILLEGAL_ARGUMENT, e.getMessage());
        }
    }

    /**
     * 其他未预期的异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R handleAll(Exception e, WebRequest request) {
        log.error("未知异常", e);
        if (isDebug(request)) {
            return R.error(INTERNAL_ERROR, e);
        } else {
            return R.error(INTERNAL_ERROR, e.getMessage());
        }
    }

    /**
     * 用户认证失败
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public R handleAll(BadCredentialsException e, WebRequest request) {
        log.error("用户认证失败", e);
        if (isDebug(request)) {
            return R.error(LOGIN_ERROR, e);
        } else {
            return R.error(LOGIN_ERROR, e.getMessage());
        }
    }

    /**
     * spring-security 的认证授权异常
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public R handleAll(AuthenticationException e, WebRequest request) {
        log.error("未认证用户访问受限资源", e);
        if (isDebug(request)) {
            return R.error(AUTHENTICATION_ERROR, e);
        } else {
            return R.error(AUTHENTICATION_ERROR, e.getMessage());
        }
    }

    private boolean isDebug(WebRequest request) {
        return Boolean.parseBoolean(request.getHeader("debug")) || Boolean.parseBoolean(request.getParameter("debug"));
    }
}
