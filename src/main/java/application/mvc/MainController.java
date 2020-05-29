package application.mvc;

import application.converter.Converter;
import application.io.ConversionService;
import application.utility.MessageWrapper;
import application.utility.Utility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainController {

    private ConversionService conversionService = null;
    private List<File> chosenFiles = null;
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
        comboConversionType.getSelectionModel().select(1);
        toggleImageOptions();
        conversionService = new ConversionService();
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

    private Converter.UI_OUTFILE_CONVERSION_TYPE getSelectedOutFileConversionType() {
        return Converter.UI_OUTFILE_CONVERSION_TYPE
                .values()
                [this.comboConversionType.getSelectionModel().getSelectedIndex()];
    }

    @FXML
    public void generate() throws IOException {
        int selectedBlocksNum = getSelectedBlocksNum();
        Converter.UI_OUTFILE_CONVERSION_TYPE selectedConversionType = getSelectedOutFileConversionType();
        Color chosenForeground = null;
        Color chosenBackground;

        if (validateFields(selectedConversionType)) {
            if (radioTwoColors.isSelected()) {
                chosenBackground = colorPickerTwoColorsBack.getValue();
                chosenForeground = colorPickerTwoColorsFore.getValue();
            } else {
                chosenBackground = colorPickerAllColorsBack.getValue();
            }
            conversionService.convertFiles(
                    this.chosenFiles,
                    this.chosenDirectory,
                    selectedBlocksNum,
                    selectedConversionType,
                    chosenBackground,
                    chosenForeground);
        }
    }

    private boolean validateFields(Converter.UI_OUTFILE_CONVERSION_TYPE selectedConversionType) {
        if (chosenFiles != null) {
            if (chosenDirectory != null) {
                if (selectedConversionType == Converter.UI_OUTFILE_CONVERSION_TYPE.IMG) {
                    if (radioTwoColors.isSelected() || radioAllColors.isSelected()) {
                        return true;
                    } else {
                        MessageWrapper.showMessage(Utility.getProps().getProperty("ui.msg.err.no_color_chosen"), AlertType.ERROR);
                        return false;
                    }
                } else if (selectedConversionType == Converter.UI_OUTFILE_CONVERSION_TYPE.TEXT) {
                    if (Utility.imageListContainsGif(chosenFiles)) {
                        MessageWrapper.showMessage(Utility.getProps().getProperty("ui.msg.err.gif_to_text"), AlertType.WARNING);
                        return false;
                    }
                }
                return true;
            } else {
                MessageWrapper.showMessage(Utility.getProps().getProperty("ui.msg.err.no_dir_chosen"), AlertType.ERROR);
                return false;
            }
        } else {
            MessageWrapper.showMessage(Utility.getProps().getProperty("ui.msg.err.no_input_file"), AlertType.ERROR);
            return false;
        }
    }

    @FXML
    public void specifyInput() {
        FileChooser fc = new FileChooser();
        fc.setTitle(Utility.getProps().getProperty("ui.lbl.choose_img"));
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All images", "*.jpg",
                        "*.png", "*.gif"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"));
        List<File> chosenFiles = fc.showOpenMultipleDialog(null);
        if (chosenFiles != null) {
            labelInputFile.setText(Utility.createFileListString(chosenFiles));
            this.chosenFiles = chosenFiles;
        }
        if (Utility.imageListContainsGif(chosenFiles)) {
            comboConversionType.getSelectionModel().select(1);
            comboConversionType.setDisable(true);
            MessageWrapper.showMessage(Utility.getProps().getProperty("ui.msg.err.gif_to_text"), AlertType.WARNING);
        } else {
            comboConversionType.getSelectionModel().select(0);
            comboConversionType.setDisable(false);
        }
    }

    @FXML
    public void specifyOutput() {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle(Utility.getProps().getProperty("ui.lbl.choose_out_file_dir"));
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
