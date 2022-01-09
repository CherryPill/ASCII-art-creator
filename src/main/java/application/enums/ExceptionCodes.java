package application.enums;

public enum ExceptionCodes {

    CLASS_LOAD_RESOURCE_LOAD_XCPT(0x1),
    JVM_GENERIC_IO_XCPT(0x1A),
    JVM_IMAGE_CONVERSION_XCPT(0x1B);

    private final Integer code;

    ExceptionCodes(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }
}
