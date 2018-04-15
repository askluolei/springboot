package com.luolei.template.support;

import cn.hutool.core.util.StrUtil;
import com.luolei.template.error.BaseException;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 罗雷
 * @date 2018/4/4 0004
 * @time 16:04
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class GlobalErrorController extends AbstractErrorController{

    private final ErrorProperties errorProperties;
    private final ErrorAttributes errorAttributes;
    public GlobalErrorController(ErrorAttributes errorAttributes,
                           ServerProperties serverProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorViewResolvers);
        Assert.notNull(serverProperties.getError(), "ErrorProperties must not be null");
        this.errorProperties = serverProperties.getError();
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping
    @ResponseBody
    public R error(HttpServletRequest request) {
        WebRequest webRequest = new ServletWebRequest(request);
        Throwable throwable = this.errorAttributes.getError(webRequest);
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        if (Objects.nonNull(throwable) && Objects.equals(throwable.getClass(), BaseException.class)) {
            BaseException baseException = (BaseException) throwable;
            if (StrUtil.isNotBlank(baseException.getCode())) {
                return R.error(baseException.getCode()).data(body);
            }
        }
        return R.fail().data(body);
    }

    protected boolean isIncludeStackTrace(HttpServletRequest request,
                                          MediaType produces) {
        ErrorProperties.IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    protected ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }

    @Override
    public String getErrorPath() {
        return this.errorProperties.getPath();
    }
}
