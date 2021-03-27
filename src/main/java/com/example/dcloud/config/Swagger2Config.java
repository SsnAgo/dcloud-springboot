package com.example.dcloud.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 访问某个包下的requestHandler
                .apis(RequestHandlerSelectors.basePackage("com.example.dcloud.controller"))
                // 可以访问任何路径
                .paths(PathSelectors.any())
                .build()
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }

    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("DCloud接口文档")
                .description("DCloud接口文档")
                .contact(new Contact("ssn", "http:localhost:8083/doc.html", "ssn@123.com "))
                .version("1.0")
                .build();
    }

    private List<SecurityContext> securityContexts(){
        List<SecurityContext> res = new ArrayList<>();
        // 设置不需要认证就可以访问的接口
        res.add(getContextByPath("/hello/.*"));
        return res;
    }

    private SecurityContext getContextByPath(String regex) {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex(regex)).build();
    }

    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> res = new ArrayList<>();
        AuthorizationScope scope = new AuthorizationScope("global","accessEverything");
        AuthorizationScope scopes[] = new AuthorizationScope[1];
        scopes[0] = scope;
        res.add(new SecurityReference("authorization",scopes));
        return  res;
    }

    // ApiKey继承于SecurityScheme抽象类
    /**
     * 设置完成后进入SwaggerUI，右上角出现“Authorization”按钮，点击即可输入我们配置的认证参数。
     * 对于不需要输入参数的接口（上文所述的包含hello的接口），在未输入Authorization参数就可以访问。
     * 其他接口则将返回401错误。点击右上角“Authorization”按钮，输入配置的参数后即可通过认证访问。参数输入后全局有效，无需每个接口单独输入。
     * 通过Swagger2的securitySchemes配置全局参数：如下列代码所示，securitySchemes的ApiKey中增加一个名为“Authorization”，type为“header”的参数。
     * @return
     */
    private List<ApiKey> securitySchemes(){
        List<ApiKey> res = new ArrayList<>();
        ApiKey apiKey = new ApiKey("authorization","authorization","Header");
        res.add(apiKey);
        return res;
    }

}
