package com.mycompany.myapp.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mycompany.myapp.pojo.ChatList;
//import com.mycompany.myapp.pojo.Chatroom;
import com.mycompany.myapp.pojo.Message;
import com.mycompany.myapp.pojo.User;
import com.mycompany.myapp.pojo.Address;

import com.mycompany.myapp.util.JwtUtil;

import com.mycompany.myapp.service.IUserService;
import com.mycompany.myapp.service.IDataService;

@RestController
public class HelloController {
	
	//private agentObjMapper agentMapper;
	
	@Resource
	private IUserService userService;
	
	@Resource
	private IDataService dataService = null;

    @RequestMapping("/")
    public String hello(){
        return "Welcome!";
    }
    
@RequestMapping(value ="/refreshToken",method = RequestMethod.POST)   
    public String refreshToken(HttpServletRequest request, @RequestBody(required = false) JSONObject jsonParam){
	    Date lasttime = (Date) request.getSession().getAttribute("lasttime");	    
	    Date nowdate = new Date();
	    long diff = nowdate.getTime()-lasttime.getTime();
	    System.out.println("lasttime:" + lasttime + " nowdate:" + nowdate);
	    System.out.println("时间差" + diff);
	    
	    JSONObject rspJson = new JSONObject();
	    if(diff<60000) {
	    	String chat_id = jsonParam.getString("chat_id");	    	   	
	    	User user = userService.findUserByChatId(chat_id);
	    	if(user!=null){ 
	    		JwtUtil jwt = new JwtUtil();
	    		String token = JwtUtil.getToken(user);
	    		System.out.println(token);
	    		rspJson.put("token", token);
	    		rspJson.put("status", "sucess");
	    	}else {
	    		rspJson.put("status", "error");
	    	}
	    }else {
	    	System.out.println("刷新Token超时");
	    	rspJson.put("status", "error");
	    }
	    return rspJson.toJSONString();
    }
    
    @RequestMapping(value ="/log",method = RequestMethod.POST)
    public String login(@RequestBody(required = false) JSONObject jsonParam){
    	String chat_id = jsonParam.getString("chat_id");
    	String password = jsonParam.getString("password");
    	JSONObject rspJson = new JSONObject();   	
    	User user = userService.tryLogin(chat_id, password);
    	if(user!=null){ 
    		JwtUtil jwt = new JwtUtil();
    		String token = jwt.getToken(user);
    		System.out.println(token);
    		rspJson.put("token", token);
    		rspJson.put("status", "sucess");
    		rspJson.put("chat_name", user.getChatName());
    		rspJson.put("imgurl", user.getImgurl());
    	}else {
    		rspJson.put("status", "error");
    	}
    	return rspJson.toJSONString();
    }
    
    @RequestMapping(value ="/newMsg",method = RequestMethod.GET)
    public String newMsg(HttpServletRequest request, HttpServletResponse response){ 
    	String chat_id = request.getParameter("chat_id");
    	User user = userService.findUserByChatId(chat_id);
    	JSONObject rspJson = new JSONObject();
    	rspJson.put("data", user);
    	return rspJson.toJSONString();
    }
    
    @RequestMapping(value ="/chat_list",method = RequestMethod.GET)
    public String chatList(HttpServletRequest request, HttpServletResponse response){ 
    	String chat_id = request.getParameter("chat_id");
//    	List<User> chatlist = userService.getUserByIdMessage(chat_id);
    	List<ChatList> chatlist = userService.getChatListByChatId(chat_id);
    	
//    	List<Chatlist> chatlist = dataService.getChatlist();
    	JSONObject rspJson = new JSONObject();
    	rspJson.put("data", chatlist);
//        System.out.println(chatlist.get(0).getTime());
    	return rspJson.toJSONString();
    }
    
    @RequestMapping(value ="/setunread",method = RequestMethod.POST)
    public String setunread(@RequestBody(required = false) JSONObject jsonParam){
    	String user_id = jsonParam.getString("user_id");
    	String friend_id = jsonParam.getString("friend_id");
    	int unread = 0;
    	int i = dataService.updateMessageUnread(unread,user_id,friend_id); 
    	JSONObject rspJson = new JSONObject();
    	if (i != 0) {
    		rspJson.put("status", "success");
    	}else {
    		rspJson.put("status", "error");
    	}
    	return rspJson.toJSONString();
    }
    
    @RequestMapping(value ="/chatroom",method = RequestMethod.POST)
    public String chatroom(@RequestBody(required = false) JSONObject jsonParam){
    	String user_id = jsonParam.getString("user_id");
    	String friend_id = jsonParam.getString("friend_id");
    	List<Message> message = dataService.getMessageByFromToId(user_id,friend_id);
//    	System.out.println(message.get(0).getTime());
    	JSONObject rspJson = new JSONObject();
    	rspJson.put("data", message);
    	return rspJson.toJSONString();
    }
    
