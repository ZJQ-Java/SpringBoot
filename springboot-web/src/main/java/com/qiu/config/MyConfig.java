//package com.qiu.config;
//
//import com.qiu.entity.Person;
//import com.qiu.entity.Pet;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.util.UrlPathHelper;
//
//@Configuration
//public class MyConfig implements WebMvcConfigurer {
//    // url /后是否截断，矩阵变量
////    @Override
////    public void configurePathMatch(PathMatchConfigurer configurer) {
////        UrlPathHelper urlPathHelper = new UrlPathHelper();
////        urlPathHelper.setRemoveSemicolonContent(false);
////        configurer.setUrlPathHelper(urlPathHelper);
////    }
//
//    @Bean
//    @ConditionalOnMissingBean //如果没有其他person bean 则注入
//    public Person person1() {
//        return new Person(2, "hhah");
//    }
//
//    @Bean
//    @ConditionalOnBean(Person.class)  //有Person bean注入，也注入pet
//    public Pet pet() {
//        return new Pet(3, "cat");
//    }
//
//
//
//}
