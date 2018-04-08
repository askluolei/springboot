package com.luolei.template.web;

import com.luolei.template.error.BaseException;
import com.luolei.template.support.R;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 用来测试的 controller
 *
 * @author luolei
 * @createTime 2018-03-24 12:20
 */
@RestController
@RequestMapping(path = {"/test"}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@Timed(value = "helloController")
public class HelloController {

    /**
     * 用来测试服务是否正常
     * @return
     */
    @GetMapping
    public R index() {
        return R.ok("hello, world");
    }

    /**
     * java8 的日期 api 是否正常序列化
     * @return
     */
    @GetMapping("/date")
    public R localDate() {
        return R.ok(LocalDate.now());
    }

    /**
     * java8 的日期 api 是否正常序列化
     * @return
     */
    @GetMapping("/time")
    public R localTime() {
        return R.ok(LocalTime.now());
    }

    /**
     * java8 的日期 api 是否正常序列化
     * @return
     */
    @GetMapping("/datetime")
    public R localDateTime() {
        return R.ok(LocalDateTime.now());
    }

    /**
     * java8 的日期 api 是否正常序列化
     * @return
     */
    @GetMapping("/instant")
    public R instant() {
        return R.ok(Instant.now());
    }

    /**
     * java 的 long 型范围比 js 的范围大，在前端直接接收long 响应可能会丢失精度， 因此在序列化的时候可能需要序列化成字符串
     * @return
     */
    @GetMapping("/long")
    public R longValue() {
        return R.ok(Long.MAX_VALUE);
    }

    /**
     * 不合法参数异常测试
     * @return
     */
    @GetMapping("/illegal-argument")
    public R illegalArgument() {
        throw new IllegalArgumentException("不合法参数异常测试");
    }

    /**
     * 业务基础异常测试
     * @return
     */
    @GetMapping("/base-exception")
    public R baseException() {
        throw new BaseException("业务基础异常测试");
    }

    /**
     * 运行期异常测试，模拟代码中未捕获到的异常
     * @return
     */
    @GetMapping("/runtime-exception")
    public R runtimeException() {
        throw new RuntimeException("运行期异常测试");
    }
}
