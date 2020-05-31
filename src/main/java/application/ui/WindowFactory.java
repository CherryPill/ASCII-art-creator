package application.ui;

import application.Main;
import application.constants.AppConstants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class WindowFactory {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public Stage createWindowFromFXML(Modality modality) {
        Stage newWindow = new Stage();
        Parent pbWindowRoot = null;
        try {
            pbWindowRoot = FXMLLoader
                    .load(getClass()
                            .getClassLoader()
                            .getResource("layout/pb.fxml"));

        } catch (IOException io) {
            logger.error("Failed to load markup for child window");
        }
        Scene scene = new Scene(pbWindowRoot, AppConstants.UIConstants.Window.PB_WINDOW_WIDTH, AppConstants.UIConstants.Window.PB_WINDOW_HEIGHT);
        newWindow.setScene(scene);
        newWindow.initModality(modality);
        newWindow.initStyle(StageStyle.UNDECORATED);
        return newWindow;
    }
}
