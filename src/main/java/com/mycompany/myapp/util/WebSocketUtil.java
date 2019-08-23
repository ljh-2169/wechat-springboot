package com.mycompany.myapp.util;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * 双工通信websocket工具类
 *
 */
@ServerEndpoint(value="/webSocket/{chatid}")
@Component

public class WebSocketUtil{
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static ConcurrentHashMap<String, WebSocketUtil> webSocketSet = new ConcurrentHashMap<>();
//    private static CopyOnWriteArraySet<WebSocketUtil> webSocketSet = new CopyOnWriteArraySet<WebSocketUtil>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String chatid = "";

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(@PathParam(value = "chatid") String chatid, Session session) {
        this.session = session;
        this.chatid = chatid;
        webSocketSet.put(chatid, this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("用户"+chatid+"加入！当前在线人数为" + getOnlineCount());
//        try {
//            sendMessage("您是第" + getOnlineCount() + "个双工通信的用户！");
//        } catch (IOException e) {
//            System.out.println("IO异常");
//        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(chatid, this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        JSONObject jsonObject = JSONObject.parseObject(message);
        String toId = jsonObject.getString("toId");
        //发送消息
        try {
        	sendtoUser(message,toId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 发送消息
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
//    	System.out.println(message);
    	this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }
    
    public void sendtoUser(String message,String toId) throws IOException {
    	System.out.println(webSocketSet.get(toId));
        if (webSocketSet.get(toId) == null) {
        	System.out.println("用户不在线");
        }else {
        	webSocketSet.get(toId).sendMessage(message);
        }
    }


//    /**
//     * 群发自定义消息
//     * */
//    public static void sendInfo(String message) throws IOException {
//        for (WebSocketUtil item : webSocketSet) {
//            try {
//            	System.out.println("群发消息");
//                item.sendMessage(message);
//            } catch (IOException e) {
//                continue;
//            }
//        }
//    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketUtil.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketUtil.onlineCount--;
    }
}