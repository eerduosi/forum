package com.forum.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    /**
     * 私信id
     */
    private Integer id;

    /**
     * 发送者id
     */
    private Integer fromId;

    /**
     * 接收者id
     */
    private Integer toId;

    /**
     * 会话id
     */
    private String conversationId;

    /**
     * 私信内容
     */
    private String content;

    /**
     * 私信状态, 0-未读;1-已读;2-删除;
     */
    private int status;

    /**
     * 私信创建时间
     */
    private Date createTime;

}
