package com.luolei.template.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luolei.template.Application;
import com.luolei.template.web.controller.vm.LoginVM;
import com.luolei.template.web.dto.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author luolei
 * @createTime 2018-04-06 11:39
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class UserJwtControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void authorizeByRefreshToken() throws Exception {
        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("user");
        loginVM.setPassword("user");
        loginVM.setRememberMe(true);
        String response = mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(loginVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.data.accessToken").isString())
                .andExpect(jsonPath("$.data.data.refreshToken").isString())
                .andReturn().getResponse().getContentAsString();

        JSONObject jsonObject = JSON.parseObject(response);
        String accessToken = jsonObject.getJSONObject("data").getJSONObject("data").getString("accessToken");
        String refreshToken = jsonObject.getJSONObject("data").getJSONObject("data").getString("refreshToken");

        loginVM = new LoginVM();
        loginVM.setRefreshToken(refreshToken);
        String response2 = mockMvc.perform(post("/api/refreshToken")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(loginVM)))
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.data.accessToken").isString())
                .andExpect(jsonPath("$.data.data.refreshToken").doesNotExist())
                .andReturn().getResponse().getContentAsString();

        JSONObject jsonObject2 = JSON.parseObject(response2);
        String accessToken2 = jsonObject2.getJSONObject("data").getJSONObject("data").getString("accessToken");
        assertThat(accessToken).isEqualTo(accessToken2);
    }

    /**
     * 用户登录，没有rememberMe 标记，则不返回refreshToken
     * @throws Exception
     */
    @Test
    public void testAuthorizeWithoutRememberMe() throws Exception {
        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("user");
        loginVM.setPassword("user");
        loginVM.setRememberMe(false);
        mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(loginVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.data.accessToken").isString())
                .andExpect(jsonPath("$.data.data.refreshToken").doesNotExist());
    }

    /**
     * 用户名错误，返回认证失败
     * @throws Exception
     */
    @Test
    public void testAuthorizeUsernameError() throws Exception {
        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("user123");
        loginVM.setPassword("user");
        loginVM.setRememberMe(false);
        mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(loginVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("authentication_error"));
    }

    /**
     * 密码错误返回认证失败
     * @throws Exception
     */
    @Test
    public void testAuthorizePasswordError() throws Exception {
        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("user");
        loginVM.setPassword("user123");
        loginVM.setRememberMe(false);
        mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(loginVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("authentication_error"));
    }

    /**
     * 登录 rememberMe，返回accessToken 和 refreshToken
     * @throws Exception
     */
    @Test
    public void testAuthorizeWithRememberMe() throws Exception {
        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("user");
        loginVM.setPassword("user");
        loginVM.setRememberMe(true);
        mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(loginVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.data.accessToken").isString())
                .andExpect(jsonPath("$.data.data.refreshToken").isString());
    }

    /**
     * 重复登录
     * 做了过来，同一个帐号，同一个平台（android，ios，pd），同ip，只生成一个可用token
     * 就是说，如果在电脑(同一)上用 chrome 浏览器， ie 浏览器登录，返回的token 是同一个（token还在有效期）
     * @throws Exception
     */
    @Test
    public void testRepeatLogin() throws Exception {
        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("user");
        loginVM.setPassword("user");
        loginVM.setRememberMe(true);

        mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(loginVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.data.accessToken").isString())
                .andExpect(jsonPath("$.data.data.refreshToken").isString())
                .andExpect(jsonPath("$.data.data.onlineCount").value(1));

        mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(loginVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.data.accessToken").isString())
                .andExpect(jsonPath("$.data.data.refreshToken").isString())
                .andExpect(jsonPath("$.data.data.onlineCount").value(1));
    }

    /**
     * 同一个帐号，不同平台登录，会重新生成 token（都有效）
     * @throws Exception
     */
    @Test
    public void testRepeatLoginDifferentPlatform() throws Exception {
        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("user");
        loginVM.setPassword("user");
        loginVM.setRememberMe(true);

        mockMvc.perform(post("/api/authenticate")
                .header("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1) ")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(loginVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.data.accessToken").isString())
                .andExpect(jsonPath("$.data.data.refreshToken").isString())
                .andExpect(jsonPath("$.data.data.onlineCount").value(1));

        String response = mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("User-Agent", "Mozilla/5.0 (iPhone; U; CPU iPhone OS 2_0 like Mac OS X; en-us) AppleWebKit/525.18.1 (KHTML, like Gecko) Version/3.1.1 Mobile/5A347 Safari/525.200")
                .content(JSON.toJSONString(loginVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.data.accessToken").isString())
                .andExpect(jsonPath("$.data.data.refreshToken").isString())
                .andExpect(jsonPath("$.data.data.onlineCount").value(2))
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = JSON.parseObject(response);
        String accessToken = jsonObject.getJSONObject("data").getJSONObject("data").getString("accessToken");
        String offlineToken = jsonObject.getJSONObject("data").getJSONObject("data").getString("offlineToken");

        /**
         * 这个踢出功能权限太大了，可以将不同平台的都踢出去了
         * 后面可以考虑根据平台来踢人
         * 踢人就是使对方的 token 无效，但是没办法将token标记为无效，只能利用缓存或者数据库来实现
         * 但是，这样的方式就会在认证的时候还访问缓存或者数据库等资源，得不偿失，还是别用这个功能的好
         * 这样，认证直接就是解析token了
         */
        TokenDto tokenDto = new TokenDto();
        tokenDto.setOfflineToken(offlineToken);
        mockMvc.perform(put("/api/authenticate")
            .header("Authorization","Bearer " + accessToken)
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(JSON.toJSONString(tokenDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.data.onlineCount").value(1));
    }
}
