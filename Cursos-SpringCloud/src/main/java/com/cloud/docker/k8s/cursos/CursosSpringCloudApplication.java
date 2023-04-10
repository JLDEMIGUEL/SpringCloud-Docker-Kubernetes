package com.cloud.docker.k8s.cursos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CursosSpringCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(CursosSpringCloudApplication.class, args);
	}

}
