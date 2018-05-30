package com.tadpolechain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lsq on 2018/4/10.
 */

public class Game {

    private String name;

    private String summary;

    private String icon;

    private int status;

    @SerializedName("ReceiveTCT")
    private int[] receiveTCTs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int[] getReceiveTCTs() {
        return receiveTCTs;
    }

    public void setReceiveTCTs(int[] receiveTCTs) {
        this.receiveTCTs = receiveTCTs;
    }
}
