package application.enums;

public enum ExceptionFatality {
    FATAL("Fatal"),
    NON_FATAL("Non-fatal");

    private String title;

    ExceptionFatality(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
