package com.luolei.template.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Rest api 的统一响应格式
 * {
 *     code: success,
 *     message: 成功,
 *     data: {
 *         key1: antType,
 *         key2: antType,
 *     }
 * }
 * 如果只传一个 data 进来，而且为基本类型（包括 date bigdecimal 之类，非bean），那么会用map包装
 * 如果一个data，且为bean，那就不需要包装了
 * 如果多个data，那就是map了
 *
 * @author luolei
 * @createTime 2018-03-24 12:15
 */
@Getter
@NoArgsConstructor
public class R {

    /**
     * 单数据时候的 key
     */
    private static final String SINGLE_DATA_KEY = "data";

    /**
     * 常见的响应吗常量定义在这里
     */
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final String KNOWN_BIZ_ERROR = "known_biz_error";
    public static final String ILLEGAL_ARGUMENT = "illegal_argument";
    public static final String INTERNAL_ERROR = "internal_error";
    public static final String NOT_IMPLEMENTED = "not_implemented";
    public static final String FILE_NOT_EXIST = "file_not_exist";

    public static final String LOGIN_ERROR = "login_error";
    public static final String AUTHENTICATION_ERROR = "authentication_error";
    public static final String AUTHORIZATION_ERROR = "authorization_error";
    public static final String FORCE_OFFLINE = "force_offline";



    /**
     * 响应码对应的描述存在这里
     */
    private static final Map<String, String> codes = new HashMap<>();

    /**
     * 这里初始化响应码对应的描述
     * 后面也可以从文件读取
     */
    static {
        put(SUCCESS, "成功");
        put(FAIL, "失败");
        put(KNOWN_BIZ_ERROR, "业务异常");
        put(ILLEGAL_ARGUMENT, "请求参数不合法");
        put(INTERNAL_ERROR, "未预期的系统内部异常");
        put(NOT_IMPLEMENTED, "未实现的功能");
        put(FILE_NOT_EXIST, "待下载的文件不存在");
        put(LOGIN_ERROR, "登录失败");
        put(AUTHENTICATION_ERROR, "未认证用户访问受限资源");
        put(AUTHORIZATION_ERROR, "认证用户权限不足");
        put(FORCE_OFFLINE, "被踢下线");
    }

    static void put(String key, String value) {
        codes.put(key, value);
    }

    static Class<?>[] basicTypes = {
            byte.class, char.class, short.class, int.class, float.class, double.class, long.class,
            Byte.class, Character.class, Short.class, Integer.class, Float.class, Double.class, Long.class,
            byte[].class, char[].class, short[].class, int[].class, float[].class, double[].class, long[].class,
            String.class, Date.class, LocalDate.class, LocalTime.class, LocalDateTime.class, Instant.class,
            BigDecimal.class
    };

    /**
     * 上面的类型
     * 如果是，就用 map 包一层
     * 保证 最终返回的是 {}
     * @param clazz
     * @return
     */
    static boolean isBasicType(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        for (Class<?> t : basicTypes) {
            if (t == clazz) {
                return true;
            }
        }
        return false;
    }

    // ------- 成功响应的快捷创建方式 响应码为 success
    public static R ok() {
        return new R(SUCCESS, codes.get(SUCCESS), null);
    }

    public static R ok(Object data) {
        return new R(SUCCESS, codes.get(SUCCESS), data);
    }

    public static R ok(String message, Object data) {
        return new R(SUCCESS, message, data);
    }

    // ------- 失败响应的快捷创建方式 响应码为 自定义的
    public static R error(String code) {
        return new R(code, codes.get(code), null);
    }

    public static R error(String code, Object data) {
        return new R(code, codes.get(code), data);
    }

    public static R error(String code, String message, Object data) {
        return new R(code, message, data);
    }

    // ---------- 快速失败的快捷创建方式 这里的响应码是 fail

    public static R fail() {
        return new R(FAIL, codes.get(FAIL), null);
    }

    public static R fail(Object data) {
        return new R(FAIL, codes.get(FAIL), data);
    }

    public static R fail(String message, Object data) {
        return new R(FAIL, message, data);
    }

    /**
     * 响应码
     * 不要使用 http 的状态码
     * 可以使用简练的英文
     */
    private String code;
    /**
     * 附带的描述信息，或者如果返回少量的数据可以在这里
     */
    private String message;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 响应数据
     * 这是一个临时字段
     * 如果返回的业务对象只有一个，直接用 data
     * 如果多个，用map，最后赋值给data
     */
    @JsonIgnore
    private Map<String, Object> map;

    public R(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        data(data);
    }

    public R code(String code) {
        this.code = code;
        if (codes.containsKey(code)) {
            this.message = codes.get(code);
        }
        return this;
    }

    public R message(String message) {
        this.message = message;
        return this;
    }

    private void analysisData() {
        if (this.map.size() > 1) {
            this.data = map;
        } else if (this.map.size() == 1) {
            Object inData = this.map.entrySet().iterator().next().getValue();
            if (isBasicType(inData.getClass())) {
                this.data = map;
            } else {
                this.data = inData;
            }
        } else {
            this.data = null;
        }
    }

    public R data(String key, Object data) {
        if (Objects.isNull(this.map)) {
            this.map = new HashMap<>(5);
        }
        if (Objects.nonNull(data)) {
            this.map.put(key, data);
        }
        analysisData();
        return this;
    }

    public R data(Object data) {
        return data(SINGLE_DATA_KEY, data);
    }

    public R data(Map<String, Object> data) {
        if (Objects.isNull(this.map)) {
            this.map = new HashMap<>(5);
        }
        this.map.putAll(data);
        analysisData();
        return this;
    }
}
