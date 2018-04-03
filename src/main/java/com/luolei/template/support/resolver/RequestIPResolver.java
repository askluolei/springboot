package com.luolei.template.support.resolver;

import com.luolei.template.support.annotation.RequestIP;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求的 IP
 *
 * @author 罗雷
 * @date 2018/4/2 0002
 * @time 14:38
 */
public class RequestIPResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(String.class) && parameter.hasParameterAnnotation(RequestIP.class);
    }

    @Nullable
    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        return webRequest.getNativeRequest(HttpServletRequest.class).getRemoteAddr();
    }
}
