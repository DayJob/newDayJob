package com.jin.dayjob.models;

/**
 * Created by Jin on 2015-06-15.
 */
public class User {
    int id;
    String name;
    String address;
    String phone;
    String sex;
    String birth;

    public User(int id, String name, String address, String phone, String sex, String birth) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.sex = sex;
        this.birth = birth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }
}
