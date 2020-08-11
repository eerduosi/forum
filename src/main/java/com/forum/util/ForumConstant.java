package com.forum.util;

public interface ForumConstant {

    /**
     * 重复激活
     */
    Integer ACTIVATION_REPEATED = 0;

    /**
     * 激活成功
     */
    Integer ACTIVATION_SUCCESS = 1;


    /**
     * 激活失败
     */
    Integer ACTIVATION_FAILURE = 2;

    /**
     * 退出状态
     */
    Integer TICKET_LOGOUT = 0;

    /**
     * 登录状态
     */
    Integer TICKET_LOGIN = 1;

    /**
     * 登录凭证默认超时时间
     */
    Integer DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 登录凭证记住状态的超时时间
     */
    Integer REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;
}
