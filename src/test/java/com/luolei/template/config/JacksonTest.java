package com.luolei.template.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luolei.template.Application;
import com.luolei.template.support.R;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author luolei
 * @createTime 2018-04-11 21:47
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class JacksonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    public void testOk() throws Exception{
        R ok = R.ok();
        String json = objectMapper.writeValueAsString(ok);
        log.info(json);
        assertThat(json).doesNotContain("data");
        ok = R.ok("hello world");
        json = objectMapper.writeValueAsString(ok);
        log.info(json);
        assertThat(json).contains("\"data\":{\"data\":\"hello world\"}");
        ok = R.ok("hello").data("first", "luolei");
        json = objectMapper.writeValueAsString(ok);
        log.info(json);
        assertThat(json).contains("\"data\":{\"data\":\"hello\",\"first\":\"luolei\"}");
    }
}
