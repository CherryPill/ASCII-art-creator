package application.io;

import application.converter.Converter;
import application.ui.WindowFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConversionService {

    private WindowFactory windowFactory = new WindowFactory();

    public void convertFiles(List<File> inputFiles,
                             File o,
                             int blocks,
                             Converter.UI_OUTFILE_CONVERSION_TYPE conversion,
                             Color b,
                             Color f) throws IOException {
        Stage progressBarWindow = windowFactory.createWindow(
                Modality.WINDOW_MODAL,
                "Processing",
                new Double[]{400D, 400D},
                new Double[]{400D, 400D});

        progressBarWindow.show();

        /*String outFileName;
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
            Converter c = new Converter(i,
                    blocks,
                    type,
                    Utility.getAwtColorFromFXColor(b),
                    Utility.getAwtColorFromFXColor(f));
            c.setOutFile(outFileName);
            c.convert(o);
        }*/
    }
}
