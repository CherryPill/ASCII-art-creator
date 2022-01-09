package application.utility;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

@UtilityClass
public class FileUtil {


    private InputStream is;
    private Properties props;

    public final String EMPTY_STRING = "";
    public final String DOT_CHAR = ".";
    public final String NEW_LINE = "\n";

    static {
        try {
            is = FileUtil.class.getResourceAsStream(PathInfoHolder.getLogFileLocation());
            props = new Properties();
            props.load(is);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String inferExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(DOT_CHAR) + 1);
    }

    public String omitExtension(String fileName) {
        fileName = Optional.ofNullable(fileName).orElse(EMPTY_STRING);
        int lastDot = fileName.lastIndexOf(DOT_CHAR);
        return lastDot == 0 ? EMPTY_STRING : lastDot < 0 ? fileName : fileName.substring(0, lastDot);
    }

    public java.awt.Color getAwtColorFromFXColor(javafx.scene.paint.Color fxColor) {
        return fxColor == null ? null : new java.awt.Color((float) fxColor.getRed(),
                (float) fxColor.getGreen(),
                (float) fxColor.getBlue(),
                (float) fxColor.getOpacity());
    }

    public Boolean imageListContainsGif(List<File> files) {
        return Optional.ofNullable(files)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(i -> FileUtil.inferExtension(i.getName()))
                .anyMatch(i -> i.equals("gif"));
    }

    public String createFileListString(List<File> files) {
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

    public String inferChosenDirectory(File file) {
        return file.getParent();
    }
}
