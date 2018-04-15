package com.luolei.template.repository;

import com.luolei.template.domain.Message;
import com.luolei.template.domain.support.MessageState;

import java.time.Instant;
import java.util.List;

/**
 * @author luolei
 * @createTime 2018-04-14 23:32
 */
public interface MessageRepository extends BaseRepository<Message, Long> {


    /**
     * 根据状态查询
     * @param state
     * @return
     */
    List<Message> findAllByState(MessageState state);

    /**
     * 根据状态和创建日期查询
     * @param state
     * @param createdDate
     * @return
     */
    List<Message>  findByStateAndCreatedDateAfter(MessageState state, Instant createdDate);

    /**
     * 创建时间大于给定参数
     * @param createDate
     * @return
     */
    List<Message> findAllByCreatedDateAfter(Instant createDate);

    /**
     * 根据状态计数
     * @param state
     * @return
     */
    Integer countAllByState(MessageState state);

    /**
     * 根据状态和创建时间计数
     * @param state
     * @param createDate
     * @return
     */
    Integer countAllByStateAndCreatedDateAfter(MessageState state, Instant createDate);

    /**
     * 根据创建时间计数
     * @param createDate
     * @return
     */
    Integer countAllByCreatedDateAfter(Instant createDate);
}
