package com.forum.service.impl;

import com.forum.service.LikeService;
import com.forum.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service(value = "likeServiceImpl")
public class LikeServiceImpl implements LikeService {

    private RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 点赞业务
     *
     * @param userId
     * @param entityType
     * @param entityId
     */
    @Override
    public void like(Integer userId, Integer entityType, Integer entityId, Integer entityUserId) {

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {

                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);

                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);

                //判断是否已经存在 redis 中
                Boolean member = redisOperations.opsForSet().isMember(entityLikeKey, userId);

                //开启事务
                redisOperations.multi();

                if (member) {

                    //存在则移除
                    redisOperations.opsForSet().remove(entityLikeKey, userId);

                    redisOperations.opsForValue().decrement(userLikeKey);

                } else {

                    //不存在则添加
                    redisOperations.opsForSet().add(entityLikeKey, userId);

                    redisOperations.opsForValue().increment(userLikeKey);

                }

                //执行命令
                return redisOperations.exec();
            }
        });

    }

    /**
     * 查询实体的点赞数量
     *
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public Long findEntityLikeCount(Integer entityType, Integer entityId) {

        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);

        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    /**
     * 查询某人对某实体的点赞状态
     *
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public Integer findEntityLikeStatus(Integer userId, Integer entityType, Integer entityId) {

        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);

        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }

    /**
     * 查询某个用户获得的赞
     * @param userId
     * @return
     */
    @Override
    public Integer findUserLikeCount(Integer userId){

        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);

        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);

        return count == null ? 0 : count.intValue();
    }
}
