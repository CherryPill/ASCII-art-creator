package application.utility;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

public class Utility {

    private static InputStream is;
    private static Properties props;

    public static final String EMPTY_STRING = "";
    public static final String DOT_CHAR = ".";
    public static final String NEW_LINE = "\n";


    static {
        try {
            is = Utility.class.getResourceAsStream("/props/properties.properties");
            props = new Properties();
            props.load(is);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String inferExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(DOT_CHAR) + 1);
    }

    public static String omitExtension(String fileName) {
        fileName = Optional.ofNullable(fileName).orElse(EMPTY_STRING);
        int lastDot = fileName.lastIndexOf(DOT_CHAR);
        return lastDot == 0 ? EMPTY_STRING : lastDot < 0 ? fileName : fileName.substring(0, lastDot);
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

    public static Boolean imageListContainsGif(List<File> files) {
        return Optional.ofNullable(files)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(i -> Utility.inferExtension(i.getName()))
                .anyMatch(i -> i.equals("gif"));
    }

    public static String createFileListString(List<File> files) {
        List<File> files_ = Optional.ofNullable(files)
                .orElseGet(Collections::emptyList);
        List<String> stringList = files_
                .stream()
                .map(File::getName)
                .map(i ->
                        i.length() > 10 ? i.substring(0, 2) + "." +
                                i.substring(i.lastIndexOf(".")) : i)
                .limit(2)
                .collect(Collectors.toList());
        int filteredSize = stringList.size();
        String finalString = String.join(", ", stringList);
        return finalString.concat(((files_.size() - filteredSize) > 0 ? " and " + (files_.size() - filteredSize) + " other files" : ""));
    }
}
