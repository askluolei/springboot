package com.luolei.template.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
 * 如果只传一个 data 进来，那么data 里面的key 还是data
 *
 * @author luolei
 * @createTime 2018-03-24 12:15
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
        put(AUTHENTICATION_ERROR, "认证异常");
        put(AUTHORIZATION_ERROR, "授权异常");
        put(FORCE_OFFLINE, "被踢下线");
    }

    static void put(String key, String value) {
        codes.put(key, value);
    }

    static HashMap<String, Object> toMap(Object data) {
        HashMap<String, Object> map = new HashMap<>(5);
        map.put(SINGLE_DATA_KEY, data);
        return map;
    }

    // ------- 成功响应的快捷创建方式 响应码为 success
    public static R ok() {
        return new R(SUCCESS, codes.get(SUCCESS), null);
    }

    public static R ok(Object data) {
        return new R(SUCCESS, codes.get(SUCCESS), toMap(data));
    }

    public static R ok(String message, Object data) {
        return new R(SUCCESS, message, toMap(data));
    }

    // ------- 失败响应的快捷创建方式 响应码为 自定义的
    public static R error(String code) {
        return new R(code, codes.get(code), null);
    }

    public static R error(String code, Object data) {
        return new R(code, codes.get(code), toMap(data));
    }

    public static R error(String code, String message, Object data) {
        return new R(code, message, toMap(data));
    }

    // ---------- 快速失败的快捷创建方式 这里的响应码是 fail

    public static R fail() {
        return new R(FAIL, codes.get(FAIL), null);
    }

    public static R fail(Object data) {
        return new R(FAIL, codes.get(FAIL), toMap(data));
    }

    public static R fail(String message, Object data) {
        return new R(FAIL, message, toMap(data));
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
    private Map<String, Object> data;

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

    public R data(String key, Object data) {
        if (Objects.isNull(this.data)) {
            this.data = new HashMap<>(5);
        }
        this.data.put(key, data);
        return this;
    }

    public R data(Object data) {
        if (Objects.isNull(this.data)) {
            this.data = new HashMap<>(5);
        }
        this.data.put(SINGLE_DATA_KEY, data);
        return this;
    }

    public R data(Map<String, Object> data) {
        if (Objects.isNull(this.data)) {
            this.data = new HashMap<>(5);
        }
        this.data.putAll(data);
        return this;
    }
}
