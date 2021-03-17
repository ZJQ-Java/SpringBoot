package com.qiu.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "qiu")  //通过设置前缀，然后再yml文件中设置属性
//@PropertySource(value = "classpath:user.properties")
@Validated
public class Person {

    private String              personName;
    private Integer             age;
    @Email
    private String              email;
    private List<Object>        lists;
    private Map<String, String> maps;
    private Dog                 dog;

    @Override
    public String toString() {
        return "Person{" +
                "personName='" + personName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", lists=" + lists +
                ", maps=" + maps +
                ", dog=" + dog +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Object> getLists() {
        return lists;
    }

    public void setLists(List<Object> lists) {
        this.lists = lists;
    }

    public Map<String, String> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, String> maps) {
        this.maps = maps;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }
}
