package application.io;

import application.converter.Converter;
import application.utility.Utility;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Service {
    public void generateText(File i, File o, int blocks, Converter.UI_OUTFILE_CONVERSION_TYPE conversion, Color b, Color f) throws IOException {
        String outFileName;
        Converter.CONV_TYPE type = null;
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
    }
}
