package com.forum.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class ForumUtil {

    /**
     *
     * 生成随机字符串
     *
     */
    public static String generateUUID(){
        /**
         * 生成 UUID 并替换其中的横线
         */
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     *
     * MD5 + salt 加密
     *
     * @param key
     *
     * @return
     *
     */
    public static String md5(String key){

        /**
         * 若为空值 , 返回 null
         */
        if(StringUtils.isBlank(key)){
            return null;
        }

        /**
         * 若为不空值 , 返回 十六进制 md5 加密后的结果
         */
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

}
