package io.github.wesleyosantos91.domain.entity.enums;

import java.util.HashMap;
import java.util.Map;

public enum CourseStatusEnum {

    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String value;

    private static final Map<String, CourseStatusEnum> VALUE_MAP = new HashMap<>();

    static {
        for (CourseStatusEnum status : CourseStatusEnum.values()) {
            VALUE_MAP.put(status.value.toLowerCase(), status);
        }
    }

    CourseStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CourseStatusEnum fromValue(String value) {
        final CourseStatusEnum status = VALUE_MAP.get(value.toLowerCase());
        if (status == null) {
            throw new IllegalArgumentException("Unknown user type: " + value);
        }
        return status;
    }
}
