package com.forum.controller;

import com.forum.entity.DiscussPost;
import com.forum.entity.User;
import com.forum.service.DiscussPostService;
import com.forum.util.ForumUtil;
import com.forum.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping(value = "/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping(value = "/add")
    @ResponseBody
    public String addDiscussPost(String title, String content){

        User user = hostHolder.getUser();

        if(user == null){

            return ForumUtil.getJSONString(403, "您还未登录");

        }

        DiscussPost discussPost = new DiscussPost();

        discussPost.setUserId(hostHolder.getUser().getId());

        discussPost.setTitle(title);

        discussPost.setContent(content);

        discussPost.setCreateTime(new Date());

        discussPostService.addDiscussPost(discussPost);

        return ForumUtil.getJSONString(0, "发布成功");

    }

}
