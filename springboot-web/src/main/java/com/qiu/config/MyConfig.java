package com.qiu.config;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.qiu.entity.Person;
import com.qiu.entity.Pet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class MyConfig implements WebMvcConfigurer {
    // url /后是否截断，矩阵变量
//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer) {
//        UrlPathHelper urlPathHelper = new UrlPathHelper();
//        urlPathHelper.setRemoveSemicolonContent(false);
//        configurer.setUrlPathHelper(urlPathHelper);
//    }
    private static ListeningExecutorService      backgroundRefreshPools =
            MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
    public static  LoadingCache<Integer, String> KEY_NAME_CACHE         = CacheBuilder.newBuilder()
            .maximumSize(4096)
            .refreshAfterWrite(4,TimeUnit.SECONDS)
            .removalListener((RemovalListener<Integer, String>) rn -> {
                //逻辑操作
                log.info("remove cache rn" + JSON.toJSONString(rn));
            })
            .build(new CacheLoader<Integer, String>() {
                @Override
                public String load(Integer key) throws Exception {
                    if (key == null || key == 0L) {
                        return null;
                    }
//                    log.info("load cache key=" + key);
                    return getValue(key) ;
                }

                @Override
                public ListenableFuture<String> reload(Integer key, String oldValue) throws Exception {
                    return backgroundRefreshPools.submit(()->getValue(key));
                }
                public String getValue(Integer key){
                    if(key == 11){
                        return null;
                    }
                    log.info("getValue -----> key:" +key);
                   return key + "cache";
                }
            });//在构建时指定自动加载器
    public static  void refreshKeyNameCache(){
        Map<Integer, String> map = new HashMap<>(1024, 1f);
        for (Integer i = 0; i < 10; i++) {
            map.put(i, i + "cache");
        }
        KEY_NAME_CACHE.putAll(map);
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginInterceptor())
//                .addPathPatterns("/**")
//                .order(1)
//                .excludePathPatterns("/", "/login", "/css/**", "/fonts/**", "/images/**",
//                        "/js/**", "/aa/**"); //放行的请求
//        registry.addInterceptor(new LogoutInterceptor())
//                .addPathPatterns("/**")
//                .order(2)
//                .excludePathPatterns("/", "/login", "/css/**", "/fonts/**", "/images/**",
//                        "/js/**", "/aa/**"); //放行的请求
//    }

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


}
