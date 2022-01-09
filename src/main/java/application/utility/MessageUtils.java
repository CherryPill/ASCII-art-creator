package application.utility;

import application.enums.ExceptionCodes;
import application.enums.ExceptionFatality;
import lombok.experimental.UtilityClass;

import static application.constants.AppConstants.exceptionCodes;

@UtilityClass
public class MessageUtils {

    private static final String messageFormat = "%s exception occurred.%sException code: %X, Description: %s,%s,%s" +
            "%s" +
            "Error log can be found at %s";

    public String buildExceptionMessageString(ExceptionCodes exceptionCode,
                                              ExceptionFatality exceptionFatality,
                                              Throwable throwable) {
        return String.format(messageFormat,
                exceptionFatality.getTitle(),
                FileUtil.NEW_LINE,
                exceptionCode.getCode(),
                exceptionCodes.get(exceptionCode),
                throwable,
                FileUtil.NEW_LINE,
                FileUtil.NEW_LINE,
                PathInfoHolder.getUserHome() + PathInfoHolder.getLogFileLocation());
    }
}
