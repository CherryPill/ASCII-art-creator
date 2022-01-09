package application.utility.files;

import lombok.Getter;

public class PathInfoHolder {

    @Getter
    private static final String logFileLocation = "/ascii_art/logs/log.txt";

    @Getter
    private static final String propFileLocation = "/props/properties.properties";

    @Getter
    private static final String progressBarFxmlLayoutLocation = "layout/pb.fxml";

    @Getter
    private static final String userHome = System.getProperty("user.home");

}
