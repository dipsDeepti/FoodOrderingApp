package com.upgrad.FoodOrderingApp.service.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ItemType {
    VEG("VEG"),
    NON_VEG("NON_VEG");

    private final String value;

    private ItemType(String value) {
        this.value = value;
    }

@Override
    public String toString(){
        return value;
}

}