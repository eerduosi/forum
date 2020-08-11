package com.forum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 登录凭证
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginTicket {

    /**
     * 登录凭证 id
     */
    private Integer id;

    /**
     * 用户 id
     */
    private Integer userId;

    /**
     * 登录凭证
     */
    private String ticket;

    /**
     * 登录凭证状态
     */
    private Integer status;

    /**
     * 登录凭证过期时间
     */
    private Date expired;
}
