package application.ui;

import javafx.scene.layout.Pane;
import javafx.stage.Modality;

public class WindowSettings {
    private Modality windowModality;
    private String windowTitle;
    private Pane windowParent;
    private Integer[] windowPosition;
    private Double[] windowDimensions;

    public Modality getWindowModality() {
        return windowModality;
    }

    public void setWindowModality(Modality windowModality) {
        this.windowModality = windowModality;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public Pane getWindowParent() {
        return windowParent;
    }

    public void setWindowParent(Pane windowParent) {
        this.windowParent = windowParent;
    }

    public Integer[] getWindowPosition() {
        return windowPosition;
    }

    public void setWindowPosition(Integer[] windowPosition) {
        this.windowPosition = windowPosition;
    }

    public Double[] getWindowDimensions() {
        return windowDimensions;
    }

    public void setWindowDimensions(Double[] windowDimensions) {
        this.windowDimensions = windowDimensions;
    }

    public WindowSettings(Modality windowModality) {
        this.windowModality = windowModality;
    }
}
