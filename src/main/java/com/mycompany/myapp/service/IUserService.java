package com.mycompany.myapp.service;

import java.util.List;

import com.mycompany.myapp.pojo.User;
import com.mycompany.myapp.pojo.ChatList;

public interface IUserService {
	
	public User tryLogin(String chat_id, String password);
	public User findUserByChatId(String chat_id);
	
	public int updateName(String chat_id, String newname);
	
	public List<User> getUserById(String user_id);
	
	public List<User> getUserByIdMessage(String chat_id);
	
	public List<ChatList> getChatListByChatId(String chat_id);
	
	public int updatePhotoById(String imgurl, String chat_id);
	
//	public User selectByName(String username);


}
