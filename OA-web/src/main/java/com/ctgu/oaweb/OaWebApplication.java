package com.ctgu.oaweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author Elm Forest
 */
@EnableOpenApi
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@MapperScan({"com.ctgu.oauser.mapper",
        "com.ctgu.oadepartment.mapper",
        "com.ctgu.oamessage.mapper",
        "com.ctgu.oadocument.mapper",
        "com.ctgu.oaschedule.mapper",
        "com.ctgu.oacheckingin.mapper"})
@SpringBootApplication
@ComponentScan(basePackages = {"com.ctgu"})
public class OaWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(OaWebApplication.class, args);
    }

}
