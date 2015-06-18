package com.example.jin.materialdesign.models;

/**
 * Created by Jin on 2015-06-15.
 */
public class User {
    int id;
    String name;
    String address;
    String phone;
    String datetime;

    public User(int id, String name, String address, String phone, String datetime) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.datetime = datetime;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
