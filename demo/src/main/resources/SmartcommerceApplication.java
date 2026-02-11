package com.smartcommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SmartcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartcommerceApplication.class, args);
	}

}
