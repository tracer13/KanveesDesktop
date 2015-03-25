package com.kanvees.desktop.model.enums;

public enum ImportanceEnum {

    REGULAR("Not important"),
    IMPORTANT("Important"),
    CRITICAL("Critical");

    private String label;

    ImportanceEnum(String label){
        this.label = label;
    }

    @Override
    public String toString(){
        return label;
    }
}
