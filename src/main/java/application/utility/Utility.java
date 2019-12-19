package application.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class Utility {

    private static InputStream is;
    private static Properties props;

    static {
        try {
            is = Utility.class.getResourceAsStream("/props/properties.properties");
            props = new Properties();
            props.load(is);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String inferExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static String omitExtension(String fileName) {
        String[] splitArr = fileName.split("\\.");
        System.out.println(splitArr[0]);
        return splitArr[0];
    }

    public static java.awt.Color getAwtColorFromFXColor(javafx.scene.paint.Color fxColor) {
        return fxColor == null ? null : new java.awt.Color((float) fxColor.getRed(),
                (float) fxColor.getGreen(),
                (float) fxColor.getBlue(),
                (float) fxColor.getOpacity());
    }

    public static Properties getProps() {
        return props;
    }

    public static String concatListStrings(List<File> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(a -> {
            sb.append(a.getName());
            sb.append(props.getProperty("sys.core.default.delimiter"));
        });
        return sb.toString();
    }
}
