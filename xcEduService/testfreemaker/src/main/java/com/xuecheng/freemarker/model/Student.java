package com.xuecheng.freemarker.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author chenjia
 * @version 1.0
 * @date 2020/5/7 10:11
 * @desc
 */
@Data
@ToString
public class Student {

    // 姓名
    private String name;

    // 年龄
    private Integer age;

    // 生日
    private Date birthday;

    // 资产
    private Double money;

    // 朋友
    private List<Student> friends;

    // 最好的朋友
    private Student bestFriend;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public List<Student> getFriends() {
        return friends;
    }

    public void setFriends(List<Student> friends) {
        this.friends = friends;
    }

    public Student getBestFriend() {
        return bestFriend;
    }

    public void setBestFriend(Student bestFriend) {
        this.bestFriend = bestFriend;
    }
}
