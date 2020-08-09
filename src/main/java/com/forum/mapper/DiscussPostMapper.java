package com.forum.mapper;

import com.forum.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DiscussPostMapper {

    /**
     * 使用动态 sql 语句实现
     *
     *  1.userId != 0 时查询指定用户帖子
     *  2.userId == 0 时查询全部帖子
     *
     * @param userId
     * @return
     */
    List<DiscussPost> selectDiscussPosts(@Param(value = "userId") Integer userId, @Param(value = "offset")Integer offset, @Param(value = "limit")Integer limit);

    /**
     * 使用动态 sql 语句实现
     *
     *  1.userId != 0 时查询用户帖子的总数
     *  2.userId == 0 时查询全部帖子的总数
     *
     * @param userId
     * @return
     */
    Integer selectDiscussPostRows(@Param(value = "userId")Integer userId);

}
