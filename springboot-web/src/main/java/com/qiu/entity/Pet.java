package com.qiu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pet {
    private String consultCategory;//:增值>其他>联系业务员
    private String consultCategoryId;//:6350>6362>6653
    private String remark;//:测试咨询描述
    private String questionStatus;//:未处理
    private String cscProcessType;//:10
    private String cscResponsible;//:城市/大区运营
}
