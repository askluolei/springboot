package com.luolei.template.web;

import com.luolei.template.Application;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * web 测试
 * 简单的测试，json是否正常，异常是否处理好了
 *
 * @author luolei
 * @createTime 2018-04-06 11:19
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class HelloControllerTest {

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
    public void testIndex() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.data").value("hello, world"));
    }

    @Test
    public void testLocalDate() throws Exception {
        mockMvc.perform(get("/test/date"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.data").value(LocalDate.now().toString()));
    }

    @Test
    public void testLocalTime() throws Exception {
        mockMvc.perform(get("/test/time"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));
    }

    @Test
    public void testLocalDateTime() throws Exception {
        mockMvc.perform(get("/test/datetime"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));
    }

    @Test
    public void testInstant() throws Exception {
        mockMvc.perform(get("/test/instant"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"));
    }

    @Test
    public void testLongValue() throws Exception {
        mockMvc.perform(get("/test/long"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.data.data").isString());
    }

    @Test
    public void testIllegalArgument() throws Exception {
        mockMvc.perform(get("/test/illegal-argument"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("illegal_argument"));
    }

    @Test
    public void testBaseException() throws Exception {
        mockMvc.perform(get("/test/base-exception"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("known_biz_error"));
    }

    @Test
    public void testRuntimeException() throws Exception {
        mockMvc.perform(get("/test/runtime-exception"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("internal_error"));
    }
}
