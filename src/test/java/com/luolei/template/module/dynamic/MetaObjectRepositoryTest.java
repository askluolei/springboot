package com.luolei.template.module.dynamic;

import com.luolei.template.Application;
import com.luolei.template.module.dynamic.domain.MetaObject;
import com.luolei.template.module.dynamic.domain.MetaTenant;
import com.luolei.template.module.dynamic.repository.MetaObjectRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author 罗雷
 * @date 2018/5/8 0008
 * @time 17:45
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class MetaObjectRepositoryTest {

    @Autowired
    private MetaObjectRepository objectRepository;

    @Autowired
    private MetaTenantRepository tenantRepository;

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
        tenant = tenantRepository.save(tenant);
        assertThat(tenantRepository.count()).isEqualTo(1);

        MetaObject object1 = new MetaObject();
        object1.setObjectName("o1");
        object1.setObjectNaturalName("对象1");
        object1.setTenant(tenant);
        objectRepository.save(object1);

        MetaObject object2 = new MetaObject();
        object2.setObjectName("o2");
        object2.setObjectNaturalName("对象2");
        object2.setTenant(tenant);
        objectRepository.save(object2);

        assertThat(objectRepository.count()).isEqualTo(2);

        assertThat(objectRepository.findAllByObjectName("o1").size()).isEqualTo(1);
        assertThat(objectRepository.findAllByObjectNaturalName("对象1").size()).isEqualTo(1);
        assertThat(objectRepository.findAllByTenant(tenant.getId()).size()).isEqualTo(2);
    }
}
