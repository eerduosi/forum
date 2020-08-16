package com.forum.mapper;

import com.forum.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {

    /**
     * 查找评论实体
     * @param entityType
     * @param entityId
     * @param offset
     * @param limit
     * @return
     */
    List<Comment> selectCommentByEntity(Integer entityType, Integer entityId, Integer offset, Integer limit);

    /**
     * 查找评论数量
     * @param entityType
     * @param entityId
     * @return
     */
    Integer selectCountByEntity(Integer entityType, Integer entityId);

}
