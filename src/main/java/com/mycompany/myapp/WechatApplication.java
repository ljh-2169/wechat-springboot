package com.mycompany.myapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


//@Controller
//@RequestMapping("/hello")
//@EnableAutoConfiguration
//public class Demo1Application {
//	
//	public static void main(String[] args) {
//		SpringApplication.run(Demo1Application.class, args);
//	}
//}

@SpringBootApplication
@MapperScan("com.mycompany.myapp.dao")
public class WechatApplication {

	public static void main(String[] args) {
		SpringApplication.run(WechatApplication.class, args);
	}

}
