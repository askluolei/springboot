package com.luolei.template.web;

import com.luolei.template.security.support.HasPermission;
import com.luolei.template.security.support.HasRole;
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

    /**
     * 匿名不了了，在全局配置里面 /api 已经配置为认证之后才可以访问
     * @return
     */
    @GetMapping("/anonymoususer")
    public R anonymoususer() {
        return R.ok();
    }

    /**
     * 认证用户可以访问
     * @return
     */
    @GetMapping("/authentication")
    public R authentication() {
        return R.ok();
    }

    /**
     * 角色 admin 可以访问
     * @return
     */
    @HasRole({"admin"})
    @GetMapping("/role")
    public R hasRole() {
        return R.ok();
    }

    /**
     * 权限  p1 可以访问
     * @return
     */
    @HasPermission({"p1"})
    @GetMapping("/authority1")
    public R hasAuthority1() {
        return R.ok();
    }

    /**
     * 权限  p2 可以访问
     * @return
     */
    @HasPermission({"p2"})
    @GetMapping("/authority2")
    public R hasAuthority2() {
        return R.ok();
    }

    /**
     * 权限  p3 可以访问
     * @return
     */
    @HasPermission({"p3"})
    @GetMapping("/authority3")
    public R hasAuthority3() {
        return R.ok();
    }

    /**
     * 权限  p4 可以访问
     * @return
     */
    @HasPermission({"p4"})
    @GetMapping("/authority4")
    public R hasAuthority4() {
        return R.ok();
    }

    /**
     * 权限  p5 可以访问
     * @return
     */
    @HasPermission({"p5"})
    @GetMapping("/authority5")
    public R hasAuthority5() {
        return R.ok();
    }
}
