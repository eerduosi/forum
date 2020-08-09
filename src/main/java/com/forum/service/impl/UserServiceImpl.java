package com.forum.service.impl;

import com.forum.entity.User;
import com.forum.mapper.UserMapper;
import com.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public User selectUserByUserId(Integer id) {
        return userMapper.selectUserByUserId(id);
    }

    @Override
    public User selectUserByUserName(String username) {
        return userMapper.selectUserByUserName(username);
    }

    @Override
    public User selectUserByUserEmail(String email) {
        return userMapper.selectUserByUserEmail(email);
    }

    @Override
    public Integer insertUser(User user) {
        return userMapper.insertUser(user);
    }

    @Override
    public Integer updateUserStatusByUserId(Integer id, Integer status) {
        return userMapper.updateUserStatusByUserId(id, status);
    }

    @Override
    public Integer updateUserHeaderUrlByUserId(Integer id, String headerUrl) {
        return userMapper.updateUserHeaderUrlByUserId(id, headerUrl);
    }

    @Override
    public Integer updateUserPasswordByUserId(Integer id, String password) {
        return userMapper.updateUserPasswordByUserId(id, password);
    }
}
