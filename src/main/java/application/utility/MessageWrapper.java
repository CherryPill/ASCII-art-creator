package application.utility;

import application.constants.AppConstants;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MessageWrapper {

    private static String inferAlertStringFromType(AlertType at) {
        return AppConstants.UIConstants.Message.AlertStrings[at.ordinal()];
    }

    public static void showMessage(String message, AlertType at) {
        Alert a = new Alert(at);
        a.setTitle(MessageWrapper.inferAlertStringFromType(at));
        a.setHeaderText(message);
        a.showAndWait();
    }
}

