package application.utility;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MessageWrapper {

	public static void showMessage(String message, AlertType at){
		Alert a = new Alert(at);
        a.setTitle(MessageWrapper.inferAlertStringFromType(at));
        a.setHeaderText(message);
        a.showAndWait();
	}
	private static String inferAlertStringFromType(AlertType at){
		switch(at){
		case ERROR:{
			return "Error";
		}
		case INFORMATION:{
			return "Information";
		}
		case WARNING:{
			return "Warning";
		}
		}
		return "Message";
	}
}

