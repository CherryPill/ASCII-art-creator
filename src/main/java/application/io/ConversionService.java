package application.io;

import application.converter.ConverterWorker;
import application.converters.Converter;
import application.converters.text.TextConverter;
import application.dto.InputInfoDto;
import application.enums.ExceptionCodesEnum;
import application.enums.ExceptionFatalityEnum;
import application.enums.ui.FileConversionType;
import application.ui.WindowFactory;
import application.utility.messages.MessageUtil;
import application.utility.messages.MessageUtils;
import exceptions.ClassLoaderResourceLoadException;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;

public class ConversionService {

    private static final Logger LOGGER = LogManager.getLogger(ConversionService.class);

    private final WindowFactory windowFactory = new WindowFactory();

    public void convertFiles(InputInfoDto inputInfoDto) {

        Optional<Stage> progressBarWindow = Optional.empty();
        try {
            progressBarWindow = windowFactory.createWindowFromFXML(
                    Modality.APPLICATION_MODAL);
            progressBarWindow.ifPresent(Stage::show);
        } catch (IOException ioe) {
            MessageUtil.showMessage(Alert.AlertType.WARNING,
                    MessageUtils.buildExceptionMessageString(
                            ExceptionCodesEnum.JVM_GENERIC_IO_XCPT,
                            ExceptionFatalityEnum.NON_FATAL,
                            ioe));
        } catch (ClassLoaderResourceLoadException classLoaderException) {
            MessageUtil.showMessage(Alert.AlertType.WARNING,
                    MessageUtils.buildExceptionMessageString(
                            ExceptionCodesEnum.CLASS_LOAD_RESOURCE_LOAD_XCPT,
                            ExceptionFatalityEnum.NON_FATAL,
                            classLoaderException));
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
        conversionTask.setOnFailed(
                workerStateEvent -> {
                    finalProgressBarWindow.ifPresent(Stage::close);
                    Throwable throwable = conversionTask.getException();

                    String detailedUiMsg = MessageUtils.buildExceptionMessageString(
                            ExceptionCodesEnum.JVM_IMAGE_CONVERSION_XCPT,
                            ExceptionFatalityEnum.NON_FATAL,
                            throwable);

                    MessageUtil.showMessage(Alert.AlertType.ERROR,
                            String.format("Error during conversion: %s%n", detailedUiMsg));

                    conversionTask.getException().printStackTrace();
                    LOGGER.catching(conversionTask.getException());
                }
        );

        new Thread(conversionTask).start();
    }

    private Converter fetchAppropriateConverter(InputInfoDto inputInfoDto) {
        if (FileConversionType.TEXT.equals(inputInfoDto.getFileInfoDto().getFileConversionType())) {
            return new TextConverter();
        } else if (FileConversionType.IMG) {

        }
    }
}
