package com.luolei.template.web.controller;

import com.luolei.template.support.R;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限测试
 *
 * @author 罗雷
 * @date 2018/4/3 0003
 * @time 17:23
 */
@RestController
@RequestMapping(path = {"/api/security"}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class SecurityController {

    @GetMapping("/anonymoususer")
    public R anonymoususer() {
        return R.ok();
    }

    @GetMapping("/authentication")
    public R authentication() {
        return R.ok();
    }

    @GetMapping("/role")
    public R hasRole() {
        return R.ok();
    }

    @GetMapping("/authority")
    public R hasAuthority() {
        return R.ok();
    }
}
