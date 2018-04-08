package com.luolei.template.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luolei.template.Application;
import com.luolei.template.repository.AuthorityRepository;
import com.luolei.template.repository.RoleRepository;
import com.luolei.template.repository.UserRepository;
import com.luolei.template.web.controller.vm.LoginVM;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author luolei
 * @createTime 2018-04-07 11:03
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class SecurityControllerTest implements DataInit {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        init(authorityRepository, roleRepository, userRepository, passwordEncoder);
    }

    @Test
    public void testAnnotationOnType() throws Exception {
        mockMvc.perform(get("/api/security2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("authentication_error"));

        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("admin");
        loginVM.setPassword("admin");
        loginVM.setRememberMe(false);
        String contentAsString = mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(loginVM)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = JSON.parseObject(contentAsString);
        String accessToken = jsonObject.getJSONObject("data").getJSONObject("data").getString("accessToken");
        assertThat(accessToken).isNotBlank();

        mockMvc.perform(get("/api/security2")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("authorization_error"));

        loginVM = new LoginVM();
        loginVM.setUsername("user");
        loginVM.setPassword("user");
        loginVM.setRememberMe(false);
        contentAsString = mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(loginVM)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        jsonObject = JSON.parseObject(contentAsString);
        accessToken = jsonObject.getJSONObject("data").getJSONObject("data").getString("accessToken");
        assertThat(accessToken).isNotBlank();

        mockMvc.perform(get("/api/security2")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));
    }

    /**
     * 先不认证访问，返回未认证用户访问受限资源异常
     * 然后普通用户 user 有 p1 p4 权限
     * 2，3，5 访问异常
     * @throws Exception
     */
    @Test
    public void testNormalUser() throws Exception {
        mockMvc.perform(get("/api/security/authentication"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("authentication_error"));

        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("user");
        loginVM.setPassword("user");
        loginVM.setRememberMe(false);
        String contentAsString = mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(loginVM)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = JSON.parseObject(contentAsString);
        String accessToken = jsonObject.getJSONObject("data").getJSONObject("data").getString("accessToken");
        assertThat(accessToken).isNotBlank();

        mockMvc.perform(get("/api/security/authentication")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));

        mockMvc.perform(get("/api/security/role")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("authorization_error"));

        mockMvc.perform(get("/api/security/authority1")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));

        mockMvc.perform(get("/api/security/authority2")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("authorization_error"));

        mockMvc.perform(get("/api/security/authority3")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("authorization_error"));

        mockMvc.perform(get("/api/security/authority4")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));

        mockMvc.perform(get("/api/security/authority5")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("authorization_error"));
    }

    /**
     * 先不认证访问，返回未认证用户访问受限资源异常
     * 然后管理员用户 admin 有 p1, p2, p3, p5 权限
     * 4 访问异常
     * @throws Exception
     */
    @Test
    public void testAdminUser() throws Exception {
        mockMvc.perform(get("/api/security/authentication"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("authentication_error"));

        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("admin");
        loginVM.setPassword("admin");
        loginVM.setRememberMe(false);
        String contentAsString = mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(loginVM)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        JSONObject jsonObject = JSON.parseObject(contentAsString);
        String accessToken = jsonObject.getJSONObject("data").getJSONObject("data").getString("accessToken");
        assertThat(accessToken).isNotBlank();

        mockMvc.perform(get("/api/security/authentication")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));

        mockMvc.perform(get("/api/security/role")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));

        mockMvc.perform(get("/api/security/authority1")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));

        mockMvc.perform(get("/api/security/authority2")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));

        mockMvc.perform(get("/api/security/authority3")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));

        mockMvc.perform(get("/api/security/authority4")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("authorization_error"));

        mockMvc.perform(get("/api/security/authority5")
                .header("Authorization","Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));
    }
}
