package com.course.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: 目光定晴天
 * @Date: 2021/6/9 15:56
 * @Description:
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
//    Spring的@Bean注解用于告诉方法，产生一个Bean对象，
//    然后这个Bean对象交给Spring管理。产生这个Bean对象的方法Spring只会调用一次，
//    随后这个Spring将会将这个Bean对象放在自己的IOC容器中。
//    使用@Bean注解的好处就是能够动态获取一个Bean对象，能够根据环境不同得到不同的Bean对象。
//    或者说将Spring和其他组件分离（其他组件不依赖Spring，但是又想Spring管理生成的bean）。
    //https://blog.csdn.net/jdfk423/article/details/80829263
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .pathMapping("/")
                .select()
                .paths(PathSelectors.regex("/.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("chapter13-UserManger api")
                .description("这是我swaggerUI生成的接口文档")
                .contact(new Contact("fanyu","","490718876@qq.com"))
                .version("1.0")
                .build();

    }
}
