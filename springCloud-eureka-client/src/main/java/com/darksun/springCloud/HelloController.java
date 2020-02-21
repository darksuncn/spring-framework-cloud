package com.darksun.springCloud;

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
  public String hello() {
      return "Hello Spring Boot! Your name is "+name+", age is "+age;
  }	
	
}
