package com.qiu.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class God {
    public Person person;
    public Pet    pet;
    public String button;


    public static void main(String[] args) {
        God god = new God();
        final Person person = new Person();
        person.setConsultCategory("setConsultCategory_person");
        person.setConsultCategoryId("setConsultCategoryId_person");
        person.setQuestionStatus("setQuestionStatus_person");
        person.setRemark("");
        final Pet pet = new Pet();
        pet.setConsultCategory("setConsultCategory_pet");
        pet.setConsultCategoryId("setConsultCategoryId_pet");
        pet.setCscProcessType("setCscProcessType_pet");
        pet.setCscResponsible("setCscResponsible_pet");
        pet.setQuestionStatus("setQuestionStatus_pet");
        pet.setRemark("setRemark_pet");

        god.setPerson(person);
        god.setButton("测试");
        god.setPet(pet);
        final String s = JSON.toJSONString(god);
        System.out.println(s);

        final God god1 = JSON.parseObject(s, God.class);
        System.out.println(god1);

    }
}
