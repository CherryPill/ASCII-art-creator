package application.utility;

import application.validator.ValidationResultEntry;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.List;

@UtilityClass
public class MessageUtil {

    private String createTitleString(AlertType at) {
        return at.name().charAt(0) + at.name().toLowerCase().substring(1);
    }

    public void showMessage(AlertType at, String message) {
        Alert a = new Alert(at);
        a.setTitle(createTitleString(at));
        a.setHeaderText(message);
        a.showAndWait();
    }

    public void showAllMessages(ValidationResultEntry validationResultEntry) {
        validationResultEntry
                .getErrorListByType()
                .orElseGet(HashMap::new)
                .forEach((alertType, strings) -> {
                    showMessage(alertType, buildMessage(strings));
                });
    }

    private String buildMessage(List<String> messages) {
        return String.join(Utility.NEW_LINE, messages);
    }
}

