package com.umnix.transferbansko.model;


public enum Destination {

    SOF_BAN("Sofia -> Bansko", 60, 100),
    BAN_SOF("Bansko -> Sofia", 60, 100),
    PLOV_BAN("Plovdiv -> Bansko", 65, 110),
    PAN_PLOV("Bansko -> Plovdiv", 65, 110),
    THES_BAN("Thessaloniki -> Bansko", 120, 150),
    BAN_THES("Bansko -> Thessaloniki", 120, 150),
    OTHER("Different Route", 0, 0);

    private String description;
    private int mpvPrice;
    private int miniBusPrice;

    Destination(String description, int mpvPrice, int miniBusPrice) {
        this.description = description;
        this.mpvPrice = mpvPrice;
        this.miniBusPrice = miniBusPrice;
    }

    public static Destination fromDescription(String description) {
        for (Destination item : Destination.values()) {
            if (item.description.equals(description)) {
                return item;
            }
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public int getMpvPrice() {
        return mpvPrice;
    }

    public int getMiniBusPrice() {
        return miniBusPrice;
    }
}
