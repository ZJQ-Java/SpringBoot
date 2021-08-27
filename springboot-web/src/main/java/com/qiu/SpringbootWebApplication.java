package com.qiu;

import com.alibaba.fastjson.JSON;
import com.qiu.server.God;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

@SpringBootApplication
public class SpringbootWebApplication {
    public static void main(String[] args) {

        ConfigurableApplicationContext applicationContext = SpringApplication.run(SpringbootWebApplication.class, args);
        Map<String, God> beanNamesForType = applicationContext.getBeansOfType(God.class);
        for (Map.Entry<String, God> stringGodEntry : beanNamesForType.entrySet()) {
            System.out.println("key:"+stringGodEntry.getKey() + " value:" + stringGodEntry.getValue());
            System.out.println(stringGodEntry.getValue().showName());
            Class supportedType = stringGodEntry.getValue().supportedType();
            System.out.println(supportedType.getSimpleName());
        }
    }

}
