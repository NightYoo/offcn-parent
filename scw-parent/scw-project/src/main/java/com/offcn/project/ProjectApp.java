package com.offcn.project;

import com.offcn.common.util.OssTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableEurekaClient
@EnableSwagger2 // 开启swagger2
@MapperScan("com.offcn.project.mapper")
public class ProjectApp {

    public static void main(String[] args) {

        SpringApplication.run(ProjectApp.class);
    }


}
