package com.jin.dayjob.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Jin on 2015-06-07.
 */
public class TaskMarker implements ClusterItem {
    private int index;
    private int id;
    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private final int icon;
    private final int small_Icon;
    String pay;
    String description;
    String location;
    String date;
    String time;
    String phone;
    String category;
    String username;
    String createTime;

    public TaskMarker(int index, int id, double lat, double lng, String mTitle, String mSnippet,
                      int icon, int small_Icon, String pay, String description, String location, String date, String time, String phone, String category, String username, String createTime) {
        this.index = index;
        this.id = id;
        this.mPosition = new LatLng(lat, lng);
        this.mTitle = mTitle;
        this.mSnippet = mSnippet;
        this.icon = icon;
        this.small_Icon = small_Icon;
        this.pay = pay;
        this.description = description;
        this.location = location;
        this.date = date;
        this.time = time;
        this.phone = phone;
        this.category = category;
        this.username = username;
        this.createTime = createTime;

    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSnippet() {
        return mSnippet;
    }


    public int getIcon() {
        return icon;
    }

    public int getSmallIcon() {
        return small_Icon;
    }

    public int getId() {
        return id;
    }

    public int getIndex() {
        return index;
    }

    public String getPay() {
        return pay;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPhone() {
        return phone;
    }

    public String getCategory() {
        return category;
    }

    public String getUsername() {
        return username;
    }

    public String getCreateTime() {
        return createTime;
    }

}