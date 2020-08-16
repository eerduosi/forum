package com.forum.service;

import com.forum.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DiscussPostService {

    /**
     * 查询贴子对象
     *
     *  1.userId != 0 时查询指定用户帖子
     *  2.userId == 0 时查询全部帖子
     *
     * @param userId
     * @return
     */
    List<DiscussPost> selectDiscussPosts(@Param(value = "userId") Integer userId, @Param(value = "offset")Integer offset, @Param(value = "limit")Integer limit);

    /**
     * 查询贴子数
     *
     *  1.userId != 0 时查询用户帖子的总数
     *  2.userId == 0 时查询全部帖子的总数
     *
     * @param userId
     * @return
     */
    Integer selectDiscussPostRows(@Param(value = "userId")Integer userId);

    /**
     * 增加帖子
     *
     * @param discussPost
     *
     * @return
     */
    public Integer addDiscussPost(DiscussPost discussPost);

    /**
     * 查找帖子
     *
     * @param id : discussPost id
     *
     * @return
     *
     */
    DiscussPost findDiscussPostById(Integer id);
}
