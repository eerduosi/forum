package com.forum.controller;

import com.forum.entity.Comment;
import com.forum.service.CommentService;
import com.forum.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@Controller
@RequestMapping(value = "/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping(value = "/add/{discussPostId}")
    public String addComment(@PathVariable(value = "discussPostId")Integer discussPostId, Comment comment){

        comment.setUserId(hostHolder.getUser().getId());

        comment.setStatus(0);

        comment.setCreateTime(new Date());

        commentService.addComment(comment);

        System.out.println(comment);

        return "redirect:/discuss/detail/" + discussPostId;

    }

}
