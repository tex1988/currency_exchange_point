package com.github.oleksii.zinkevych.currency_exchange_point.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ExchangeMode {
    DIRECT("direct"),
    PROXY("proxy");

    public final String value;

    ExchangeMode(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ExchangeMode fromValue(String value) {
        for (ExchangeMode e : values()) {
            if (e.value.equalsIgnoreCase(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException("No such value: " + value);
    }
}
