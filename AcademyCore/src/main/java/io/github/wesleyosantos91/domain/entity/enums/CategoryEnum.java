package io.github.wesleyosantos91.domain.entity.enums;

import java.util.HashMap;
import java.util.Map;

public enum CategoryEnum {

    FRONT_END("Front-end"),
    BACK_END("Back-end");

    private final String value;

    private static final Map<String, CategoryEnum> VALUE_MAP = new HashMap<>();

    static {
        for (CategoryEnum status : CategoryEnum.values()) {
            VALUE_MAP.put(status.value.toLowerCase(), status);
        }
    }

    CategoryEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CategoryEnum fromValue(String value) {
        final CategoryEnum status = VALUE_MAP.get(value.toLowerCase());
        if (status == null) {
            throw new IllegalArgumentException("Unknown user type: " + value);
        }
        return status;
    }
}
