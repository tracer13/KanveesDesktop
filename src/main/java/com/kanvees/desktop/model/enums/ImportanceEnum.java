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

    public String getStringValue (){
        String importanceStringValue = "";
        switch (label) {
            case "Not important":
                importanceStringValue = " ";
                break;
            case "Important":
                importanceStringValue=  "!";
                break;
            case "Critical":
                importanceStringValue = "!!";
                break;
        }
        return importanceStringValue;
    }
}
