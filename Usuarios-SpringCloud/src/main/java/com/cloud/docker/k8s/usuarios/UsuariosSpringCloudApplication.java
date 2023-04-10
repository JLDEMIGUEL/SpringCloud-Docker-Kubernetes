package com.cloud.docker.k8s.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class UsuariosSpringCloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsuariosSpringCloudApplication.class, args);
    }

}
