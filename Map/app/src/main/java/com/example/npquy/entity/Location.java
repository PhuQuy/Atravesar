package com.example.npquy.entity;

import java.io.Serializable;

/**
 *
 * @author npquy
 *
 */
public class Location implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    double Lat;
    double Lgn;

    // The constructor
    public Location(double latitude, double longitude) {
        super();
        this.Lat = latitude;
        this.Lgn = longitude;
    }
    public Location() {
        super();
    }
    @Override
    public String toString() {
        return "Location [Lat=" + Lat + ", Lgn=" + Lgn + "]";
    }
    public double getLat() {
        return Lat;
    }
    public void setLat(double lat) {
        Lat = lat;
    }
    public double getLgn() {
        return Lgn;
    }
    public void setLgn(double lgn) {
        Lgn = lgn;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(Lat);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(Lgn);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Location other = (Location) obj;
        if (Double.doubleToLongBits(Lat) != Double.doubleToLongBits(other.Lat))
            return false;
        if (Double.doubleToLongBits(Lgn) != Double.doubleToLongBits(other.Lgn))
            return false;
        return true;
    }


}
