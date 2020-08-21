package com.forum.service.impl;

import com.forum.entity.Comment;
import com.forum.mapper.CommentMapper;
import com.forum.service.CommentService;
import com.forum.service.DiscussPostService;
import com.forum.util.ForumConstant;
import com.forum.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service(value = "commentServiceImpl")
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;

    @Override
    public List<Comment> findCommentsByEntity(Integer entityType, Integer entityId, Integer offset, Integer limit) {
        return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
    }

    @Override
    public Integer findCommentCount(Integer entityType, Integer entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public Integer addComment(Comment comment) {

        if(comment == null){
            throw new IllegalArgumentException("参数不能为空");
        }

        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));

        comment.setContent(sensitiveFilter.filter(comment.getContent()));

        Integer rows = commentMapper.insertComment(comment);

        /**
         * 更新评论数量 comment_count
         */
        if(comment.getEntityType().equals(ForumConstant.ENTITY_TYPE_POST)) {

            Integer count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());

            discussPostService.updateCommentCount(comment.getEntityId(), count);

        }

        return rows;
    }
}
