package com.tadpolechain;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by lsq on 2018/4/10.
 */
public class Duration {

    @SerializedName("start_time")
    private String startTime;

    @SerializedName("end_time")
    private String endTime;

    private float profit;

    @SerializedName("ad_times")
    private int adTimes;

    private float duration;

    @SerializedName("all_duration")
    private float allDuration;

    @SerializedName("expect_profit")
    private float expectProfit;

    @SerializedName("msg_id")
    private String msgId;

    @SerializedName("msg_content")
    private String msgContent;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public float getProfit() {
        return profit;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    public int getAdTimes() {
        return adTimes;
    }

    public void setAdTimes(int adTimes) {
        this.adTimes = adTimes;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getAllDuration() {
        return allDuration;
    }

    public void setAllDuration(float allDuration) {
        this.allDuration = allDuration;
    }

    public float getExpectProfit() {
        return expectProfit;
    }

    public void setExpectProfit(float expectProfit) {
        this.expectProfit = expectProfit;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
}
