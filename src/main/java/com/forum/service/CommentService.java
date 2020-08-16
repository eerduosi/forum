package com.forum.service;

import com.forum.entity.Comment;

import java.util.List;

public interface CommentService {

    /**
     * 查询评论
     *
     * @param entityType : 被回复目标类型
     *
     * @param entityId   : 被回复目标类型 id
     *
     * @param offset     : 从第 offset 条数据开始
     *
     * @param limit      : 共获取 limit 条数据
     *
     * @return
     *
     */
    List<Comment> findCommentsByEntity(Integer entityType, Integer entityId, Integer offset, Integer limit);

    /**
     * 获取评论的数量
     *
     * @param entityType : 被回复目标类型
     *
     * @param entityId   : 被回复目标类型 id
     *
     * @return
     */
    Integer findCommentCount(Integer entityType, Integer entityId);

}
