package com.luolei.template.web.controller;

import com.luolei.template.domain.support.RequestPlatform;
import com.luolei.template.service.JwtService;
import com.luolei.template.support.R;
import com.luolei.template.support.annotation.RequestIP;
import com.luolei.template.web.controller.vm.LoginVM;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author luolei
 * @createTime 2018-04-03 23:06
 */
@Slf4j
@RestController
@RequestMapping(path = {"/api"}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
@Timed(value = "jwt")
public class UserJwtController {

    private final JwtService jwtService;

    public UserJwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * 用户认证
     * @param loginVM
     * @return
     */
    @PostMapping("/authenticate")
    public R authorize(RequestPlatform platform, @RequestIP String ip, @Valid @RequestBody LoginVM loginVM) {
        boolean rememberMe = (loginVM.getRememberMe() == null) ? false : loginVM.getRememberMe();
        return R.ok(jwtService.authorize(loginVM.getUsername(), loginVM.getPassword(), rememberMe, platform, ip));
    }
}
