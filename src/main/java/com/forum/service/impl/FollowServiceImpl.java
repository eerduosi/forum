package com.forum.service.impl;

import com.forum.entity.User;
import com.forum.service.FollowService;
import com.forum.service.UserService;
import com.forum.util.ForumConstant;
import com.forum.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

@Service(value = "followServiceImpl")
public class FollowServiceImpl implements FollowService {

    private RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 关注用户
     * @param userId
     * @param entityType
     * @param entityId
     */
    @Override
    public void follow(Integer userId, Integer entityType, Integer entityId) {

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {

                //构造被关注者key
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

                //构造粉丝key
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

                //开启redis事务
                redisOperations.multi();

                redisOperations.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());

                redisOperations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());

                //执行事务
                return redisOperations.exec();

            }
        });

    }

    /**
     * 取关用户
     * @param userId
     * @param entityType
     * @param entityId
     */
    @Override
    public void unfollow(Integer userId, Integer entityType, Integer entityId) {

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {

                //构造被关注者key
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

                //构造粉丝key
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

                //开启redis事务
                redisOperations.multi();

                redisOperations.opsForZSet().remove(followeeKey, entityId);

                redisOperations.opsForZSet().remove(followerKey, userId);

                //执行redis事务
                return redisOperations.exec();

            }
        });
    }

    /**
     * 查询关注的实体的数量
     * @param userId
     * @param entityType
     * @return
     */
    @Override
    public Long findFolloweeCount(Integer userId, Integer entityType){

        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

        return redisTemplate.opsForZSet().zCard(followeeKey);

    }

    /**
     * 查询粉丝的数量
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public Long findFollowerCount(Integer entityType, Integer entityId){

        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

        return redisTemplate.opsForZSet().zCard(followerKey);

    }

    /**
     * 查询当前用户是否被关注
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    @Override
    public Boolean hasFollowed(Integer userId, Integer entityType, Integer entityId){

        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

        return redisTemplate.opsForZSet().score(followeeKey, entityId) != null;

    }

    /**
     * 查询某用户关注的人
     */
    @Override
    public List<Map<String, Object>> findFollowees(Integer userId, Integer offset, Integer limit) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, ForumConstant.ENTITY_TYPE_USER);

        //倒序查询
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, offset, offset + limit - 1);

        if(targetIds == null){
            return null;
        }

        List<Map<String, Object>> list = new ArrayList<>();

        for (Integer targetId : targetIds) {
            Map<String, Object>  map = new HashMap<>();

            User user = userService.findUserByUserId(targetId);

            map.put("user", user);

            Double score = redisTemplate.opsForZSet().score(followeeKey, targetId);

            map.put("followTime", new Date(score.longValue()));

            list.add(map);
        }

        return list;

    }

    /**
     * 查询某用户的粉丝
     */
    @Override
    public List<Map<String, Object>> findFollowers(Integer userId, Integer offset, Integer limit) {
        String followerKey = RedisKeyUtil.getFollowerKey(ForumConstant.ENTITY_TYPE_USER, userId);

        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset + limit - 1);

        if(targetIds == null){

            return null;
        }

        List<Map<String, Object>> list = new ArrayList<>();

        for (Integer targetId : targetIds) {
            Map<String, Object>  map = new HashMap<>();

            User user = userService.findUserByUserId(targetId);

            map.put("user", user);

            Double score = redisTemplate.opsForZSet().score(followerKey, targetId);

            map.put("followTime", new Date(score.longValue()));

            list.add(map);
        }

        return list;

    }
}
