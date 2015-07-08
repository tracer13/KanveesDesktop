package com.kanvees.desktop.model.enums;

public enum ColorsEnum {

    TRANSPARENT ("TRANSPARENT"),
    BLACK ("BLACK"),
    RED ("RED"),
    BLUE ("BLUE"),
    GREEN ("GREEN"),
    PURPLE ("PURPLE");

    private String colorString;

    ColorsEnum (String colorString) {
        this.colorString = colorString;
    }
}
