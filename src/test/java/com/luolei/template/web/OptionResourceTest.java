package com.luolei.template.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luolei.template.Application;
import com.luolei.template.domain.Option;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author luolei
 * @createTime 2018-04-15 18:17
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class OptionResourceTest extends AbstractTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private String accessToken;

    /**
     * 初始化一个 key :temp 的配置
     * @throws Exception
     */
    @Override
    public void before() throws Exception {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        LoginVM loginVM = new LoginVM();
        loginVM.setUsername("admin");
        loginVM.setPassword("admin");
        loginVM.setRememberMe(false);
        String response = mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JSON.toJSONString(loginVM)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.accessToken").isString())
                .andReturn().getResponse().getContentAsString();

        JSONObject jsonObject = JSON.parseObject(response);
        String accessToken = jsonObject.getJSONObject("data").getString("accessToken");
        this.accessToken = accessToken;

        Option option = new Option();
        option.setKey("temp");
        option.setValue("临时");
        option.setExplanation("测试用");
        mockMvc.perform(post("/api/option")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(option)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));
    }

    @Test
    public void createOption() throws Exception {
        /**
         * 新增一个配置
         */
        Option option = new Option();
        option.setKey("name");
        option.setValue("luolei");
        option.setExplanation("名字");
        String content = mockMvc.perform(post("/api/option")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(option)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andReturn().getResponse().getContentAsString();
        log.info(content);
        JSONObject jsonObject = JSON.parseObject(content);
        assertThat(jsonObject.getJSONObject("data").getString("id")).isNotBlank();

        /**
         * 新增一个已经存在key的配置，预期失败，不合法的参数
         */
        option = new Option();
        option.setKey("name");
        option.setValue("luolei");
        option.setExplanation("名字");
        mockMvc.perform(post("/api/option")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(option)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("illegal_argument"));

        /**
         * 新增一个value 存储要加密的配置
         */
        option = new Option();
        option.setKey("password");
        option.setValue("123456");
        option.setEncrypted(true);
        option.setExplanation("名字");
        content = mockMvc.perform(post("/api/option")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(option)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andReturn().getResponse().getContentAsString();
        log.info(content);
        jsonObject = JSON.parseObject(content);
        assertThat(jsonObject.getJSONObject("data").getString("id")).isNotBlank();
    }

    @Test
    public void findOptionsAndDelete() throws Exception {
        long id = 0;
        /**
         * 查询出 key 为 temp 的配置
         */
        String content = mockMvc.perform(get("/api/option")
                .param("key", "temp")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andReturn().getResponse().getContentAsString();
        log.info(content);
        JSONObject jsonObject = JSON.parseObject(content);
        String value = jsonObject.getJSONObject("data").getString("value");
        id = jsonObject.getJSONObject("data").getLongValue("id");
        assertThat(value).isEqualTo("临时");

        /**
         * 查询，不带条件，默认是分页查询，第0页，size=10
         */
        content = mockMvc.perform(get("/api/option")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andReturn().getResponse().getContentAsString();
        log.info(content);

        /**
         * 根据id删除配置
         */
        assertThat(id).isGreaterThan(0L);
        mockMvc.perform(delete("/api/option/" + id)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));
    }

    /**
     * 测试修改配置
     * @throws Exception
     */
    @Test
    public void modifyOption() throws Exception {
        String modifiedValue = "this is temp";
        Option option = new Option();
        option.setKey("temp");
        option.setValue(modifiedValue);
        option.setExplanation("名字");
        String content = mockMvc.perform(put("/api/option")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(option)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andReturn().getResponse().getContentAsString();
        log.info(content);
        JSONObject jsonObject = JSON.parseObject(content);
        assertThat(jsonObject.getJSONObject("data").getString("value")).isEqualTo(modifiedValue);

        option = new Option();
        option.setKey("temp123");
        option.setValue(modifiedValue);
        option.setExplanation("名字");
        content = mockMvc.perform(put("/api/option")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(option)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("illegal_argument"))
                .andReturn().getResponse().getContentAsString();
        log.info(content);
    }
}
