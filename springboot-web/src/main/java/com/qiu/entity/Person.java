package com.qiu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private String questionStatus;//
    private String consultCategory;//:一级分类>二级分类>三级分类
    private String consultCategoryId;//:16441805960195>16441806784515>16441808084355
    private String remark;//啊啊啊啊啊
}
