package com.luolei.template.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luolei.template.Application;
import com.luolei.template.domain.Message;
import com.luolei.template.domain.Option;
import com.luolei.template.domain.support.MessageState;
import com.luolei.template.repository.AuthorityRepository;
import com.luolei.template.repository.MessageRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author luolei
 * @createTime 2018-04-15 18:54
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class MessageResourceTest extends AbstractTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private String accessToken;

    /**
     * 初始化3个未读消息，1个已读消息，1个删除消息
     * @throws Exception
     */
    @Override
    public void before() throws Exception {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        Message message = new Message();
        message.setTitle("消息1");
        message.setContent("消息正文1");
        messageRepository.save(message);

        message = new Message();
        message.setTitle("消息2");
        message.setContent("消息正文2");
        messageRepository.save(message);

        message = new Message();
        message.setTitle("消息3");
        message.setContent("消息正文3");
        messageRepository.save(message);

        message = new Message();
        message.setTitle("消息4");
        message.setContent("消息正文4");
        message.setState(MessageState.READED);
        messageRepository.save(message);

        message = new Message();
        message.setTitle("消息5");
        message.setContent("消息正文5");
        message.setState(MessageState.DELETED);
        messageRepository.save(message);

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
    }

    @Test
    public void createMessage() throws Exception {
        Message message = new Message();
        message.setTitle("测试消息");
        message.setContent("测试消息正文");
        String content = mockMvc.perform(post("/api/message")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andReturn().getResponse().getContentAsString();
        log.info(content);
        JSONObject jsonObject = JSON.parseObject(content);
        assertThat(jsonObject.getJSONObject("data").getString("id")).isNotBlank();

        message = new Message();
        mockMvc.perform(post("/api/message")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(message)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("illegal_argument"));
    }

    @Test
    public void findMessageAndMark() throws Exception {
        /**
         * 先查询 未读消息
         */
        String content = mockMvc.perform(get("/api/message")
                .param("state", "UNREAD")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andReturn().getResponse().getContentAsString();
        log.info(content);
        JSONObject jsonObject = JSON.parseObject(content);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        assertThat(jsonArray).isNotNull();
        assertThat(jsonArray.size()).isGreaterThan(0);
        JSONObject message = jsonArray.getJSONObject(0);

        long id = message.getLongValue("id");
        assertThat(id).isGreaterThan(0L);

        /**
         * 查询未读消息数量
         * 预期3
         */
        content = mockMvc.perform(get("/api/message/count")
                .param("state", "UNREAD")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andReturn().getResponse().getContentAsString();
        log.info(content);
        jsonObject = JSON.parseObject(content);
        assertThat(jsonObject.getJSONObject("data").getIntValue("data")).isEqualTo(3);

        /**
         * 将刚刚查的未读消息的第一个标记为已读
         */
        mockMvc.perform(put("/api/message/read/" + id)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));

        /**
         * 未读消息数量，预期 2
         */
        content = mockMvc.perform(get("/api/message/count")
                .param("state", "UNREAD")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andReturn().getResponse().getContentAsString();
        jsonObject = JSON.parseObject(content);
        assertThat(jsonObject.getJSONObject("data").getIntValue("data")).isEqualTo(2);

        /**
         * 已读消息数量，预期2
         */
        content = mockMvc.perform(get("/api/message/count")
                .param("state", "READED")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andReturn().getResponse().getContentAsString();
        jsonObject = JSON.parseObject(content);
        assertThat(jsonObject.getJSONObject("data").getIntValue("data")).isEqualTo(2);

        /**
         * 将刚刚已读的标记为删除
         */
        mockMvc.perform(put("/api/message/delete/" + id)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));

        /**
         * 已读消息数量，预期1
         */
        content = mockMvc.perform(get("/api/message/count")
                .param("state", "READED")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andReturn().getResponse().getContentAsString();
        jsonObject = JSON.parseObject(content);
        assertThat(jsonObject.getJSONObject("data").getIntValue("data")).isEqualTo(1);

        /**
         * 删除的消息，预期2
         */
        content = mockMvc.perform(get("/api/message/count")
                .param("state", "DELETED")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andReturn().getResponse().getContentAsString();
        jsonObject = JSON.parseObject(content);
        assertThat(jsonObject.getJSONObject("data").getIntValue("data")).isEqualTo(2);

        /**
         * 将刚刚删除的消息从回收站里面捞出来，变成已读消息
         */
        mockMvc.perform(put("/api/message/reset/" + id)
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));

        /**
         * 已读消息数量，预期1
         */
        content = mockMvc.perform(get("/api/message/count")
                .param("state", "READED")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andReturn().getResponse().getContentAsString();
        jsonObject = JSON.parseObject(content);
        assertThat(jsonObject.getJSONObject("data").getIntValue("data")).isEqualTo(2);

        /**
         * 删除的消息，预期2
         */
        content = mockMvc.perform(get("/api/message/count")
                .param("state", "DELETED")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andReturn().getResponse().getContentAsString();
        jsonObject = JSON.parseObject(content);
        assertThat(jsonObject.getJSONObject("data").getIntValue("data")).isEqualTo(1);
    }
}
