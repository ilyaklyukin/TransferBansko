package com.umnix.transferbansko.model;


public enum TransportType {
    MPV("Executive or MPV car (1-3 persons)"),
    MINI_BUS("Mini bus (4-7 persons)");

    private String title;

    TransportType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
