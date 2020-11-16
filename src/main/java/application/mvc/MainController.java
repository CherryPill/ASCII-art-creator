package application.mvc;

import application.constants.AppConstants;
import application.enums.FileConversionType;
import application.io.ConversionService;
import application.utility.MessageWrapper;
import application.utility.Utility;
import application.validator.ValidationResultEntry;
import application.validator.Validator;
import exceptions.ClassLoaderResourceLoadException;
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
import java.util.Optional;

public class MainController {

    private String recentDir;
    private ConversionService conversionService;
    private List<File> chosenFiles;
    private File chosenDirectory;

    private Validator validator = new Validator();

    @FXML
    ColorPicker colorPickerTwoColorsBack, colorPickerTwoColorsFore, colorPickerAllColorsBack;
    @FXML
    RadioButton radioTwoColors, radioAllColors;
    @FXML
    VBox vBoxImageOptions;

    private ObservableList<Integer> blocksCountComboValues = FXCollections.observableArrayList(
            8,
            8 << 1,
            8 << 2);
    private ObservableList<String> conversionTypeComboValues = FXCollections.observableArrayList(
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

    private FileConversionType getSelectedOutFileConversionType() {
        return FileConversionType
                .values()
                [this.comboConversionType.getSelectionModel().getSelectedIndex()];
    }

    @FXML
    public void generate() throws IOException, ClassLoaderResourceLoadException {
        int selectedBlocksNum = getSelectedBlocksNum();
        FileConversionType selectedConversionType = getSelectedOutFileConversionType();
        Color chosenForeground = null;
        Color chosenBackground;

        if (validateFields()) {
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

    private boolean validateFields() {
        ValidationResultEntry validationResultEntry = validator.validate(chosenFiles, chosenDirectory);
        if (validationResultEntry.isError()) {
            MessageWrapper.showAllMessages(validationResultEntry);
        }
        return !validationResultEntry.isError();
    }

    @FXML
    public void specifyInput() {
        final FileChooser fc = new FileChooser();
        fc.setTitle(AppConstants.UIConstants.FxLabel.CHOOSE_IMAGE_DIALOG_TXT);
        Optional.ofNullable(recentDir)
                .ifPresentOrElse(
                        recentDir -> fc.setInitialDirectory(new File(recentDir)),
                        () -> fc.setInitialDirectory(new File(System.getProperty("user.home"))));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All images", "*.jpg",
                        "*.png", "*.gif"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"));
        List<File> chosenFiles = fc.showOpenMultipleDialog(null);
        if (Optional.ofNullable(chosenFiles).isPresent()) {
            labelInputFile.setText(Utility.createFileListString(chosenFiles));
            this.chosenFiles = chosenFiles;
            recentDir = Utility.inferChosenDirectory(chosenFiles.get(0));
            if (Utility.imageListContainsGif(chosenFiles)) {
                comboConversionType.getSelectionModel().select(1);
                comboConversionType.setDisable(true);
                MessageWrapper.showMessage(AlertType.WARNING, AppConstants.UIConstants.Message.Warn.NO_GIF_TEXT);
            } else {
                comboConversionType.getSelectionModel().select(0);
                comboConversionType.setDisable(false);
            }
        }
    }

    @FXML
    public void specifyOutput() {
        final DirectoryChooser dc = new DirectoryChooser();
        Optional.ofNullable(recentDir)
                .ifPresentOrElse(
                        recentDir -> dc.setInitialDirectory(new File(recentDir)),
                        () -> dc.setInitialDirectory(new File(System.getProperty("user.home"))));
        dc.setTitle(AppConstants.UIConstants.FxLabel.CHOOSE_OUT_DIR_DIALOG_TXT);
        File chosenDirectory = dc.showDialog(null);
        if (Optional.ofNullable(chosenDirectory).isPresent()) {
            labelOutputFile.setText(chosenDirectory.getName());
            this.chosenDirectory = chosenDirectory;
            recentDir = chosenDirectory.getAbsolutePath();
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
