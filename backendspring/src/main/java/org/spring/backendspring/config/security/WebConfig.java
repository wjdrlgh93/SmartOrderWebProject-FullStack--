package org.spring.backendspring.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 🔥 http://localhost:8088/uploadImg/파일명 으로 접근 가능하게 하는 설정
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:///E:/full/upload/",
                        "file:///C:/full/upload/",
                        "file:///D:/full/upload/");
    }
}
