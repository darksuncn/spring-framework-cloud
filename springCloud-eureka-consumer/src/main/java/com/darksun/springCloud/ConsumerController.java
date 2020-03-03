package com.darksun.springCloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping(value = "/consumer", method = RequestMethod.GET)
	public String helloConsumer() {
		return restTemplate.getForEntity("http://compute-service/hello", String.class).getBody();
	}

	@RequestMapping(value = "/chat", method = RequestMethod.GET)
	public String chatConsumer() {
		StringBuffer html = new StringBuffer();
		html.append("<script>\r\n");
		InputStream is = ConsumerController.class.getResourceAsStream("/jquery-2.1.1.js");
		int r;
		byte[] buff = new byte[1024];
		try {
			while ((r = is.read(buff)) > -1) {
				if (r == 1024) {
					html.append(new String(buff));
				} else if (r > 0) {
					byte[] cp = new byte[r];
					System.arraycopy(buff, 0, cp, 0, r);
					html.append(new String(cp));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		html.append("\r\n");
		html.append("function send(){\r\n" + "	var msgTxt = document.getElementById(\"msg\").value;\r\n	//alert(msgTxt);\r\n" + "	jQuery.ajax({url:\"/sendChat?msg=\"+msgTxt,async:false,success : function(result) {\r\n"
		    + "      var shtml = $(\"#disp\").html();\r\n" + 
		    "      $(\"#disp\").html(shtml+\"<br/>\"+result);\r\n" + "     }});\r\n" + "}");
		
		html.append("\r\n");
		html.append("function clearDisp(){\r\n$(\"#disp\").html(\"Dynamic Text Below<br>\");}");
		
		
		html.append("\r\n</script>\r\n");

		html.append("\r\n");
		html.append("<input type=\"text\" name=\"msg\" id=\"msg\">&nbsp;<input type=\"button\" value=\"Send\" onclick=\"send()\">&nbsp;<input type=\"button\" value=\"Clear\" onclick=\"clearDisp()\">\r\n<br><br>\r\n" + 
				"<div id=\"disp\" style=\"border: 1px solid red;width:400px;\">Dynamic Text Below<br/></div>");

		return html.toString();
	}

	@RequestMapping(value = "/sendChat", method = RequestMethod.GET)
	public String sendConsumer(HttpServletRequest req) {
		String msg = req.getParameter("msg");
		System.out.println("request /sendChat....msg="+msg);
		
		String res = restTemplate.getForEntity("http://compute-service/chat?msg="+msg, String.class).getBody();

		System.out.println("invoke chat service response..."+res);
		
		return res;

	}

}
