package application.enums;

import lombok.Getter;

@Getter
public enum ExceptionFatalityEnum {
    FATAL("Fatal"),
    NON_FATAL("Non-fatal");

    private final String title;

    ExceptionFatalityEnum(String title) {
        this.title = title;
    }

}
