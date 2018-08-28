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

    private float tct_balance;

    private int score;

    private float rmb_rate;

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

    public float getTct_balance() {
        return tct_balance;
    }

    public void setTct_balance(float tct_balance) {
        this.tct_balance = tct_balance;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public float getRmb_rate() {
        return rmb_rate;
    }

    public void setRmb_rate(float rmb_rate) {
        this.rmb_rate = rmb_rate;
    }
}
