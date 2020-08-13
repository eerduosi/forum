package com.forum.service;

import com.forum.entity.LoginTicket;
import com.forum.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserService {


    /**
     * 注册用户
     *
     * @param user
     *
     * @return
     *
     */
    Map<String, Object> register(User user);

    /**
     *
     * 激活用户
     *
     * @param userId : id
     *
     * @param code : 激活码
     *
     * @return
     *
     */
    Integer activation(Integer userId, String code);


    /**
     *
     * 依据 id 查找 user
     *
     * @param id
     *
     * @return
     *
     */
    User selectUserByUserId(Integer id);

    /**
     * 获取 username , password , expiredSeconds 进行登录
     *
     * @param username : 用户名
     *
     * @param password : 密码
     *
     * @param expiredSeconds : 过期时间
     *
     * @return
     *
     */
    Map<String, Object> login(String username, String password, Integer expiredSeconds);

    /**
     *
     * 获取当前用户 ticket 并退出登录
     *
     * @param ticket
     *
     */
    void logout(String ticket);

    /**
     * 查询登录凭证
     *
     * @param ticket : 登录凭证
     *
     * @return
     *
     */
    LoginTicket findLoginTicket(String ticket);

    /**
     *
     * 更新用户头像
     *
     * @param userId
     *
     * @param headerUrl
     *
     * @return
     *
     */
    Integer updateHeaderUrl(Integer userId, String headerUrl);
}
