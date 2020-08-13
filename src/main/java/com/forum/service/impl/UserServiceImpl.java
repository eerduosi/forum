package com.forum.service.impl;

import com.forum.entity.LoginTicket;
import com.forum.entity.User;
import com.forum.mapper.LoginTicketMapper;
import com.forum.mapper.UserMapper;
import com.forum.service.UserService;
import com.forum.util.ForumConstant;
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
public class UserServiceImpl implements UserService{
    
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

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
    @Value("${forum.path.domain}")
    private String domain;

    /**
     * 项目路径
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 注册时用户信息验证
     *
     * @param user
     *
     * @return
     *
     */
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
         * 设置注册用户的信息
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

    /**
     * 激活用户
     *
     * @param userId
     *
     * @param code
     *
     * @return
     *
     */
    @Override
    public Integer activation(Integer userId, String code) {

        User user = userMapper.selectUserByUserId(userId);

        /**
         * 判断用户激活状态
         */
        if( user.getStatus().equals(ForumConstant.ACTIVATION_SUCCESS) ){

            /**
             * 用户已激活 , 返回重复激活信息
             */
            return ForumConstant.ACTIVATION_REPEATED;

        }else if(user.getActivationCode().equals(code)){

            Integer result = userMapper.updateUserStatusByUserId(userId, ForumConstant.ACTIVATION_SUCCESS);

            if( result.equals(ForumConstant.ACTIVATION_SUCCESS) ){

                /**
                 * 更新激活状态成功 , 返回激活成功信息
                 */
                return ForumConstant.ACTIVATION_SUCCESS;

            }else{
                /**
                 * 更新激活状态失败 , 返回激活失败信息
                 */
                return ForumConstant.ACTIVATION_FAILURE;

            }


        }else{

            /**
             * 其余情况 , 返回激活失败信息
             */
            return ForumConstant.ACTIVATION_FAILURE;

        }

    }

    /**
     *
     * 依据 id 查找 user
     *
     * @param id
     *
     * @return
     *
     */
    @Override
    public User selectUserByUserId(Integer id) {

        return userMapper.selectUserByUserId(id);

    }

    /**
     *
     * 登录Service , 验证用户名, 密码
     *
     * @param username
     *
     * @param password
     *
     * @param expiredSeconds
     *
     * @return
     *
     */
    @Override
    public Map<String, Object> login(String username, String password, Integer expiredSeconds) {

        Map<String, Object> map = new HashMap<>();

        /**
         * 空值处理
         */
        if(StringUtils.isBlank(username)){

            map.put("usernameMsg", "账号不能为空");

            return map;

        }

        if(StringUtils.isBlank(password)){

            map.put("passwordMsg", "密码不能为空");

            return map;

        }

        /**
         * 验证是否已注册
         */
        User user = userMapper.selectUserByUserName(username);

        if(user == null){

            map.put("usernameMsg", "该账号未注册");

            return map;

        }

        if(!user.getStatus().equals(ForumConstant.ACTIVATION_SUCCESS)){

            map.put("usernameMsg", "该账号未激活");

            return map;

        }

        /**
         * 验证密码是否正确
         */
        password = ForumUtil.md5(password + user.getSalt());

        if(!user.getPassword().equals(password)){

            map.put("passwordMsg", "密码不正确");

            return map;

        }

        /**
         * 创建登录凭证
         */
        LoginTicket loginTicket = new LoginTicket();

        loginTicket.setUserId(user.getId());

        loginTicket.setTicket(ForumUtil.generateUUID());

        loginTicket.setStatus(ForumConstant.TICKET_LOGOUT);

        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));

        loginTicketMapper.insertLoginTicket(loginTicket);

        map.put("ticket", loginTicket.getTicket());

        return map;
    }

    /**
     *
     * 退出系统
     *
     * @param ticket
     *
     */
    @Override
    public void logout(String ticket) {

        loginTicketMapper.updateStatus(ticket, ForumConstant.TICKET_LOGIN);

    }

    /**
     * 查询登录凭证
     */
    @Override
    public LoginTicket findLoginTicket(String ticket){

        return loginTicketMapper.selectByTicket(ticket);

    }

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
    @Override
    public Integer updateHeaderUrl(Integer userId, String headerUrl) {

        return userMapper.updateUserHeaderUrlByUserId(userId, headerUrl);

    }
}
