package com.forum.service.impl;

import com.forum.entity.DiscussPost;
import com.forum.mapper.DiscussPostMapper;
import com.forum.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "discussPostServiceImpl")
public class DiscussPostServiceImpl implements DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Override
    public List<DiscussPost> selectDiscussPosts(Integer userId, Integer offset, Integer limit) {

        return discussPostMapper.selectDiscussPosts(userId, offset, limit);

    }

    @Override
    public Integer selectDiscussPostRows(Integer userId) {

        return discussPostMapper.selectDiscussPostRows(userId);

    }
}
