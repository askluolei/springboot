package com.luolei.template.web;

import com.luolei.template.security.support.HasRole;
import com.luolei.template.support.R;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luolei
 * @createTime 2018-04-07 17:26
 */
@HasRole({"user"})
@RestController
@RequestMapping(path = {"/api/security2"}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class SecurityController2 {

    @GetMapping
    public R index() {
        return R.ok();
    }
}
