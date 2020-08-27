package com.forum.controller;

import com.forum.entity.DiscussPost;
import com.forum.entity.Page;
import com.forum.entity.User;
import com.forum.service.DiscussPostService;
import com.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    /**
     * 显示 index 首页
     * @param model
     * @param page
     * @return
     */
    @GetMapping(path = {"/index", "/"})
    public String getIndexPage(Model model, Page page){

        //SpringMVC 自动将 Page 注入到 Model
        page.setRows(discussPostService.selectDiscussPostRows(0));

        page.setPath("/index");

        List<DiscussPost> discussPostList = discussPostService.selectDiscussPosts(0, page.getOffset(), page.getLimit());

        List<Map<String, Object>> list = new ArrayList<>();

        for (DiscussPost discussPost : discussPostList) {

            Map<String, Object> map = new HashMap<>();

            map.put("discussPost", discussPost);

            User user = userService.findUserByUserId(discussPost.getUserId());

            map.put("user", user);

            list.add(map);

        }

        model.addAttribute("list", list);

        return "index";
    }

    @GetMapping(value = "/error")
    public String getErrorPage(){

        return "/error/500";

    }
}
