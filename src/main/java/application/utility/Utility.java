package application.utility;

public class Utility {
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
}
