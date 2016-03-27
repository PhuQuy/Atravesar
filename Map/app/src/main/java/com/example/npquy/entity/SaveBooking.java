package com.example.npquy.entity;

/**
 * Created by npquy on 3/25/2016.
 */
public class SaveBooking {
    private String Outcode;
    private String RtnOutcode;
    private Integer cusid;
    private Address pick;
    private Address doff;
    private String via;
    private Double routedistance;
    private Double rtnroutedistance;
    private Object vehType;
    private Integer vehTypeID;
    private Double travelTime;
    private Double rtntraveltime;
    private Double totalfare;
    private Double returnfare;
    private Double fare;
    private String note;
    private Double pkLat;
    private Double pkLong;
    private Object rjType;
    private String bookingdate;
    private Integer paq;
    private Integer bags;
    private Double doLat;
    private Double doLong;
    private Double viaLat;
    private Double viaLong;
    private Boolean childseat;
    private Boolean petfriendly;
    private Boolean executive;
    private Object returndate;
    private Boolean InServiceArea;

    public SaveBooking() {
    }

    public String getOutcode() {
        return Outcode;
    }

    public void setOutcode(String outcode) {
        Outcode = outcode;
    }

    public String getRtnOutcode() {
        return RtnOutcode;
    }

    public void setRtnOutcode(String rtnOutcode) {
        RtnOutcode = rtnOutcode;
    }

    public Integer getCusid() {
        return cusid;
    }

    public void setCusid(Integer cusid) {
        this.cusid = cusid;
    }

    public Address getPick() {
        return pick;
    }

    public void setPick(Address pick) {
        this.pick = pick;
    }

    public Address getDoff() {
        return doff;
    }

    public void setDoff(Address doff) {
        this.doff = doff;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public Double getRoutedistance() {
        return routedistance;
    }

    public void setRoutedistance(Double routedistance) {
        this.routedistance = routedistance;
    }

    public Double getRtnroutedistance() {
        return rtnroutedistance;
    }

    public void setRtnroutedistance(Double rtnroutedistance) {
        this.rtnroutedistance = rtnroutedistance;
    }

    public Object getVehType() {
        return vehType;
    }

    public void setVehType(Object vehType) {
        this.vehType = vehType;
    }

    public Integer getVehTypeID() {
        return vehTypeID;
    }

    public void setVehTypeID(Integer vehTypeID) {
        this.vehTypeID = vehTypeID;
    }

    public Double getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Double travelTime) {
        this.travelTime = travelTime;
    }

    public Double getRtntraveltime() {
        return rtntraveltime;
    }

    public void setRtntraveltime(Double rtntraveltime) {
        this.rtntraveltime = rtntraveltime;
    }

    public Double getTotalfare() {
        return totalfare;
    }

    public void setTotalfare(Double totalfare) {
        this.totalfare = totalfare;
    }

    public Double getReturnfare() {
        return returnfare;
    }

    public void setReturnfare(Double returnfare) {
        this.returnfare = returnfare;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getPkLat() {
        return pkLat;
    }

    public void setPkLat(Double pkLat) {
        this.pkLat = pkLat;
    }

    public Double getPkLong() {
        return pkLong;
    }

    public void setPkLong(Double pkLong) {
        this.pkLong = pkLong;
    }

    public Object getRjType() {
        return rjType;
    }

    public void setRjType(Object rjType) {
        this.rjType = rjType;
    }

    public String getBookingdate() {
        return bookingdate;
    }

    public void setBookingdate(String bookingdate) {
        this.bookingdate = bookingdate;
    }

    public Integer getPaq() {
        return paq;
    }

    public void setPaq(Integer paq) {
        this.paq = paq;
    }

    public Integer getBags() {
        return bags;
    }

    public void setBags(Integer bags) {
        this.bags = bags;
    }

    public Double getDoLat() {
        return doLat;
    }

    public void setDoLat(Double doLat) {
        this.doLat = doLat;
    }

    public Double getDoLong() {
        return doLong;
    }

    public void setDoLong(Double doLong) {
        this.doLong = doLong;
    }

    public Double getViaLat() {
        return viaLat;
    }

    public void setViaLat(Double viaLat) {
        this.viaLat = viaLat;
    }

    public Double getViaLong() {
        return viaLong;
    }

    public void setViaLong(Double viaLong) {
        this.viaLong = viaLong;
    }

    public Boolean getChildseat() {
        return childseat;
    }

    public void setChildseat(Boolean childseat) {
        this.childseat = childseat;
    }

    public Boolean getPetfriendly() {
        return petfriendly;
    }

    public void setPetfriendly(Boolean petfriendly) {
        this.petfriendly = petfriendly;
    }

    public Boolean getExecutive() {
        return executive;
    }

    public void setExecutive(Boolean executive) {
        this.executive = executive;
    }

    public Object getReturndate() {
        return returndate;
    }

    public void setReturndate(Object returndate) {
        this.returndate = returndate;
    }

    public Boolean getInServiceArea() {
        return InServiceArea;
    }

    public void setInServiceArea(Boolean inServiceArea) {
        InServiceArea = inServiceArea;
    }

    @Override
    public String toString() {
        return "SaveBooking{" +
                "Outcode='" + Outcode + '\'' +
                ", RtnOutcode='" + RtnOutcode + '\'' +
                ", cusid=" + cusid +
                ", pick=" + pick +
                ", doff=" + doff +
                ", via='" + via + '\'' +
                ", routedistance=" + routedistance +
                ", rtnroutedistance=" + rtnroutedistance +
                ", vehType=" + vehType +
                ", vehTypeID=" + vehTypeID +
                ", travelTime=" + travelTime +
                ", rtntraveltime=" + rtntraveltime +
                ", totalfare=" + totalfare +
                ", returnfare=" + returnfare +
                ", fare=" + fare +
                ", note='" + note + '\'' +
                ", pkLat=" + pkLat +
                ", pkLong=" + pkLong +
                ", rjType=" + rjType +
                ", bookingdate='" + bookingdate + '\'' +
                ", paq=" + paq +
                ", bags=" + bags +
                ", doLat=" + doLat +
                ", doLong=" + doLong +
                ", viaLat=" + viaLat +
                ", viaLong=" + viaLong +
                ", childseat=" + childseat +
                ", petfriendly=" + petfriendly +
                ", executive=" + executive +
                ", returndate=" + returndate +
                ", InServiceArea=" + InServiceArea +
                '}';
    }
}
