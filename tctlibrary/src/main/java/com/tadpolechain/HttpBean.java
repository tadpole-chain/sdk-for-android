package com.tadpolechain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by chen on 2018/4/10
 * 基础网络数据Bean
 */
public class HttpBean<T> {

    public int code;

    public String message;

    public T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 解析数据
     * @param gson
     * @param typeToken 比如：new TypeToken<HttpBean<Object>>(){}
     * @param <T>
     * @return
     */
    public static <T> HttpBean<T> getBeanFromGson(String gson, TypeToken typeToken){
        Gson g = new Gson();
        HttpBean<T> bean = g.fromJson(gson, typeToken.getType());
        return bean;
    }

    @Override
    public String toString() {
        if(data != null) {
            return "code : " + code + " | msg : " + message
                    + " | data : " + new Gson().toJson(data);
        } else {
            return "code : " + code + " | msg : " + message;
        }
    }
}
