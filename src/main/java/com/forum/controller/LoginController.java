package com.forum.controller;

import com.forum.entity.User;
import com.forum.service.UserService;
import com.forum.util.ForumConstant;
import com.google.code.kaptcha.Producer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    /**
     * 跳转注册页面
     * @return
     */
    @GetMapping(value = "/register")
    public String getRegisterPage(){

        return "site/register";

    }

    /**
     * 跳转登录页面
     * @return
     */
    @GetMapping(value = "/login")
    public String getLoginPage(){

        return "site/login";

    }


    /**
     * 账号注册 handler
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

    /**
     * 账号激活 handler
     *
     * @param model
     *
     * @param userId
     *
     * @param code
     *
     * @return
     *
     */
    @GetMapping(value = "/activation/{userId}/{code}")
    public String activation(Model model, @PathVariable(value = "userId")Integer userId, @PathVariable(value = "code")String code){

        Integer result = userService.activation(userId, code);

        if( result.equals(ForumConstant.ACTIVATION_SUCCESS) ){

            model.addAttribute("msg", "激活成功 , 请登录!");

            model.addAttribute("target", "/index");

        }else if(result.equals(ForumConstant.ACTIVATION_REPEATED)){

            model.addAttribute("msg", "无效操作 , 此账号已激活成功!");

            model.addAttribute("target", "/index");

        }else{

            model.addAttribute("msg", "激活失败 , 激活码不正确!");

            model.addAttribute("target", "/index");

        }

        return "site/operate-result";
    }


    /**
     * 生成验证码图片
     */
    @GetMapping(value = "/kaptcha")
    public void getKaptcha(HttpServletResponse response, HttpSession session){

        /**
         * 生成验证码字符串
         */
        String text = kaptchaProducer.createText();

        /**
         * 用字符串生成验证码图片
         */
        BufferedImage image = kaptchaProducer.createImage(text);

        /**
         * 把验证码字符串存入 session
         */
        session.setAttribute("kaptcha", text);

        /**
         * 设置响应类型
         */
        response.setContentType("image/png");

        try {

            ServletOutputStream outputStream = response.getOutputStream();

            ImageIO.write(image, "png", outputStream);

        } catch (IOException e) {

            logger.error("相应验证码失败 : " + e.getMessage());

        }

    }

    /**
     *
     * 验证登录信息
     *
     * @param username : 用户名
     *
     * @param password : 密码
     *
     * @param verifyCode : 验证码
     *
     * @param rememberMe : 记住功能
     *
     * @param model : Model
     *
     * @param session : session
     *
     * @param response : response
     *
     * @return
     *
     */
    @PostMapping(value = "/login")
    public String login(@RequestParam(value = "username") String username, @RequestParam(value = "password")String password, @RequestParam(value = "verifyCode")String verifyCode, @RequestParam(value = "rememberMe", required = false)boolean rememberMe, Model model, HttpSession session, HttpServletResponse response){

        /**
         * 获取 sesion 中的验证码
         */
        String kaptcha = (String) session.getAttribute("kaptcha");

        /**
         * 检查验证码
         */
        if(StringUtils.isBlank(kaptcha) || StringUtils.isBlank(verifyCode) || !kaptcha.equalsIgnoreCase(verifyCode)){

            model.addAttribute("codeMsg", "验证码不正确");

            return "site/login";

        }

        /**
         * 检查账号 , 密码 , 设定 expired 时间
         */
        Integer expiredSeconds = rememberMe ? ForumConstant.REMEMBER_EXPIRED_SECONDS : ForumConstant.DEFAULT_EXPIRED_SECONDS;

        Map<String, Object> map = userService.login(username, password, expiredSeconds);

        /**
         * 登录成功 , 存在 ticket 登录凭证 , 设置 Cookie
         */
        if(map.containsKey("ticket")){

            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());

            cookie.setPath(contextPath);

            cookie.setMaxAge(expiredSeconds);

            response.addCookie(cookie);

            return "redirect:/index";

        }else{

            model.addAttribute("usernameMsg", map.get("usernameMsg"));

            model.addAttribute("passwordMsg", map.get("passwordMsg"));

            return "site/login";

        }

    }

    /**
     *
     * 用户退出
     *
     * @param ticket
     *
     */
    @GetMapping(value = "/logout")
    public String logout(@CookieValue(value = "ticket")String ticket){

        userService.logout(ticket);

        return "redirect:/login";

    }

}
