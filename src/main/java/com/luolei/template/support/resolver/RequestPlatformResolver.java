package com.luolei.template.support.resolver;

import cn.hutool.core.util.StrUtil;
import com.luolei.template.domain.support.RequestPlatform;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 请求来源
 *
 * @author 罗雷
 * @date 2018/4/2 0002
 * @time 14:07
 */
public class RequestPlatformResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(RequestPlatform.class);
    }

    @Nullable
    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        String header = webRequest.getHeader("User-Agent");
        RequestPlatform platform = RequestPlatform.UNKNOWN;
        if (StrUtil.isNotBlank(header)) {
            if(header.contains("iPhone")||header.contains("iPod")||header.contains("iPad")){
                platform = RequestPlatform.IOS;
            } else if(header.contains("Android") || header.contains("Linux")) {
                platform = RequestPlatform.ANDROID;
            } else if(header.indexOf("micromessenger") > 0){
                platform = RequestPlatform.WX;
            }else {
                platform = RequestPlatform.PC;
            }
        }
        return platform;
    }
}
