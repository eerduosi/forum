package com.forum.service;

import com.forum.entity.Message;

import java.util.List;

public interface MessageService {

    /**
     * 查询当前用户的会话列表, 针对每个会话只返回一条最新的私信
     * @param userId
     *
     * @param offset
     *
     * @param limit
     *
     * @return
     */
    List<Message> findConversations(Integer userId, Integer offset, Integer limit);

    /**
     * 查询当前用户的会话数量
     * @param userId
     *
     * @return
     *
     */
    Integer findConversationCount(Integer userId);

    /**
     * 查询某个会话包含的私信列表
     * @param conversationId
     *
     * @param offset
     *
     * @param limit
     *
     * @return
     */
    List<Message> findLetters(String conversationId, Integer offset, Integer limit);

    /**
     * 查询某个会话包含的私信数量
     *
     * @param conversationId
     *
     * @return
     */
    Integer findLetterCount(String conversationId);

    /**
     * 查询未读私信的数量
     *
     * @param userId
     *
     * @param conversationId
     *
     * @return
     */
    Integer findLetterUnreadCount(Integer userId,String conversationId);

    /**
     * 新增消息
     *
     * @param message
     */
    Integer addMessage(Message message);

    /**
     * 修改消息的状态
     *
     * @param ids
     *
     * @return
     */
    Integer readMessage(List<Integer> ids);
    
}
