package com.forum.service;

public interface LikeService {

    /**
     * 点赞
     * @param userId
     * @param entityType
     * @param entityId
     */
    void like(Integer userId, Integer entityType, Integer entityId, Integer entityUserId);

    /**
     * 查询实体的点赞数量
     * @param entityType
     * @param entityId
     * @return
     */
    Long findEntityLikeCount(Integer entityType, Integer entityId);

    /**
     * 查询某人对某实体的点赞状态
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    Integer findEntityLikeStatus(Integer userId, Integer entityType, Integer entityId);


    /**
     * 查询某个用户获得的赞
     * @param userId
     * @return
     */
    Integer findUserLikeCount(Integer userId);

}
