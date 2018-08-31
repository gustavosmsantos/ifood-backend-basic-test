package com.ifood.exception;

public class EntityNotFoundException extends Exception {

    private String type;

    private String value;

    public EntityNotFoundException(String type, String city) {
        this.type = type;
        this.value = city;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getMessage() {
        return String.format("%s not found: %s", this.type, this.value);
    }

}
