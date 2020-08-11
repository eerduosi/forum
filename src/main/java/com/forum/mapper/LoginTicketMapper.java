package com.forum.mapper;

import com.forum.entity.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface LoginTicketMapper {

    /**
     * 数据表中添加登录凭证
      * @param loginTicket
     * @return
     */
    @Insert({"insert into login_ticket(user_id, ticket, status, expired) values(#{userId}, #{ticket}, #{status}, #{expired})"})
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insertLoginTicket(LoginTicket loginTicket);

    /**
     * 查询登录凭证
     */
    @Select("select id, userId, ticket, status, expired from login_ticket where ticket = #{ticket} ")
    LoginTicket selectByTicket(String ticket);

    /**
     * 修改登录凭证状态
     */
    @Update("update login_ticket set status = #{status} where ticket = #{ticket}")
    Integer updateStatus(String ticket, Integer status);

}
