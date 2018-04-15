package com.luolei.template.web.resources;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Preconditions;
import com.luolei.template.domain.Message;
import com.luolei.template.domain.support.MessageState;
import com.luolei.template.repository.MessageRepository;
import com.luolei.template.security.support.HasRole;
import com.luolei.template.support.Constants;
import com.luolei.template.support.R;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author luolei
 * @createTime 2018-04-15 00:59
 */
@HasRole({ Constants.ROLE_ADMIN })
@RestController
@RequestMapping(path = {"/api/message"}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class MessageResource {

    public final MessageRepository messageRepository;

    public MessageResource(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * 创建一个未读消息
     * @param message
     * @return
     */
    @PostMapping
    public R createMessage(@RequestBody Message message) {
        Preconditions.checkArgument(StrUtil.isNotBlank(message.getTitle()), "title 为空");
        Preconditions.checkArgument(StrUtil.isNotBlank(message.getContent()), "content 为空");
        message.setState(MessageState.UNREAD);
        message = messageRepository.save(message);
        return R.ok(message);
    }

    /**
     * 标记消息为已读
     * @param id
     * @return
     */
    @PutMapping("/read/{id}")
    public R markRead(@PathVariable(name = "id") Long id) {
        return modifyState(id, MessageState.READED);
    }

    /**
     * 标记消息为删除
     * @param id
     * @return
     */
    @PutMapping("/delete/{id}")
    public R markDelete(@PathVariable(name = "id") Long id) {
        return modifyState(id, MessageState.DELETED);
    }

    /**
     * 将标记删除消息状态重置为已读
     * @param id
     * @return
     */
    @PutMapping("/reset/{id}")
    public R markReset(@PathVariable(name = "id") Long id) {
        return modifyState(id, MessageState.READED);
    }

    /**
     * 消息查询
     * @param stateStr
     * @param dateAfterStr
     * @return
     */
    @GetMapping
    public R findMessage(@RequestParam(required = false, name = "state") String stateStr, @RequestParam(required = false, name = "dateAfter") String dateAfterStr) {
        MessageState state = getState(stateStr);
        Instant dateAfter = getDateAfter(dateAfterStr);
        if (Objects.nonNull(state)) {
            return R.ok(messageRepository.findByStateAndCreatedDateAfter(state, dateAfter));
        } else {
            return R.ok(messageRepository.findAllByCreatedDateAfter(dateAfter));
        }
    }

    /**
     * 消息数量
     * @param stateStr
     * @param dateAfterStr
     * @return
     */
    @GetMapping("/count")
    public R countMessage(@RequestParam(required = false, name = "state") String stateStr, @RequestParam(required = false, name = "dateAfter") String dateAfterStr) {
        MessageState state = getState(stateStr);
        Instant dateAfter = getDateAfter(dateAfterStr);
        if (Objects.nonNull(state)) {
            return R.ok(messageRepository.countAllByStateAndCreatedDateAfter(state, dateAfter));
        } else {
            return R.ok(messageRepository.countAllByCreatedDateAfter(dateAfter));
        }
    }

    private R modifyState(Long id, MessageState state) {
        Preconditions.checkArgument(Objects.nonNull(id), "id 为null");
        Message message = messageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id:" + id + " 不存在"));
        message.setState(state);
        message = messageRepository.save(message);
        return R.ok(message);
    }

    private MessageState getState(String stateStr) {
        MessageState state = null;
        if (StrUtil.isNotBlank(stateStr)) {
            for (MessageState messageState : MessageState.values()) {
                if (messageState.name().equalsIgnoreCase(stateStr)) {
                    state = messageState;
                    break;
                }
            }
            if (Objects.isNull(state)) {
                throw new IllegalArgumentException("状态名不合法 READED|UNREAD|DELETED");
            }
        }
        return state;
    }

    private Instant getDateAfter(String dateAfterStr) {
        Instant dateAfter = null;
        if (StrUtil.isNotBlank(dateAfterStr)) {
            try {
                dateAfter = Instant.from(LocalDate.parse(dateAfterStr, DateTimeFormatter.BASIC_ISO_DATE));
            } catch (Exception e) {
                throw new IllegalArgumentException("日期不合法,格式为yyyyMMdd");
            }
        }
        if (Objects.isNull(dateAfter)) {
            // 如果不传时间，默认查近期3个月的消息
            dateAfter = Instant.now().minusSeconds(90 * 24 * 60 * 60);
        }
        return dateAfter;
    }
}
