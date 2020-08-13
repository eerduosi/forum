package com.forum.interceptor;

import com.forum.entity.LoginTicket;
import com.forum.entity.User;
import com.forum.service.UserService;
import com.forum.util.CookieUtil;
import com.forum.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /**
         * 从 Cookie 中获取登录凭证
         */
        String ticket = CookieUtil.getValue(request, "ticket");

        if(ticket != null){

            /**
             * 查询凭证
             */
            LoginTicket loginTicket = userService.findLoginTicket(ticket);

            /**
             * 判断凭证是否有效
             */
            if(loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())){

                /**
                 * 依据登录凭证查询 user
                 */
                User user = userService.selectUserByUserId(loginTicket.getUserId());

                /**
                 * 在本次请求中持有 user
                 */
                hostHolder.setUser(user);

            }

        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        /**
         * 在本次请求中获取 user
         */
        User user = hostHolder.getUser();

        /**
         * 获取的 user 不为空 并且 modelAndView 不为空 , 在 modelAndView 中设置 user 信息
         */
        if(user != null && modelAndView != null){

            modelAndView.addObject("loginUser", user);

        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        /**
         * 清理用户
         */
        hostHolder.clear();

    }

}
