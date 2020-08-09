package com.forum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscussPost {

    /**
     * 帖子 id
     */
    private Integer id;

    /**
     * 帖子所属用户 id
     */
    private Integer userId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private String content;

    /**
     * 帖子类型 : 0-普通; 1-置顶;
     */
    private Integer type;

    /**
     * 帖子状态 : 0-正常; 1-精华; 2-拉黑;
     */
    private Integer status;

    /**
     * 帖子创建时间
     */
    private Date createTime;

    /**
     * 帖子评论数
     */
    private Integer commentCount;

    /**
     * 帖子分数
     */
    private Double score;
}
