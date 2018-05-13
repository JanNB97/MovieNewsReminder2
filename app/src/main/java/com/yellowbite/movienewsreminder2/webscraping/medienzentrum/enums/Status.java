package com.yellowbite.movienewsreminder2.webscraping.medienzentrum.enums;

public enum Status {
    VERFUEGBAR("verfügbar"),
    ENTLIEHEN("entliehen"),
    VORBESTELLT("vorbestellt");

    private String value;

    Status(String value)
    {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
