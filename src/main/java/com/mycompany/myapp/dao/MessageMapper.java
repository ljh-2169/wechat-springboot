package com.mycompany.myapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

//import com.mycompany.myapp.pojo.Chatroom;
import com.mycompany.myapp.pojo.Message;

public interface MessageMapper {
    int insert(Message record);

    int insertSelective(Message record);
    
    List<Message> selectByFromToId(@Param("friend_id") String friend_id,@Param("user_id") String user_id);
    
    int updateMessageUnread(@Param("unread") Integer unread,@Param("user_id") String user_id,@Param("friend_id") String friend_id);
    
}