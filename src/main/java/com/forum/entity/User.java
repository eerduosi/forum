package com.forum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * 用户 id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 密码加盐值
     */
    private String salt;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户类型 : 0-普通用户; 1-超级管理员; 2-版主;
     */
    private Integer type;

    /**
     * 用户状态 : 0-未激活; 1-已激活;
     */
    private Integer status;

    /**
     * 用户激活码
     */
    private String activationCode;

    /**
     * 用户头像地址
     */
    private String headerUrl;

    /**
     * 用户注册时间
     */
    private Date createTime;
}
