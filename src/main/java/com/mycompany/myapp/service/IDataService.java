package com.mycompany.myapp.service;

import java.util.List;

//import com.mycompany.myapp.pojo.Chatlist;
//import com.mycompany.myapp.pojo.Chatroom;
import com.mycompany.myapp.pojo.Message;
import com.mycompany.myapp.pojo.Address;

public interface IDataService {
	
//	public List<Chatlist> getChatlist();
//	
//	public List<Chatroom> getChatroom(String chatid);
	
	public List<Message> getMessageByFromToId(String user_id, String friend_id);
	
//	public int insertChatroom(Chatroom record);
	
	public int insertMessage(Message record);
	
	public int updateMessageUnread(Integer unread, String user_id, String friend_id);
	
	public List<String> getFriendId(String user_id);
	
	public List<Address> getAddressById(String user_id);
}