    @RequestMapping(value ="/chatroom",method = RequestMethod.GET)
    public String chatroomInsert(HttpServletRequest request, HttpServletResponse response){
    	String fromId = request.getParameter("fromId");
    	String toId = request.getParameter("toId"); 
    	String type = request.getParameter("type");
    	String content = request.getParameter("content");
    	Message message = new Message();
    	message.setFromId(fromId);
    	message.setToId(toId);
    	message.setType(type);
    	message.setContent(content);
    	int i = dataService.insertMessage(message);
    	JSONObject rspJson = new JSONObject();
    	if (i == 1) {
    		rspJson.put("status", "success");
    	}else {
    		rspJson.put("status", "error");
    	}
    	return rspJson.toJSONString();
    }
    
    @RequestMapping(value ="/sendMessage",method = RequestMethod.POST)
    public String sendMessage(@RequestParam("fromId") String fromId, @RequestParam("toId") String toId, 
    		@RequestParam("file") MultipartFile file, @RequestParam("path") String path)throws IOException {
    	String fileName = file.getOriginalFilename();
    	BASE64Encoder encoder = new BASE64Encoder();
        String imgData = encoder.encode(file.getBytes());
        
        String imgurl = "/static/messageImg/" + fileName;
    	Message message = new Message();
    	message.setFromId(fromId);
    	message.setToId(toId);
    	message.setType("img");
    	message.setImgurl(imgurl);
    	int a = dataService.insertMessage(message);
    	JSONObject rspJson = new JSONObject();
    	if (a == 1) {
    		rspJson.put("status", "success");
    	}else {
    		rspJson.put("status", "error");
    	}

    	BASE64Decoder decoder = new BASE64Decoder();
        String filePath = path+fileName;
        System.out.println(filePath);
        byte[] b = decoder.decodeBuffer(imgData);
        for (int i = 0; i < b.length; ++i) {       	
        	if (b[i] < 0) {
        		b[i] += 256;
        	}
        }
        OutputStream out = new FileOutputStream(filePath);
        out.write(b);
        out.flush();
        out.close();
        rspJson.put("filePath", imgurl);
        return rspJson.toJSONString();
    }
    
    @RequestMapping(value ="/address",method = RequestMethod.GET)
    public String address(HttpServletRequest request, HttpServletResponse response){
//    	String token = request.getHeader("token");
//    	JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("123")).build();
//        try {
//            jwtVerifier.verify(token);
//        } catch (JWTVerificationException e) {
//            throw new RuntimeException("401");
//        }
    	String user_id = request.getParameter("chat_id");
    	List<User> friends = userService.getUserById(user_id);
    	JSONObject rspJson = new JSONObject();
    	rspJson.put("data", friends);
    	return rspJson.toJSONString();
    }
    
    @RequestMapping(value ="/alterName",method = RequestMethod.POST)
    
    public String alterName(@RequestBody(required = false) JSONObject jsonParam){
    	String chat_id = jsonParam.getString("chat_id");
    	String newname = jsonParam.getString("newname");
    	JSONObject rspJson = new JSONObject();
    	
    	int i = userService.updateName(chat_id, newname);
    	if(i == 1){
    		rspJson.put("status", "sucess");
    	}else {
    		rspJson.put("status", "error");
    	}
    	
    	return rspJson.toJSONString();
    }
    
    @RequestMapping(value ="/alterPhoto",method = RequestMethod.POST)
    public String alterPhoto(@RequestParam("file") MultipartFile file, @RequestParam("chat_id") String chat_id,
    		@RequestParam("path") String path)throws IOException {
    	System.out.println(chat_id);
    	String fileName = file.getOriginalFilename();
    	BASE64Encoder encoder = new BASE64Encoder();
        String imgData = encoder.encode(file.getBytes());
        
        JSONObject rspJson = new JSONObject();
//        String username = "123";
        String imgurl = "/static/" + fileName;
        int a = userService.updatePhotoById(imgurl,chat_id);
    	if(a == 1){
    		rspJson.put("status", "success");
    	}else {
    		rspJson.put("status", "error");
    	}
    	
//    	User user = userService.selectByName(username);

    	BASE64Decoder decoder = new BASE64Decoder();
        String filePath = path+fileName;
        System.out.println(filePath);
        byte[] b = decoder.decodeBuffer(imgData);
        for (int i = 0; i < b.length; ++i) {       	
        	if (b[i] < 0) {
        		b[i] += 256;
        	}
        }
        OutputStream out = new FileOutputStream(filePath);
        out.write(b);
        out.flush();
        out.close();
        rspJson.put("filePath", imgurl);
        return rspJson.toJSONString();
    }
    
//    @RequestMapping("/test")
//    public agentObj agentGet(){
//    	agentObj record = new agentObj();
//    	Page p = new Page();
//    	//return agentMapper.selectByQueryObj(record, p);
//        return agentMapper.selectByPrimaryKey(2);
//    }

}