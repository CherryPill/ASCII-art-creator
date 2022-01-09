package application.utility.messages;

import application.enums.ExceptionCodesEnum;
import application.enums.ExceptionFatalityEnum;
import application.utility.files.PathInfoHolder;
import application.utility.files.FileUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MessageUtils {

    private static final String messageFormat = "%s exception occurred.%sException code: %X, Description: %s,%s,%s" +
            "%s" +
            "Error log can be found at %s";

    public String buildExceptionMessageString(ExceptionCodesEnum exceptionCode,
                                              ExceptionFatalityEnum exceptionFatalityEnum,
                                              Throwable throwable) {
        return String.format(messageFormat,
                exceptionFatalityEnum.getTitle(),
                FileUtil.NEW_LINE,
                exceptionCode.getCode(),
                exceptionCode.getDesc(),
                throwable,
                FileUtil.NEW_LINE,
                FileUtil.NEW_LINE,
                PathInfoHolder.getUserHome() + PathInfoHolder.getLogFileLocation());
    }
}
