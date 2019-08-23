package com.mycompany.myapp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mycompany.myapp.dao.UserMapper;

import com.mycompany.myapp.pojo.User;
import com.mycompany.myapp.pojo.ChatList;

import com.mycompany.myapp.service.IUserService;

@Service("userService")
public class UserServiceImpl implements IUserService {
	@Resource
	private UserMapper userMapper;
	
	public User tryLogin(String chat_id, String password){
		return this.userMapper.selectByIdPwd(chat_id,password);
	}
	
	public User findUserByChatId(String chat_id){
		return this.userMapper.selectByChatId(chat_id);
	}
	
	public int updateName(String chat_id, String newname) {
		return this.userMapper.updateNameSelective(chat_id,newname);
	}
	
	public List<User> getUserById(String user_id) {
		return this.userMapper.selectById(user_id);
	}
	
	public List<User> getUserByIdMessage(String chat_id) {
		return this.userMapper.selectByIdMessage(chat_id);
	}
	
	public List<ChatList> getChatListByChatId(String chat_id) {
		return this.userMapper.selectChatList(chat_id);
	}

	public int updatePhotoById(String imgurl, String chat_id) {
		return this.userMapper.updatePhotoById(imgurl,chat_id);
	}
//	
//	public User selectByName(String username){
//		return this.userMapper.selectByName(username);
//	}
	
}
