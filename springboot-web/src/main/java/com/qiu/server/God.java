package com.qiu.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiu.entity.mysql.Book;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public interface God<T> {
    public String showName();

    default Class<T> supportedType() {
        Type[] interfaces = getClass().getGenericInterfaces();
        Type operationLogHandler = null;
        if (interfaces.length == 1) {
            operationLogHandler = interfaces[0];
        } else {
            for (Type _interface : interfaces) {
                if (_interface.getTypeName().contains(God.class.getName())) {
                    operationLogHandler = _interface;
                    break;
                }
            }
            if (operationLogHandler == null) {
                return null;
            }
        }
        return (Class<T>) ((ParameterizedType) operationLogHandler).getActualTypeArguments()[0];
    }

    public static void main(String[] args) {
        Map<String,String> a = new HashMap<>();
        a.put("username","suliangjian");
        System.out.println(JSON.toJSONString(a));
//        JSONObject jsonObject = new JSONObject(str);
    }
}

@Component
@Data
class Person implements God<Book> {
    String id;

    @Override
    public String showName() {
        return "person";
    }
}

@Component
class Animal implements God<String> {

    @Override
    public String showName() {
        return "Animal";
    }
}

