package com.forum.controller;

import com.forum.entity.User;
import com.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 返回注册页面
     * @return
     */
    @GetMapping(value = "/register")
    public String getRegisterPage(){

        return "site/register";

    }

    /**
     * 用户注册控制
     */
    @PostMapping(value = "/register")
    public String register(Model model, User user){

        /**
         * 用户注册 Service
         */
        Map<String, Object> register = userService.register(user);

        if(register == null || register.isEmpty()){

            model.addAttribute("msg", "注册成功, 我们已经向您的邮箱发送了一封激活邮件, 请尽快激活!");

            model.addAttribute("target", "/index");

            return "site/operate-result";

        }else{

            model.addAttribute("usernameMsg", register.get("usernameMsg"));

            model.addAttribute("passwordMsg", register.get("passwordMsg"));

            model.addAttribute("emailMsg", register.get("emailMsg"));

            return "site/register";

        }

    }

}
