package com.forum.mapper;

import com.forum.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageMapper {

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
    List<Message> selectConversations(@Param(value = "userId") Integer userId, @Param(value = "offset")Integer offset, @Param(value = "limit")Integer limit);

    /**
     * 查询当前用户的会话数量
     * @param userId
     *
     * @return
     *
     */
    Integer selectConversationCount(Integer userId);

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
    List<Message> selectLetters(@Param(value = "conversationId")String conversationId, @Param(value = "offset")Integer offset, @Param(value = "limit")Integer limit);

    /**
     * 查询某个会话包含的私信数量
     *
     * @param conversationId
     *
     * @return
     */
    Integer selectLetterCount(String conversationId);

    /**
     * 查询未读私信的数量
     *
     * @param userId
     *
     * @param conversationId
     *
     * @return
     */
    Integer selectLetterUnreadCount(@Param(value = "userId") Integer userId, @Param(value = "conversationId") String conversationId);

}