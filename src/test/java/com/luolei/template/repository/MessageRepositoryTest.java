package com.luolei.template.repository;

import com.luolei.template.Application;
import com.luolei.template.domain.Message;
import com.luolei.template.domain.support.MessageState;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author luolei
 * @createTime 2018-04-15 10:02
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Before
    public void setUp() {
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

        log.info("========== 开始测试 ==========");
    }

    @After
    public void clear() {
        log.info("========== 结束测试 ==========");
    }

    @Test
    public void findAll() {
        List<Message> all = messageRepository.findAll();
        assertThat(all.size()).isEqualTo(5);
    }

    @Test
    public void findAllByState() {
        List<Message> allByState = messageRepository.findAllByState(MessageState.UNREAD);
        assertThat(allByState.size()).isEqualTo(3);
    }

    @Test
    public void findByStateAndCreatedDateAfter() {
        List<Message> allByStateAnAndCreatedDateAfter = messageRepository.findByStateAndCreatedDateAfter(MessageState.UNREAD, Instant.now());
        assertThat(allByStateAnAndCreatedDateAfter.size()).isEqualTo(0);
    }

    @Test
    public void findAllByCreatedDateAfter() {
        List<Message> allByCreatedDateAfter = messageRepository.findAllByCreatedDateAfter(Instant.now().minusSeconds(5));
        assertThat(allByCreatedDateAfter.size()).isEqualTo(5);
    }

    @Test
    public void countAllByState() {
        assertThat(messageRepository.countAllByState(MessageState.UNREAD)).isEqualTo(3);
        assertThat(messageRepository.countAllByState(MessageState.READED)).isEqualTo(1);
        assertThat(messageRepository.countAllByState(MessageState.DELETED)).isEqualTo(1);
    }

    @Test
    public void countAllByStateAndCreatedDateAfter() {
        assertThat(messageRepository.countAllByStateAndCreatedDateAfter(MessageState.UNREAD, Instant.now())).isEqualTo(0);
        assertThat(messageRepository.countAllByStateAndCreatedDateAfter(MessageState.UNREAD, Instant.now().minusSeconds(5))).isEqualTo(3);
    }

    @Test
    public void countAllByCreatedDateAfter() {
        assertThat(messageRepository.countAllByCreatedDateAfter(Instant.now())).isEqualTo(0);
        assertThat(messageRepository.countAllByCreatedDateAfter(Instant.now().minusSeconds(5))).isEqualTo(5);
    }
}
