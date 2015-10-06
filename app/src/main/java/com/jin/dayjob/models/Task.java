package com.jin.dayjob.models;

public class Task {
    int id;
    String pay;
    String description;
    String location;
    String date;
    String time;
    String phone;
    String category;
    int icon;
    double latitude;
    double longitude;
    String username;
    String createTime;

    public Task(int id, String pay, String description, String location, String date, String time,
                  String phone, String category, int icon, double latitude,
                  double longitude, String username, String createTime) {
        super();
        this.id = id;
        this.pay = pay;
        this.description = description;
        this.location = location;
        this.date = date;
        this.time = time;
        this.phone = phone;
        this.category = category;
        this.icon = icon;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createTime = createTime;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
