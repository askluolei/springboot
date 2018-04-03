package com.luolei.template.domain;

import com.luolei.template.domain.support.AuthorityType;
import lombok.Data;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * equals 和 hashCode 测试
 * 判断数据库实体是否相同不是根据内容来的，而是 id
 * 当然，是继承了 AbstractAuditingEntity 并且没有重写 equals 和 hashcode 才有这个特性
 *
 * @author 罗雷
 * @date 2018/4/3 0003
 * @time 9:45
 */
public class EqualsAndHashCodeTest {

    /**
     * 这是个错误的例子
     * 不要在继承了 AbstractAuditingEntity 后重写 equals 和 hashCode （使用 @Getter 和 @Setter 代替 @Data）
     */
    @Data
    public static class Counterexample extends AbstractAuditingEntity {
        private String str;
    }

    /**
     * 反例测试
     */
    @Test
    public void testCounterexample() {
        Counterexample counterexample = new Counterexample();
        counterexample.setStr("str");
        counterexample.setId(2L);

        Counterexample counterexample1 = new Counterexample();
        counterexample1.setStr("str222");
        counterexample1.setId(2L);
        assertThat(counterexample.equals(counterexample1)).isFalse();

        Set<Counterexample> counterexamples = new HashSet<>();
        counterexamples.add(counterexample);
        counterexamples.add(counterexample1);

        assertThat(counterexamples.size()).isEqualTo(2);

        Counterexample counterexample2 = new Counterexample();
        counterexample1.setId(2L);
        assertThat(counterexamples.contains(counterexample2)).isFalse();
    }

    @Test
    public void testEquals() {
        Authority authority = new Authority();
        authority.setId(2L);
        authority.setAuthorityCn("用户添加");
        authority.setAuthority("user:add");
        authority.setType(AuthorityType.ENTITY);

        Authority authority1 = new Authority();
        authority1.setId(2L);
        assertThat(authority.equals(authority1)).isTrue();
    }

    @Test
    public void testHashCode() {
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setId(2L);
        authority.setAuthorityCn("用户添加");
        authority.setAuthority("user:add");
        authority.setType(AuthorityType.ENTITY);

        Authority authority1 = new Authority();
        authority1.setId(2L);
        authorities.add(authority);
        authorities.add(authority1);

        assertThat(authorities.size()).isEqualTo(1);

        Authority authority2 = new Authority();
        authority2.setId(2L);
        assertThat(authorities.contains(authority2)).isTrue();
    }
}
