package com.forum.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";

    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    private static final String PREFIX_USER_LIKE = "like:user";

    //某个实体的赞
    public static String getEntityLikeKey(Integer entityType, Integer entityId){

        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;

    }

    //某个用户的赞
    public static String getUserLikeKey(Integer userId){

        return PREFIX_USER_LIKE + SPLIT + userId;

    }

}
