package com.forum.service;

import java.util.List;
import java.util.Map;

public interface FollowService {

    void follow(Integer userId, Integer entityType, Integer entityId);

    void unfollow(Integer userId, Integer entityType, Integer entityId);

    Long findFolloweeCount(Integer userId, Integer entityType);

    Long findFollowerCount(Integer entityType, Integer entityId);

    Boolean hasFollowed(Integer userId, Integer entityType, Integer entityId);

    List<Map<String, Object>> findFollowees(Integer userId, Integer offset, Integer limit);

    List<Map<String, Object>> findFollowers(Integer userId, Integer offset, Integer limit);

}
