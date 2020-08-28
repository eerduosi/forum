package com.forum.controller;

import com.forum.entity.User;
import com.forum.service.LikeService;
import com.forum.util.ForumUtil;
import com.forum.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {

    private LikeService likeService;

    @Autowired
    public void setLikeService(LikeService likeService) {
        this.likeService = likeService;
    }

    private HostHolder hostHolder;

    @Autowired
    public void setHostHolder(HostHolder hostHolder) {
        this.hostHolder = hostHolder;
    }

    @PostMapping(value = "/like")
    @ResponseBody
    public String like(Integer entityType, Integer entityId, Integer entityUserId){

        User user = hostHolder.getUser();

        /**
         * 点赞
         */
        likeService.like(user.getId(), entityType, entityId, entityUserId);

        /**
         * 数量
         */
        Long likeCount = likeService.findEntityLikeCount(entityType, entityId);

        /**
         * 状态
         */
        Integer likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        return ForumUtil.getJSONString(0, null, map);

    }
}
