package application.io;

import application.converter.Converter;
import application.ui.WindowFactory;
import application.utility.Utility;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ConversionService {

    private WindowFactory windowFactory = new WindowFactory();

    public void convertFiles(List<File> inputFiles,
                             File o,
                             int blocks,
                             Converter.UI_OUTFILE_CONVERSION_TYPE conversion,
                             Color b,
                             Color f) throws IOException {
        Stage progressBarWindow = windowFactory.createWindowFromFXML(
                Modality.WINDOW_MODAL);
        progressBarWindow.show();
        String outFileName;
        Converter.CONV_TYPE type = null;
        for (File i : inputFiles) {
            if (conversion == Converter.UI_OUTFILE_CONVERSION_TYPE.IMG) {
                outFileName = Utility.omitExtension(i.getName()) + "_" + UUID.randomUUID() + "." + Utility.inferExtension(i.getName());
                if (Utility.inferExtension(i.getName()).equals("gif")) {
                    type = Converter.CONV_TYPE.IMG_GIF;
                } else {
                    type = Converter.CONV_TYPE.IMG_OTHER;
                }
            } else {
                outFileName = Utility.omitExtension(i.getName()) + "_" + UUID.randomUUID() + ".txt";
                type = Converter.CONV_TYPE.TXT;
            }
            Converter conversionTask = new Converter(i,
                    o,
                    blocks,
                    type,
                    Utility.getAwtColorFromFXColor(b),
                    Utility.getAwtColorFromFXColor(f));
            conversionTask.setOutFile(outFileName);
            ProgressBar pbNode = (ProgressBar) progressBarWindow.getScene().lookup("#perFrameProgressBar");
            pbNode.progressProperty().bind(conversionTask.progressProperty());
            conversionTask.setOnSucceeded(event -> progressBarWindow.close());
            new Thread(conversionTask).start();
        }
    }
}
