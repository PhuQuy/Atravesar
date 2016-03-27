package com.example.npquy.entity;

/**
 * Created by npquy on 3/22/2016.
 */
public class User {
    private String cusId;
    private String name;
    private String mobile;
    private String email;
    private String deviceId;

    public User() {
    }

    public User(String name, String mobile, String email, String deviceId) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.deviceId = deviceId;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
