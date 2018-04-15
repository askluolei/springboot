package com.luolei.template.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author luolei
 * @createTime 2018-04-15 13:17
 */
@Slf4j
public class OptionSecureValueTest {

    @Test
    public void test() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Option option = new Option();
        option.setEncrypted(true);
        option.setKey("password");
        option.setEncryptedValue("123456");

        String json = objectMapper.writeValueAsString(option);
        log.info(json);
        log.info(option.getValue());

        option = new Option();
        option.setEncrypted(false);
        option.setKey("password");
        option.setValue("123456");

        json = objectMapper.writeValueAsString(option);
        log.info(json);
        log.info(option.getValue());
    }
}
