package com.offcn.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 //开启swagger2自动生成api文档的功能
public class AppSwaggerConfig {

    @Bean("订单模块")
    public Docket createDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("订单模块")
                .apiInfo(createApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.offcn.order.controller"))
                .paths(PathSelectors.any())
                .build();
    }


    public ApiInfo createApiInfo(){
        return new ApiInfoBuilder()
                .title("七易众筹-订单平台接口文档")
                .description("提供订单模块的文档说明")
                .version("1.0")
                .build();
    }
}
