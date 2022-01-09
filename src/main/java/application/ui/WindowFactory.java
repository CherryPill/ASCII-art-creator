package application.ui;

import application.constants.AppConstants;
import application.enums.ExceptionCodesEnum;
import application.utility.files.PathInfoHolder;
import exceptions.ClassLoaderResourceLoadException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class WindowFactory {

    private static final Logger LOGGER = LogManager.getLogger(WindowFactory.class);

    public Optional<Stage> createWindowFromFXML(Modality modality) throws IOException, ClassLoaderResourceLoadException {
        Optional<Stage> newWindow = Optional.of(new Stage());
        Optional<Parent> pbWindowRoot;
        try {
            Optional<URL> url = Optional.ofNullable(getClass()
                    .getClassLoader()
                    .getResource(PathInfoHolder.getProgressBarFxmlLayoutLocation()));
            if (url.isEmpty()) {
                throw new ClassLoaderResourceLoadException();
            } else {
                try {
                    pbWindowRoot = Optional.ofNullable(FXMLLoader.load(url.get()));
                    if (pbWindowRoot.isPresent()) {
                        Scene scene = new Scene(pbWindowRoot.get(), AppConstants.UIConstants.Window.PB_WINDOW_WIDTH, AppConstants.UIConstants.Window.PB_WINDOW_HEIGHT);
                        newWindow.get().setScene(scene);
                        newWindow.get().initModality(modality);
                        newWindow.get().initStyle(StageStyle.UNDECORATED);
                    }
                    return newWindow;
                } catch (IOException ioe) {
                    LOGGER.warn(String.format("%s thrown. FXML had trouble loading layout resource file",
                            ExceptionCodesEnum.JVM_GENERIC_IO_XCPT.getDesc()));
                    LOGGER.warn(ioe);
                    throw ioe;
                }
            }
        } catch (ClassLoaderResourceLoadException clrle) {
            LOGGER.warn(String.format("%s thrown when trying to construct local resource URL",
                    ExceptionCodesEnum.CLASS_LOAD_RESOURCE_LOAD_XCPT.getDesc()));
            LOGGER.warn(clrle);
            throw clrle;
        }
    }
}
