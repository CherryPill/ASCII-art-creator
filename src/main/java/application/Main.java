package application;

import application.constants.AppConstants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {

    private static final Logger logger = LogManager.getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("layout/layout.fxml"));
            Scene scene = new Scene(root,
                    AppConstants.UIConstants.Window.MAIN_WINDOW_WIDTH,
                    AppConstants.UIConstants.Window.MAIN_WINDOW_HEIGHT);
            scene.getStylesheets().add(getClass().getResource("/style/application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle(AppConstants.UIConstants.Window.MAIN_WINDOW_APP_NAME);
            primaryStage.show();
            logger.info("Main app started. UI started.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
