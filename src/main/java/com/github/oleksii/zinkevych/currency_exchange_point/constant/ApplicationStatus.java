package com.github.oleksii.zinkevych.currency_exchange_point.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ApplicationStatus {

    NEW ("NEW"),
    CONFIRMED ("CONFIRMED"),
    COMPLETED ("COMPLETED"),
    CANCELED ("CANCELED");

    public final String value;

    ApplicationStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ApplicationStatus fromValue(String value) {
        for (ApplicationStatus e : values()) {
            if (e.value.equalsIgnoreCase(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException("No such value: " + value);
    }
}
