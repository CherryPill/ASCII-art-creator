package application.enums;

import lombok.Getter;

@Getter
public enum ExceptionFatality {
    FATAL("Fatal"),
    NON_FATAL("Non-fatal");

    private final String title;

    ExceptionFatality(String title) {
        this.title = title;
    }

}
