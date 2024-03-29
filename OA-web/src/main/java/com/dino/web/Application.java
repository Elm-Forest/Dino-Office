package com.dino.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 启动类
 *
 * @author Elm Forest
 */
@EnableAsync
@EnableOpenApi
@EnableWebSocket
@EnableScheduling
@EnableTransactionManagement
@MapperScan(basePackages = {"com.dino.common.dao"})
@ComponentScan(basePackages = {"com.dino"})
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
