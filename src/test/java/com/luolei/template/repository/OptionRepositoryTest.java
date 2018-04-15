package com.luolei.template.repository;

import com.luolei.template.Application;
import com.luolei.template.domain.Option;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author luolei
 * @createTime 2018-04-15 13:04
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

    @Before
    public void setUp() {
        Option option = new Option();
        option.setKey("name");
        option.setValue("luolei");
        option.setExplanation("姓名");
        optionRepository.save(option);

        option = new Option();
        option.setEncrypted(true);
        option.setKey("password");
        option.setEncryptedValue("123456");
        option.setExplanation("密码");
        optionRepository.save(option);

        log.info("========== 开始测试 ==========");
    }

    @After
    public void clear() {
        log.info("========== 结束测试 ==========");
    }

    @Test
    public void findOneByKey() {
        Optional<Option> optional = optionRepository.findOneByKey("name");
        assertThat(optional.isPresent()).isTrue();
        optional = optionRepository.findOneByKey("password");
        assertThat(optional.isPresent()).isTrue();
        Option option = optional.get();
        assertThat(option.getValue()).isNotEqualToIgnoringCase("123456");
        assertThat(option.getEncryptedValue()).isEqualTo("123456");
    }
}
