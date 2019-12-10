package application.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MessageWrapper {

    public static void showMessage(String message, AlertType at) {
        Alert a = new Alert(at);
        a.setTitle(MessageWrapper.inferAlertStringFromType(at));
        a.setHeaderText(message);
        a.showAndWait();
    }

    private static String inferAlertStringFromType(AlertType at) {
        return Utility.getProps().getProperty("ui.msg.alert_strings_arr")
                .split(Utility.getProps().getProperty("sys.core.default.delimiter"))
                [at.ordinal()];
    }
}

