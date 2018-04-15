package com.luolei.template.domain;

import com.luolei.template.domain.support.MessageState;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author luolei
 * @createTime 2018-04-14 22:45
 */
@Getter
@Setter
@Entity
@Table(name = "t_message")
public class Message extends AbstractAuditingEntity {

    @Column(name = "c_title", nullable = false, length = 128)
    private String title;

    @Column(name = "c_content", nullable = false)
    private String content;

    @Column(name = "c_state")
    @Enumerated(EnumType.STRING)
    private MessageState state = MessageState.UNREAD;
}
