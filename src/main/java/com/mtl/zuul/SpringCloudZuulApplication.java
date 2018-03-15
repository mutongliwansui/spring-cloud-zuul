package com.mtl.zuul;

import com.mtl.zuul.filter.GatewayFilter;
import com.netflix.zuul.ZuulFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

//@SpringBootApplication
//注意不是@EnableZuulServer
@EnableZuulProxy
//注意这里使用了更加简化的@SpringCloudApplication
@SpringCloudApplication
public class SpringCloudZuulApplication {
	@Bean
	public ZuulFilter gatewayFilter(){
		return new GatewayFilter();
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudZuulApplication.class, args);
	}
}
