package com.tadpolechain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lsq on 2018/4/10.
 */
public class User {

    private String id;

    private String token;

    private int age;

    private int sex;

    private String nickname;

    private String avatar;

    private float profit;

    private int status;

    @SerializedName("ad_divider")
    private int adDivider;

    @SerializedName("app_url")
    private String appUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAdDivider() {
        return adDivider;
    }

    public void setAdDivider(int adDivider) {
        this.adDivider = adDivider;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }
}
