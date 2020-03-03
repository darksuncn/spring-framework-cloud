package com.darksun.springCloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@Value("${name}")
	String name;
	@Value("${age}")
	int age;

	@RequestMapping("/hello")
	public String hello(HttpServletRequest req) {
		
		String info = "url:" + req.getRequestURL().toString()+"/ip_host_user:"+req.getRemoteHost()+"_"+req.getRemoteUser()+"/port:"+req.getRemotePort();
		System.out.println("some one invoke this hello service from " + info);
		return "Hello Spring Boot["+info+"]! Your name is " + name + ", age is " + age;
	}


	@RequestMapping("/chat")
	public String chat(HttpServletRequest req) {
		
		String info = "url:" + req.getRequestURL().toString()+"/ip_host_user:"+req.getRemoteHost()+"_"+req.getRemoteUser()+"/port:"+req.getRemotePort();
		System.out.println("some one invoke this chat service from " + info);

		String msg = req.getParameter("msg");
		System.out.println("request /chat....msg="+msg);
		
		StringBuffer html = new StringBuffer();
		java.net.Socket sk = null;
		BufferedReader buf = null;
		try {
			sk = new java.net.Socket("127.0.0.1", 18081);
			buf = new BufferedReader(new InputStreamReader(sk.getInputStream()));
//			try {
//				String str = buf.readLine();
//				if(str != null)
//					html.append(str+"\r\n");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

			PrintWriter out = new PrintWriter(sk.getOutputStream(),true);
			out.println(msg);

			try {
				String str = buf.readLine();
				html.append(str+"\r\n");
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(sk != null) {
				try {
					sk.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println(html.toString());		
		
		
		return html.toString();
	}
	
	
	
}
