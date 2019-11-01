package application.mvc;

import java.io.File;
import java.io.IOException;
import application.io.Service;
import application.utility.MessageWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class MainController {

    private Service ioServ = null;
    private File chosenFile = null;
    private File chosenDirectory = null;

    @FXML
    ColorPicker colorPickerTwoColorsBack, colorPickerTwoColorsFore, colorPickerAllColorsBack;
    @FXML
    RadioButton radioTwoColors, radioAllColors;
    @FXML
    VBox vBoxImageOptions;
    ObservableList<Integer> blocksCountComboValues = FXCollections.observableArrayList(
            8,
            8 << 1,
            8 << 2);
    ObservableList<String> conversionTypeComboValues = FXCollections.observableArrayList(
            "Text",
            "Image");
    @FXML
    Button buttonInputFile, buttonOutputFile, buttonGenerate;
    @FXML
    Label labelInputFile, labelOutputFile;
    @FXML
    ComboBox comboBlocksCount, comboConversionType;

    @FXML
    public void initialize() {
        comboBlocksCount.setItems(blocksCountComboValues);
        comboConversionType.setItems(conversionTypeComboValues);
        comboBlocksCount.getSelectionModel().select(0);
        comboConversionType.getSelectionModel().select(0);
        toggleImageOptions();
        ioServ = new Service();
    }

    public void toggleImageOptions() {
        if (comboConversionType.getSelectionModel().getSelectedIndex() == 0) {
            vBoxImageOptions.setDisable(true);
        } else {
            vBoxImageOptions.setDisable(false);
        }
    }

    private int getSelectedBlocksNum() {
        return (int) this.comboBlocksCount.getSelectionModel().getSelectedItem();
    }

    private int getSelectedConversionType() {
        return this.comboConversionType.getSelectionModel().getSelectedIndex();
    }

    @FXML
    public void generate() throws IOException {
        int selectedBlocksNum = getSelectedBlocksNum();
        int selectedConversionType = getSelectedConversionType();
        Color chosenForeground = null;
        Color chosenBackground;

        if (validateFields(selectedConversionType)) {
            if (radioTwoColors.isSelected()) {
                chosenBackground = colorPickerTwoColorsBack.getValue();
                chosenForeground = colorPickerTwoColorsFore.getValue();
            } else {
                chosenBackground = colorPickerAllColorsBack.getValue();
            }
            ioServ.generateText(this.chosenFile,
                    this.chosenDirectory,
                    selectedBlocksNum,
                    selectedConversionType,
                    chosenBackground,
                    chosenForeground);
        }
    }

    private boolean validateFields(int selectedConversionType) {
        if (chosenFile != null) {
            if (chosenDirectory != null) {
                if (selectedConversionType == 1) {
                    if (radioTwoColors.isSelected() || radioAllColors.isSelected()) {
                        return true;
                    } else {
                        MessageWrapper.showMessage("Please choose one of color options", AlertType.ERROR);
                        return false;
                    }
                }
                return true;
            } else {
                MessageWrapper.showMessage("No output directory chosen", AlertType.ERROR);
                return false;
            }
        } else {
            MessageWrapper.showMessage("No input file chosen", AlertType.ERROR);
            return false;
        }
    }

    @FXML
    public void specifyInput() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose image");
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"));
        File chosenFile = fc.showOpenDialog(null);
        if (chosenFile != null) {
            labelInputFile.setText(chosenFile.getName());
            this.chosenFile = chosenFile;
        }
    }

    @FXML
    public void specifyOutput() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("Choose ouput file directory");
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        File chosenDirectory = dc.showDialog(null);
        if (chosenDirectory != null) {
            labelOutputFile.setText(chosenDirectory.getName());
            this.chosenDirectory = chosenDirectory;
        }
    }

    @FXML
    public void handleRadioPress(ActionEvent event) {
        RadioButton pressedBtn = (RadioButton) event.getSource();
        String pressedBtnID = pressedBtn.getId();
        if (pressedBtnID.equals("radioAllColors")) {
            radioTwoColors.setSelected(false);
            radioAllColors.setSelected(true);
        } else if (pressedBtnID.equals("radioTwoColors")) {
            radioTwoColors.setSelected(true);
            radioAllColors.setSelected(false);
        }
    }
}
