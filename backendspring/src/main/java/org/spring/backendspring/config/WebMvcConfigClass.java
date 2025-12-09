// package org.spring.backendspring.config;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class WebMvcConfigClass implements WebMvcConfigurer {

//     @Value("${filePath}")
//     private String filePath;

//     @Override
//     public void addResourceHandlers(ResourceHandlerRegistry registry) {
//         registry.addResourceHandler("/upload/**") // 💡 웹에서 접근할 URL 패턴
//                 .addResourceLocations("file:///E:/full/upload/");
//         // .addResourceLocations(filePath); // 💡 실제 파일이 저장된 로컬 경로
//     }
// }

