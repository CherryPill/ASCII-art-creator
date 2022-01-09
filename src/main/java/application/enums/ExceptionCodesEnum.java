package application.enums;

import lombok.Getter;

@Getter
public enum ExceptionCodesEnum {

    CLASS_LOAD_RESOURCE_LOAD_XCPT(0x1, "Class loader resource load"),
    JVM_GENERIC_IO_XCPT(0x1A, "Generic IOException"),
    JVM_IMAGE_CONVERSION_XCPT(0x1B, "Image conversion exception");

    private final Integer code;

    private final String desc;

    ExceptionCodesEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
