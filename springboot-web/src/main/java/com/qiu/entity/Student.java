package com.qiu.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    Integer id;
    String  name;
    Integer age;
    static List<Student> list = null;

    static {
        list = Arrays.asList(new Student(1, "zjq", 21),
                new Student(2, "小明", 20),
                new Student(2, "小红", 19),
                new Student(3, "小白", 21),
                new Student(1, "小蓝", 19),
                new Student(4, "小黑", 21));
    }

    public static void main(String[] args) {
        /*Map<Integer, List<Student>> collect = list.stream().collect(Collectors.groupingBy(Student::getAge));
        System.out.println(JSON.toJSONString(collect));
        Map<Integer, Long> collect1 = list.stream()
                .collect(Collectors.groupingBy(Student::getAge, Collectors.counting()));
        System.out.println(JSON.toJSONString(collect1));
        Map<Integer, Map<Integer, List<Student>>> collect2 = list.stream()
                .collect(Collectors.groupingBy(Student::getId, Collectors.groupingBy(Student::getAge)));
        System.out.println(JSON.toJSONString(collect2));
        Map<Integer, List<Student>> collect3 = list.stream()
                .collect(Collectors.groupingBy(Student::getAge, TreeMap::new, Collectors.toList()));
        System.out.println(collect3);*/
        Map<Integer, Student> collect = list.stream().collect(Collectors.toMap(Student::getId, Function.identity(),(o1,o2)->o1));
        System.out.println(JSON.toJSONString(collect));
        collect.get(1).setAge(100);
        System.out.println(JSON.toJSONString(collect));


    }
}
