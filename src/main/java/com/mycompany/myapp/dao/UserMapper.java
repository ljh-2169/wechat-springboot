package com.mycompany.myapp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mycompany.myapp.pojo.User;
import com.mycompany.myapp.pojo.ChatList;

public interface UserMapper {
    int insert(User record);

    int insertSelective(User record);
    
    User selectByIdPwd(@Param("chat_id") String chat_id, @Param("password") String password);
    User selectByChatId(@Param("chat_id") String chat_id);
    
    int updateNameSelective(@Param("chat_id") String chat_id, @Param("newname") String newname);
    
    List<User> selectById(@Param("user_id") String user_id);
    
    List<User> selectByIdMessage(@Param("chat_id") String chat_id); 
    List<ChatList> selectChatList(@Param("chat_id") String chat_id);
    
    int updatePhotoById(@Param("imgurl") String imgurl, @Param("chat_id") String chat_id);
}