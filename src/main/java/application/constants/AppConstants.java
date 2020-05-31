package application.constants;

public final class AppConstants {
    private AppConstants() {
    }

    public static class UIConstants {
        public static class Window {
            public static final double MAIN_WINDOW_WIDTH = 600;
            public static final double MAIN_WINDOW_HEIGHT = 400;

            public static final double PB_WINDOW_WIDTH = 251;
            public static final double PB_WINDOW_HEIGHT = 127;

            public static final String MAIN_WINDOW_APP_NAME = "ASCII Art creator";
        }

        public static class Message {
            public static class Error {
                public static final String NO_DIR_CHOSEN = "No output directory chosen";
                public static final String NO_INPUT_FILE_CHOSEN = "No output directory chosen";
                public static final String NO_COLOR_CHOSEN = "Please choose one of color options";
            }

            public static class Warn {
                public static final String NO_GIF_TEXT = "Animated gif file detected. Only conversion to image is now available";
            }

            public static String[] AlertStrings = {"Message", "Information", "Warning", "Confirmation", "Error"};
        }

        public static class FxLabel {
            public static final String CHOOSE_IMAGE_DIALOG_TXT = "Choose image";
            public static final String CHOOSE_OUT_DIR_DIALOG_TXT = "Choose output file directory";

        }
    }

    public static class Logic {
        public static final String CORE_CHARS = "-+^=>?$&@#";
    }

    public static class System {
        public static final String NO_WRITERS_FOUND = "No writers found";
    }

}
