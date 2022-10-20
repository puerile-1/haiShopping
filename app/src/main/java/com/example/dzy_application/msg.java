package com.example.dzy_application;

public class msg {
    private int image;
    private String name;
    private String msg;
    private String time;

    public msg(int image, String name, String msg, String time) {
        this.image = image;
        this.name = name;
        this.msg = msg;
        this.time = time;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
