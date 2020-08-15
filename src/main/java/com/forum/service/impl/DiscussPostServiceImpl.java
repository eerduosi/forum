package com.forum.service.impl;

import com.forum.entity.DiscussPost;
import com.forum.mapper.DiscussPostMapper;
import com.forum.service.DiscussPostService;
import com.forum.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service(value = "discussPostServiceImpl")
public class DiscussPostServiceImpl implements DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public List<DiscussPost> selectDiscussPosts(Integer userId, Integer offset, Integer limit) {

        return discussPostMapper.selectDiscussPosts(userId, offset, limit);

    }

    @Override
    public Integer selectDiscussPostRows(Integer userId) {

        return discussPostMapper.selectDiscussPostRows(userId);

    }

    @Override
    public Integer addDiscussPost(DiscussPost discussPost) {

        if(discussPost == null){

            throw new IllegalArgumentException("参数不能为空");

        }

        //转移 HTML 标记
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));

        /**
         * 过滤敏感词
         */
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));

        return discussPostMapper.insertDiscussPost(discussPost);

    }
}
