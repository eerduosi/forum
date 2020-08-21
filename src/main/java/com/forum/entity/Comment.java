package com.forum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    /**
     * 评论 id
     */
    private Integer id;

    /**
     * 发起评论 userId
     */
    private Integer userId;

    /**
     * 被回复目标类型 , 1-帖子 ; 2-评论; 3-用户;
     */
    private Integer entityType;

    /**
     * 被回复目标类型的 id
     */
    private Integer entityId;

    /**
     * 被回复评论的评论的 id, 如果没有则默认为 0
     */
    private Integer targetId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论状态
     */
    private Integer status;

    /**
     * 评论时间
     */
    private Date createTime;

}
