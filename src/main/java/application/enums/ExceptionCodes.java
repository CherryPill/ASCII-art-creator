package application.enums;

import lombok.Getter;

@Getter
public enum ExceptionCodes {

    CLASS_LOAD_RESOURCE_LOAD_XCPT(0x1),
    JVM_GENERIC_IO_XCPT(0x1A);

    private final Integer code;

    ExceptionCodes(Integer code) {
        this.code = code;
    }

}
