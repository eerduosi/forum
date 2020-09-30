package com.forum.controller;

import com.forum.annotation.LoginRequired;
import com.forum.entity.Page;
import com.forum.entity.User;
import com.forum.service.FollowService;
import com.forum.service.UserService;
import com.forum.util.ForumConstant;
import com.forum.util.ForumUtil;
import com.forum.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class FollowController {

    private FollowService followService;

    @Autowired
    private void setFollowService(FollowService followService) {
        this.followService = followService;
    }

    private HostHolder hostHolder;

    @Autowired
    private void setHostHolder(HostHolder hostHolder) {
        this.hostHolder = hostHolder;
    }

    private UserService userService;

    @Autowired
    private void setUserService(UserService userService) {
        this.userService = userService;
    }


    /**
     * 关注用户
     */
    @PostMapping(value = "/follow")
    @ResponseBody
    @LoginRequired
    public String follow(Integer entityType, Integer entityId){

        User user = hostHolder.getUser();

        if (user == null) {

            return ForumUtil.getJSONString(403, "您还未登录");

        }

        followService.follow(user.getId(), entityType, entityId);

        return ForumUtil.getJSONString(0, "已关注!");

    }

    /**
     * 取消关注用户
     * @param entityType
     * @param entityId
     * @return
     */
    @PostMapping(value = "/unfollow")
    @ResponseBody
    @LoginRequired
    public String unfollow(Integer entityType, Integer entityId){

        User user = hostHolder.getUser();

        if (user == null) {

            return ForumUtil.getJSONString(403, "您还未登录");

        }

        followService.unfollow(user.getId(), entityType, entityId);

        return ForumUtil.getJSONString(0, "已取消关注!");

    }

    /**
     * 关注的人列表
     * @param userId
     * @param page
     * @param model
     * @return
     */
    @GetMapping(value = "/followees/{userId}")
    public String getFollowees(@PathVariable(value = "userId")Integer userId, Page page, Model model){

        User user = userService.findUserByUserId(userId);

        if(user == null){
            throw new RuntimeException("该用户不存在");
        }

        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/followees/"+userId);
        page.setRows(followService.findFolloweeCount(userId, ForumConstant.ENTITY_TYPE_USER).intValue());

        List<Map<String, Object>> userList = followService.findFollowees(userId, page.getOffset(), page.getLimit());

        if(userList != null){

            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", userList);

        return "site/followee";

    }


    @GetMapping(value = "/followers/{userId}")
    public String getFollowers(@PathVariable(value = "userId")Integer userId, Page page, Model model){

        User user = userService.findUserByUserId(userId);

        if(user == null){
            throw new RuntimeException("该用户不存在");
        }

        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/followers/"+userId);
        page.setRows(followService.findFolloweeCount(userId, ForumConstant.ENTITY_TYPE_USER).intValue());

        List<Map<String, Object>> userList = followService.findFollowers(userId, page.getOffset(), page.getLimit());

        if(userList != null){

            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }
        model.addAttribute("users", userList);

        return "site/follower";

    }

    private boolean hasFollowed(Integer userId){
        if(hostHolder.getUser() == null){
            return false;
        }
        return followService.hasFollowed(hostHolder.getUser().getId(), ForumConstant.ENTITY_TYPE_USER, userId);
    }



}
