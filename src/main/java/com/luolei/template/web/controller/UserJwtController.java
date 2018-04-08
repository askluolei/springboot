package com.luolei.template.web.controller;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Preconditions;
import com.luolei.template.domain.support.RequestPlatform;
import com.luolei.template.error.BaseException;
import com.luolei.template.security.SecurityUtils;
import com.luolei.template.service.JwtService;
import com.luolei.template.support.R;
import com.luolei.template.support.annotation.RequestIP;
import com.luolei.template.web.controller.vm.LoginVM;
import com.luolei.template.web.dto.TokenDto;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 认证相关
 *
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
     * 使用refreshToken 换取凭证
     * @param platform
     * @param ip
     * @param loginVM
     * @return
     */
    @PostMapping("/refreshToken")
    public R refreshToken(RequestPlatform platform, @RequestIP String ip, @RequestBody LoginVM loginVM) {
        Preconditions.checkArgument(StrUtil.isNotBlank(loginVM.getRefreshToken()), "请求参数不合法");
        return R.ok(jwtService.authorize(loginVM.getRefreshToken(), platform, ip));
    }

    /**
     * 用户认证
     * 返回accessToken,
     * 如果rememberMe，则还有 refreshToken
     * @param loginVM
     * @return
     */
    @PostMapping("/authenticate")
    public R authorize(RequestPlatform platform, @RequestIP String ip, @Valid @RequestBody LoginVM loginVM) {
        boolean rememberMe = (loginVM.getRememberMe() == null) ? false : loginVM.getRememberMe();
        return R.ok(jwtService.authorize(loginVM.getUsername(), loginVM.getPassword(), rememberMe, platform, ip));
    }

    /**
     * 踢出本帐号的其他凭证
     * @return
     */
    @PutMapping("/authenticate")
    public R authorize(@RequestBody TokenDto tokenDto) {
        Preconditions.checkArgument(StrUtil.isNotBlank(tokenDto.getOfflineToken()), "踢人凭证不能为空");
        String accessToken = SecurityUtils.getCurrentUserJWT().orElseThrow(() -> new BaseException("获取凭证失败"));
        Preconditions.checkState(StrUtil.isNotBlank(accessToken), "请求凭证不存在");
        return R.ok(jwtService.offline(accessToken, tokenDto.getOfflineToken()));
    }
}
