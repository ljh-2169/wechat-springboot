package com.mycompany.myapp.util;

import java.io.PrintWriter;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mycompany.myapp.pojo.User;
import com.mycompany.myapp.service.IUserService;
import com.mycompany.myapp.util.JwtUtil;


@Component
public class LoginHandlerIntercepter implements HandlerInterceptor {  
	
	@Resource
	private IUserService userService;
    
    @Override  
    public void afterCompletion(HttpServletRequest request,  
            HttpServletResponse response, Object arg2, Exception arg3)  
            throws Exception {  
    }  
  
    @Override  
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,  
            Object arg2, ModelAndView arg3) throws Exception {  
  
    }  
    @Override  
    public boolean preHandle(HttpServletRequest request, HttpServletResponse arg1,  
            Object arg2) throws Exception {  
           String requestURI = request.getRequestURI(); 
             if(requestURI.indexOf("log")<0 & requestURI.indexOf("refreshTokenlog")<0){  
                 //访问除登录外的页面  
            	String token = request.getHeader("token");
            	String chat_id = JWT.decode(token).getAudience().get(0);	    	   	
    	    	User user = userService.findUserByChatId(chat_id);
             	JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
                 try {
                     jwtVerifier.verify(token);
                 } catch (Exception e) {
                	 System.out.println("token验证错误：" + e.getMessage());
                	 String errorSignature = "The Token's Signature resulted invalid when verified using the Algorithm: HmacSHA256";
                	 if(e.getMessage().equals(errorSignature)) {
                		 System.out.println("token签名错误");
             	    	 arg1.setStatus(401);
                         throw new RuntimeException("401");
                	 }
                	 Date lasttime = new Date(0);
                	 if(request.getSession().getAttribute("lasttime")!=null) {
                		 lasttime = (Date) request.getSession().getAttribute("lasttime");	    
                	 }
             	     Date nowdate = new Date();
             	     long diff = nowdate.getTime()-lasttime.getTime();
             	     System.out.println("lasttime:" + lasttime + " nowdate:" + nowdate);
             	     System.out.println("时间差" + diff);
             	     if(diff<60000) {
            	    	String newtoken = JwtUtil.getToken(user);
						System.out.println("newtoken:" + newtoken);
						arg1.addHeader("newtoken", newtoken);
            	     }else {
            	    	System.out.println("刷新Token超时");
            	    	arg1.setStatus(401);
                        throw new RuntimeException("401");
            	     }    
                 }
//                 System.out.println(jwtVerifier.verify(token).getExpiresAt());
                 Date date = new Date();
                 request.getSession().setAttribute("lasttime", date);
                 request.getSession().setAttribute("user", chat_id);
//                 System.out.println("最后一次请求时间:" + date);
//                 System.out.println("session——ID:" + request.getSession().getId());
                 return true;           	  
             }else{  
                 return true;  
             }  
    }  
  
}  