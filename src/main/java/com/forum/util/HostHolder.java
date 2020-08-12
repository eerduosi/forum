package com.forum.util;

import com.forum.entity.User;
import org.springframework.stereotype.Component;

/**
 * 用于代替 Session 对象
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    /**
     * 将 user 放入 ThreadLocal
     * @param user
     */
    public void setUser(User user){

        users.set(user);

    }

    /**
     * 获取 ThreadLocal 中的 user
     * @return
     */
    public User getUser(){

        return users.get();

    }

    /**
     * 清除 ThreadLocal 中的 user
     * @return
     */
    public void clear(){

        users.remove();

    }

}
