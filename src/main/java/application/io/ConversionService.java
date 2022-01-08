package application.io;

import application.converter.ConverterWorker;
import application.converters.Converter;
import application.converters.text.TextConverter;
import application.dto.InputImageSettingsDto;
import application.dto.InputInfoDto;
import application.enums.ExceptionCodes;
import application.enums.ExceptionFatality;
import application.enums.ui.FileConversionType;
import application.ui.WindowFactory;
import application.utility.MessageUtil;
import application.utility.MessageUtils;
import exceptions.ClassLoaderResourceLoadException;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ConversionService {

    private final WindowFactory windowFactory = new WindowFactory();

    public void convertFiles(InputInfoDto inputInfoDto) {

        Optional<Stage> progressBarWindow = Optional.empty();
        try {
            progressBarWindow = windowFactory.createWindowFromFXML(
                    Modality.APPLICATION_MODAL);
            progressBarWindow.ifPresent(Stage::show);
        } catch (IOException ioe) {
            MessageUtil.showMessage(Alert.AlertType.WARNING,
                    MessageUtils.buildExceptionMessageString(ExceptionCodes.JVM_GENERIC_IO_XCPT,
                            ExceptionFatality.NON_FATAL));
        } catch (ClassLoaderResourceLoadException classLoaderException) {
            MessageUtil.showMessage(Alert.AlertType.WARNING,
                    MessageUtils.buildExceptionMessageString(ExceptionCodes.CLASS_LOAD_RESOURCE_LOAD_XCPT,
                            ExceptionFatality.NON_FATAL));
        }

        ConverterWorker<Converter> conversionTask = ConverterWorker.builder()
                .converter(fetchAppropriateConverter(inputInfoDto))
                .inputInfoDto(inputInfoDto)
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
            MessageUtil.showMessage(Alert.AlertType.INFORMATION, "Finished converting");
        });
        new Thread(conversionTask).start();
    }

    private Converter fetchAppropriateConverter(InputInfoDto inputInfoDto) {
        if (FileConversionType.TEXT.equals(inputInfoDto.getFileInfoDto().getFileConversionType())) {
            return new TextConverter();
        } else if (FileConversionType.IMG) {

        }
    }
}
