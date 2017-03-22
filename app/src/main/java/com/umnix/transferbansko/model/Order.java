package com.umnix.transferbansko.model;


import android.text.TextUtils;

public class Order {

    private Destination destination;
    private boolean isNeedReturn = true;
    private TransportType transportType = TransportType.MPV;

    private String arrivalDate;
    private String arrivalTime;
    private String departureDate;
    private String departureTime;

    private String departureFlight;
    private String arrivalFlight;

    private String passengersCount;
    private String childCount;
    private String clientName;
    private String clientHotel;
    private String clientEmail;
    private String clientPhone;

    private String comments;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getClientHotel() {
        return clientHotel;
    }

    public void setClientHotel(String clientHotel) {
        this.clientHotel = clientHotel;
    }

    public String getArrivalFlight() {
        return arrivalFlight;
    }

    public void setArrivalFlight(String arrivalFlight) {
        this.arrivalFlight = arrivalFlight;
    }

    public String getDepartureFlight() {
        return departureFlight;
    }

    public void setDepartureFlight(String departureFlight) {
        this.departureFlight = departureFlight;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    public String getChildCount() {
        return TextUtils.isEmpty(childCount) ? "0" : childCount;
    }

    public void setChildCount(String childCount) {
        this.childCount = childCount;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public boolean isNeedReturn() {
        return isNeedReturn;
    }

    public void setNeedReturn(boolean needReturn) {
        isNeedReturn = needReturn;
    }

    public String getPassengersCount() {
        return passengersCount;
    }

    public void setPassengersCount(String passengersCount) {
        this.passengersCount = passengersCount;
    }

    private String formFlightInfo(String date, String time, String flight) {
        String flightInfo = "-";

        if (!TextUtils.isEmpty(date)) {
            flightInfo = date;
        }

        if (!TextUtils.isEmpty(time)) {
            flightInfo = flightInfo + " " + time;
        }

        if (!TextUtils.isEmpty(flight)) {
            flightInfo = flightInfo + ", \"" + flight + "\"";
        }

        return flightInfo;
    }

    public String fillContent(String template) {

        String arrivalInfo = formFlightInfo(getArrivalDate(), getArrivalTime(), getArrivalFlight());
        String departureInfo = formFlightInfo(getDepartureDate(), getDepartureTime(), getDepartureFlight());

        String content = String.format(template,
                getDestination().getDescription(),
                getClientName(),
                getClientHotel(),
                getClientEmail(),
                getClientPhone(),
                arrivalInfo,
                departureInfo,
                getTransportType().getTitle(),
                getPassengersCount(),
                getChildCount(),
                calcTotalAmount(),
                getComments()
        );

        return content;
    }

    public String calcTotalAmount() {

        if (destination == null || transportType == null) {
            return "-";
        }
        if (destination == Destination.OTHER) {
            return "on request";
        }

        int sum = transportType == TransportType.MPV ?
                destination.getMpvPrice() :
                destination.getMiniBusPrice();

        if (isNeedReturn) {
            sum = sum * 2;
        }

        return String.valueOf(sum) + " €";
    }
}
