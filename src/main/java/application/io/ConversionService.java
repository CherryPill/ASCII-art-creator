package application.io;

import application.converter.Converter;
import application.enums.ExceptionCodes;
import application.enums.ExceptionFatality;
import application.enums.ui.FileConversionType;
import application.ui.WindowFactory;
import application.utility.MessageUtils;
import application.utility.MessageWrapper;
import application.utility.Utility;
import exceptions.ClassLoaderResourceLoadException;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ConversionService {

    private WindowFactory windowFactory = new WindowFactory();

    public void convertFiles(List<File> inputFiles,
                             File outputDir,
                             int blocks,
                             FileConversionType conversionType,
                             Color backgroundColor,
                             Color foregroundColor) {

        Optional<Stage> progressBarWindow = Optional.empty();
        try {
            progressBarWindow = windowFactory.createWindowFromFXML(
                    Modality.APPLICATION_MODAL);
            progressBarWindow.ifPresent(Stage::show);
        } catch (IOException ioe) {
            MessageWrapper.showMessage(Alert.AlertType.WARNING,
                    MessageUtils.buildExceptionMessageString(ExceptionCodes.JVM_GENERIC_IO_XCPT,
                            ExceptionFatality.NON_FATAL));
        } catch (ClassLoaderResourceLoadException clrle) {
            MessageWrapper.showMessage(Alert.AlertType.WARNING,
                    MessageUtils.buildExceptionMessageString(ExceptionCodes.CLASS_LOAD_RESOURCE_LOAD_XCPT,
                            ExceptionFatality.NON_FATAL));
        }

        Converter conversionTask = Converter.initBuilder()
                .withInputFiles(inputFiles)
                .withOutputDir(outputDir)
                .withConversionType(conversionType)
                .withBlockSize(blocks)
                .withBgColor(Utility.getAwtColorFromFXColor(backgroundColor))
                .withFgColor(Utility.getAwtColorFromFXColor(foregroundColor))
                .build();

        progressBarWindow.ifPresent(pb -> {
            ProgressBar pbNode = (ProgressBar) pb.getScene().lookup("#perFrameProgressBar");
            pbNode.progressProperty().bind(conversionTask.progressProperty());
            Label individualProgressBarLabel = (Label) pb.getScene().lookup("#individualProgressLabel");
            individualProgressBarLabel.textProperty().bind(
                    Bindings.createStringBinding(
                            conversionTask::getMessage,
                            conversionTask.messageProperty()
                    )
            );
        });
        Optional<Stage> finalProgressBarWindow = progressBarWindow;
        conversionTask.setOnSucceeded(event -> {
            finalProgressBarWindow.ifPresent(Stage::close);
            MessageWrapper.showMessage(Alert.AlertType.INFORMATION, "Finished converting");
        });
        new Thread(conversionTask).start();
    }
}
