package com.xuecheng.freemarker.controller;

import com.xuecheng.freemarker.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * @author chenjia
 * @version 1.0
 * @date 2020/5/7 10:21
 * @desc
 */
@Controller
@RequestMapping("/freemarker")
public class FreemarkerController {

    @RequestMapping("/test")
    public String test(Map<String, Object> map) {
        map.put("name", "freemarker");
        Student stu1 = new Student();
        stu1.setName("小明");
        stu1.setAge(18);
        stu1.setBirthday(new Date());
        stu1.setMoney(1000.0D);
        Student stu2 = new Student();
        stu2.setName("小红");
        stu2.setAge(16);
        stu2.setBirthday(new Date());
        stu2.setMoney(200.0D);
        stu1.setBestFriend(stu2);
        List<Student> stus = new ArrayList<Student>();
        stus.add(stu1);
        stus.add(stu2);
        map.put("stu1",stu1);
        map.put("stu2",stu2);
        map.put("stus",stus);
        Map<String,Student> stuMap = new HashMap<String, Student>();
        stuMap.put("stu1",stu1);
        stuMap.put("stu2",stu2);
        map.put("stuMap",stuMap);
        return "test";
    }
}
