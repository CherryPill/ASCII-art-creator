package application.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class WindowFactory {

    public Stage createWindowFromFXML(Modality modality) {
        Stage newWindow = new Stage();
        Parent pbWindowRoot = null;
        try {
            pbWindowRoot = FXMLLoader
                    .load(getClass()
                            .getClassLoader()
                            .getResource("layout/pb.fxml"));

        } catch (IOException io) {
            System.out.println("Failed to load markup for child window");
        }
        Scene scene = new Scene(pbWindowRoot, 251, 127);
        newWindow.setScene(scene);
        newWindow.initStyle(StageStyle.UNDECORATED);
        return newWindow;
    }
}
