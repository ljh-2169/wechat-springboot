package com.mycompany.myapp.util;

import java.util.Calendar;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mycompany.myapp.pojo.User;

public class JwtUtil {
	public static String getToken(User user) {
        String token="";
        Date iatDate = new Date();
        System.out.println("token签发过期时间:" + iatDate);
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 20);
        Date expiresDate = nowTime.getTime();
        System.out.println("token过期时间:" + expiresDate);

        token= JWT.create().withAudience(user.getChatId()).withIssuedAt(iatDate).withExpiresAt(expiresDate)
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

}
