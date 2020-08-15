package com.forum.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
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


    /**
     * 返回 json 格式的字符串
     * @param code
     * @param msg
     * @param map
     * @return
     */
    public static String getJSONString(Integer code, String msg, Map<String, Object> map){

        JSONObject json = new JSONObject();

        json.put("code", code);

        json.put("msg", msg);

        if(map != null){

            for (String key : map.keySet()) {

                json.put(key, map.get(key));

            }
        }

        return json.toJSONString();

    }

    public static String getJSONString(Integer code, String msg){

        return getJSONString(code, msg, null);

    }

    public static String getJSONString(Integer code){

        return getJSONString(code, null, null);

    }

//    public static void main(String[] args) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("name", "xx");
//        map.put("age", 18);
//        System.out.println(getJSONString(0, "ok", map));
//    }
}
