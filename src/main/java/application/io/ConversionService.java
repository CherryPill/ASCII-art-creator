package application.io;

import application.converter.Converter;
import application.ui.WindowFactory;
import application.utility.MessageWrapper;
import application.utility.Utility;
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

public class ConversionService {

    private WindowFactory windowFactory = new WindowFactory();

    public void convertFiles(List<File> inputFiles,
                             File outputDir,
                             int blocks,
                             Converter.UI_OUTFILE_CONVERSION_TYPE conversionType,
                             Color backgroundColor,
                             Color foregroundColor) throws IOException {
        Stage progressBarWindow = windowFactory.createWindowFromFXML(
                Modality.APPLICATION_MODAL);
        progressBarWindow.show();

        Converter conversionTask = Converter.initBuilder()
                .withInputFiles(inputFiles)
                .withOutputDir(outputDir)
                .withConversionType(conversionType)
                .withBlockSize(blocks)
                .withBgColor(Utility.getAwtColorFromFXColor(backgroundColor))
                .withFgColor(Utility.getAwtColorFromFXColor(foregroundColor))
                .build();

        ProgressBar pbNode = (ProgressBar) progressBarWindow.getScene().lookup("#perFrameProgressBar");
        pbNode.progressProperty().bind(conversionTask.progressProperty());
        Label individualProgressBarLabel = (Label) progressBarWindow.getScene().lookup("#individualProgressLabel");
        individualProgressBarLabel.textProperty().bind(
                Bindings.createStringBinding(
                        conversionTask::getMessage,
                        conversionTask.messageProperty()
                )
        );
        conversionTask.setOnSucceeded(event -> {
            progressBarWindow.close();
            MessageWrapper.showMessage("Finished converting",
                    Alert.AlertType.INFORMATION);
        });
        new Thread(conversionTask).start();
    }
}
