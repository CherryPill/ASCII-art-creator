package application.mvc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class MainController {

	ObservableList<Integer> blocksCountComboValues = FXCollections.observableArrayList(
			8,
			8<<1,
			8<<2);
	ObservableList<String> conversionTypeComboValues = FXCollections.observableArrayList(
			"Text", 
			"Image");
	
	@FXML Button buttonInputFile, buttonOutputFile, buttonGenerate;
	@FXML Label labelInputFile, labelOutputFile;
	@FXML ComboBox comboBlocksCount, comboConversionType;
	
	@FXML
	public void initialize(){
		comboBlocksCount.setItems(blocksCountComboValues);
		comboConversionType.setItems(conversionTypeComboValues);
		comboBlocksCount.getSelectionModel().select(0);
		comboConversionType.getSelectionModel().select(0);
	}
	
	@FXML
	public void generate(){
		
	}
	@FXML
	public void specifyInput(){
		
	}
	@FXML
	public void specifyOutput(){
		
	}
	
}
