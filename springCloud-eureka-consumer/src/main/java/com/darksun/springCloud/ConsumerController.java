package com.darksun.springCloud;

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
	
}
