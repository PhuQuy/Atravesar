package com.example.npquy.entity;

/**
 * Created by ITACHI on 3/27/2016.
 */
public class JourneyHistory {
    String JobPartID;
    String Status;
    String Contact;
    Address PickupAddress;
    Address DropoffAddress;
    Double TotalFare;
    String Notes;
    String PickupDateTime;
    String ViaLocations;
    String FlightNumber;
    Double ExpFlightArrival;
    Integer ChildSeat;
    Integer Passengers;
    Integer Suitcases;
    String MeetAndGreet;
    String PaymentMethod;
    String JourneyType;
    String VehicleType;

    public JourneyHistory() {
    }

    public String getJobPartID() {
        return JobPartID;
    }

    public void setJobPartID(String jobPartID) {
        JobPartID = jobPartID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public Address getPickupAddress() {
        return PickupAddress;
    }

    public void setPickupAddress(Address pickupAddress) {
        PickupAddress = pickupAddress;
    }

    public Address getDropoffAddress() {
        return DropoffAddress;
    }

    public void setDropoffAddress(Address dropoffAddress) {
        DropoffAddress = dropoffAddress;
    }

    public Double getTotalFare() {
        return TotalFare;
    }

    public void setTotalFare(Double totalFare) {
        TotalFare = totalFare;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getPickupDateTime() {
        return PickupDateTime;
    }

    public void setPickupDateTime(String pickupDateTime) {
        PickupDateTime = pickupDateTime;
    }

    public String getViaLocations() {
        return ViaLocations;
    }

    public void setViaLocations(String viaLocations) {
        ViaLocations = viaLocations;
    }

    public String getFlightNumber() {
        return FlightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        FlightNumber = flightNumber;
    }

    public Double getExpFlightArrival() {
        return ExpFlightArrival;
    }

    public void setExpFlightArrival(Double expFlightArrival) {
        ExpFlightArrival = expFlightArrival;
    }

    public Integer getChildSeat() {
        return ChildSeat;
    }

    public void setChildSeat(Integer childSeat) {
        ChildSeat = childSeat;
    }

    public Integer getPassengers() {
        return Passengers;
    }

    public void setPassengers(Integer passengers) {
        Passengers = passengers;
    }

    public Integer getSuitcases() {
        return Suitcases;
    }

    public void setSuitcases(Integer suitcases) {
        Suitcases = suitcases;
    }

    public String getMeetAndGreet() {
        return MeetAndGreet;
    }

    public void setMeetAndGreet(String meetAndGreet) {
        MeetAndGreet = meetAndGreet;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public String getJourneyType() {
        return JourneyType;
    }

    public void setJourneyType(String journeyType) {
        JourneyType = journeyType;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }
}
