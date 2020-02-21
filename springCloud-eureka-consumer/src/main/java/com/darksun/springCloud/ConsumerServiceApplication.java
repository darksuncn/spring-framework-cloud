package com.darksun.springCloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class ConsumerServiceApplication {

	//必须使用 @LoadBalanced，否则无法根据 http://compute-service/hello 去寻找compute-service，
	//就会把 compute-service 当成一个 host 地址了
  @Bean
  @LoadBalanced
  RestTemplate restTemplate () {
      return new RestTemplate();
  }
	
  public static void main(String[] args) {
  	//new SpringApplicationBuilder(ComputeServiceApplication.class).web(WebApplicationType.NONE).run(args);
  	SpringApplication.run(ConsumerServiceApplication.class, args);
}	

}
