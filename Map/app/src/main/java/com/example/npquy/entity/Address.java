package com.example.npquy.entity;

import java.util.List;
import java.util.Objects;

/**
 * Created by npquy on 3/17/2016.
 */
public class Address {
    private Objects outCode;
    private String postCode;
    private String fullAddress;
    private String Category;
    private String icon_Path;
    private Double Latitude;
    private Double Longitude;

    public Address() {
    }

    public Objects getOutCode() {
        return outCode;
    }

    public void setOutCode(Objects outCode) {
        this.outCode = outCode;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getIcon_Path() {
        return icon_Path;
    }

    public void setIcon_Path(String icon_Path) {
        this.icon_Path = icon_Path;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }
}
