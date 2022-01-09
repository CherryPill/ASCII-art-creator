package application.utility;

import application.enums.ExceptionCodes;
import application.enums.ExceptionFatality;

import static application.constants.AppConstants.exceptionCodes;
// todo refactor
public class MessageUtils {


    public static String buildExceptionMessageString(ExceptionCodes exceptionCode,
                                                     ExceptionFatality exceptionFatality) {
        return String.format("%s exception occurred.%sException code: %X, Description: %s%s" +
                        "Please contact the developer and provide the exception code and description. %s" +
                        "A more detailed description of the error can be found at %s.",
                exceptionFatality.getTitle(),
                Utility.NEW_LINE,
                exceptionCode.getCode(),
                exceptionCodes.get(exceptionCode),
                Utility.NEW_LINE,
                Utility.NEW_LINE,
                System.getProperty("user.home") + "/ascii_art/logs/log.txt");
    }

    public static String buildExceptionMessageString(ExceptionCodes exceptionCode,
                                                     ExceptionFatality exceptionFatality,
                                                     Throwable throwable){
        return String.format("%s exception occurred.%sException code: %X, Description: %s,%s,%s" +
                        "%s" +
                        "Error log can be found at %s.",
                exceptionFatality.getTitle(),
                Utility.NEW_LINE,
                exceptionCode.getCode(),
                exceptionCodes.get(exceptionCode),
                throwable,
                Utility.NEW_LINE,
                Utility.NEW_LINE,
                System.getProperty("user.home") + "/ascii_art/logs/log.txt");
    }
}
