package com.mycompany.myapp.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


//import com.mycompany.myapp.dao.ChatlistMapper;
//import com.mycompany.myapp.dao.ChatroomMapper;
import com.mycompany.myapp.dao.MessageMapper;
import com.mycompany.myapp.dao.AddressMapper;

//import com.mycompany.myapp.pojo.Chatlist;
//import com.mycompany.myapp.pojo.Chatroom;
import com.mycompany.myapp.pojo.Message;
import com.mycompany.myapp.pojo.Address;

import com.mycompany.myapp.service.IDataService;


/**
 * @author ljh
 * 处理数据
 */
@Service("dataService")
public class DataServiceImpl implements IDataService {
	
//	@Resource
//	private ChatlistMapper chatlistMapper;
//	
//	@Resource
//	private ChatroomMapper chatroomMapper;
	
	@Resource
	private MessageMapper messageMapper;
	
	@Resource
	private AddressMapper addressMapper;

//	@Override
//	public List<Chatlist> getChatlist() {
//		// TODO Auto-generated method stub
//		return chatlistMapper.selectAll();
//	}
//	
//	@Override
//	public List<Chatroom> getChatroom(String chatid) {
//		return chatroomMapper.selectByChatid(chatid);
//	}
	
	@Override
	public List<Message> getMessageByFromToId(String user_id, String friend_id) {
		return messageMapper.selectByFromToId(user_id,friend_id);
	}
	
//	@Override
//	public int insertChatroom(Chatroom record) {
//		return chatroomMapper.insertNewMessage(record);
//	}
	
	@Override
	public int insertMessage(Message record) {
		return messageMapper.insertSelective(record);
	}
	
	@Override
	public int updateMessageUnread(Integer unread, String user_id, String friend_id) {
		return messageMapper.updateMessageUnread(unread,user_id,friend_id);
	}
	
	@Override
	public List<String> getFriendId(String user_id) {
		return addressMapper.selectFriendId(user_id);
	}
	
	@Override
	public List<Address> getAddressById(String user_id){
		return addressMapper.selectAddressById(user_id);
	}
}
