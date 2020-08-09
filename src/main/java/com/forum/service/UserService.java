package com.forum.service;

import com.forum.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserService {
    /**
     * 依据 id 查找 user
     * @param id
     * @return
     */
    User selectUserByUserId(Integer id);

    /**
     * 依据 name 查找 user
     * @param username
     * @return
     */
    User selectUserByUserName(String username);

    /**
     * 依据 email 查找 user
     * @param email
     * @return
     */
    User selectUserByUserEmail(String email);

    /**
     * 新增 user
     * @param user
     * @return
     */
    Integer insertUser(User user);

    /**
     * 依据 user 的 id 更新 user 的 status
     * @param id
     * @param status
     * @return
     */
    Integer updateUserStatusByUserId(@Param(value = "id") Integer id, @Param(value = "status") Integer status);

    /**
     * 依据 user 的 id 更新 user 的 headerUrl
     * @param id
     * @param headerUrl
     * @return
     */
    Integer updateUserHeaderUrlByUserId(@Param(value = "id")Integer id, @Param(value = "headerUrl")String headerUrl);

    /**
     * 依据 user 的 id 更新 user 的 password
     * @param id
     * @param password
     * @return
     */
    Integer updateUserPasswordByUserId(@Param(value = "id")Integer id, @Param(value = "password")String password);
}
