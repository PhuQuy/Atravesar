package com.example.npquy.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by npquy on 3/17/2016.
 */
public class BundleAddress {
    private List<Address> addresses = new ArrayList<>();
    private String message;
    private Integer code;

    public BundleAddress() {
    }

    public List<Address> getAddressList() {
        return addresses;
    }

    public void setAddressList(List<Address> addressList) {
        this.addresses = addressList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "BundleAddress{" +
                "addressList=" + addresses +
                ", message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
