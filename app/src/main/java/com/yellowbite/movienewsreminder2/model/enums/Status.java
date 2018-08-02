package com.yellowbite.movienewsreminder2.model.enums;

public enum Status {
    VERFUEGBAR("Verfügbar"),
    ENTLIEHEN("Entliehen"),
    VORBESTELLT("Vorbestellt");

    private String value;

    Status(String value)
    {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
