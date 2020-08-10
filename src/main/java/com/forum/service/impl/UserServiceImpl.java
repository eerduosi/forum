package com.forum.service.impl;

import com.forum.entity.User;
import com.forum.mapper.UserMapper;
import com.forum.service.UserService;
import com.forum.util.ForumUtil;
import com.forum.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;

    /**
     * 发邮件工具类
     */
    @Autowired
    private MailClient mailClient;

    /**
     * 注入模板引擎
     */
    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 域名
     */
    @Value("${forum.key.domain}")
    private String domain;

    /**
     * 项目路径
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Override
    public Map<String, Object> register(User user){

        Map<String, Object> map = new HashMap<>();

        /**
         *
         * 空值处理
         *
         */
        if(user == null){

            throw new IllegalArgumentException("参数不能为空");

        }

        if(StringUtils.isBlank(user.getUsername())){

            map.put("usernameMsg", "账号不能为空");

        }

        if(StringUtils.isBlank(user.getPassword())){

            map.put("passwordMsg", "密码不能为空");

        }

        if(StringUtils.isBlank(user.getEmail())){

            map.put("emailMsg", "邮箱不能为空");

        }

        /**
         * 验证账号是否存在
         */
        if(userMapper.selectUserByUserName(user.getUsername()) != null){

            map.put("usernameMsg", "该账号已存在");

            return map;
        }

        /**
         * 验证邮箱是否已注册
         */
        if(userMapper.selectUserByUserEmail(user.getEmail()) != null){

            map.put("emailMsg", "该邮箱已注册");

        }

        /**
         * 注册用户
         */
        user.setSalt(ForumUtil.generateUUID().substring(0, 5));

        user.setPassword(ForumUtil.md5(user.getPassword() + user.getSalt()));

        user.setType(0);

        user.setStatus(0);

        user.setActivationCode(ForumUtil.generateUUID());

        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));

        user.setCreateTime(new Date());

        userMapper.insertUser(user);

        /**
         * 激活邮件
         */
        Context context = new Context();

        context.setVariable("email", user.getEmail());

        /**
         * http://localhost:8080/forum/activation/id/code
         */
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();

        context.setVariable("url", url);

        String content = templateEngine.process("mail/activation", context);

        mailClient.sendMail(user.getEmail(), "激活账号", content);

        return map;
    }
    
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
