package application.ui;

import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowFactory {

    public Stage createWindow(Modality modality, String title, Double[] pos, Double[] dims) {
        VBox vbox = new VBox();
        ProgressBar progressBar = new ProgressBar(0);
        vbox.getChildren().add(progressBar);
        Scene newWindowScene = new Scene(
                vbox,
                pos[0],
                dims[1]);
        Stage newWindow = new Stage();
        newWindow.initStyle(StageStyle.UNDECORATED);
        newWindow.initModality(modality);
        newWindow.setTitle(title);
        newWindow.setX(pos[0]);
        newWindow.setX(pos[1]);
        newWindow.setScene(newWindowScene);
        return newWindow;
    }
}
