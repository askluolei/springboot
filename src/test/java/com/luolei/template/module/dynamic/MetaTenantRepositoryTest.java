package com.luolei.template.module.dynamic;

import com.luolei.template.Application;
import com.luolei.template.module.dynamic.domain.MetaTenant;
import com.luolei.template.module.dynamic.repository.MetaTenantRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 罗雷
 * @date 2018/5/8 0008
 * @time 17:30
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class MetaTenantRepositoryTest {

    @Autowired
    private MetaTenantRepository metaTenantRepository;

    @Before
    public void setUp() {
        log.info("========== 开始测试 ==========");
    }

    @After
    public void clear() {
        log.info("========== 结束测试 ==========");
    }

    @Test
    public void test() {
        MetaTenant tenant = new MetaTenant();
        tenant.setName("test");
        metaTenantRepository.save(tenant);
        assertThat(metaTenantRepository.count()).isEqualTo(1);
        Optional<MetaTenant> test = metaTenantRepository.findOneByName("test");
        assertThat(test.isPresent()).isTrue();
    }
}
