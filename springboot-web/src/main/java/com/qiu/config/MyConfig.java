package com.qiu.config;

import com.qiu.entity.Person;
import com.qiu.entity.Pet;
import com.qiu.interceptor.LoginInterceptor;
import com.qiu.interceptor.LogoutInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyConfig implements WebMvcConfigurer {
    // url /后是否截断，矩阵变量
//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer) {
//        UrlPathHelper urlPathHelper = new UrlPathHelper();
//        urlPathHelper.setRemoveSemicolonContent(false);
//        configurer.setUrlPathHelper(urlPathHelper);
//    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .order(1)
                .excludePathPatterns("/","/login","/css/**","/fonts/**","/images/**",
                        "/js/**","/aa/**"); //放行的请求
        registry.addInterceptor(new LogoutInterceptor())
                .addPathPatterns("/**")
                .order(2)
                .excludePathPatterns("/","/login","/css/**","/fonts/**","/images/**",
                        "/js/**","/aa/**"); //放行的请求
    }

    @Bean
    @ConditionalOnMissingBean //如果没有其他person bean 则注入
    public Person person1() {
        return new Person(2, "hhah");
    }

    @Bean
    @ConditionalOnBean(Person.class)  //有Person bean注入，也注入pet
    public Pet pet() {
        return new Pet(3, "cat");
    }


}
