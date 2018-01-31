package application.mvc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import application.converter.Converter;
import application.io.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class MainController {
	Service ioServ = null;
	File chosenFile = null;
	File chosenDirectory = null;
	
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
		ioServ = new Service();
	}
	
	private int getSelectedBlocksNum(){
		return (int)this.comboBlocksCount.getSelectionModel().getSelectedItem();
	}
	private int getSelectedConversionType(){
		return this.comboConversionType.getSelectionModel().getSelectedIndex();
	}
	@FXML
	public void generate() throws IOException{
		int selectedBlocksNum = getSelectedBlocksNum();
		int selectedConversionType = getSelectedConversionType();
		ioServ.generateText(this.chosenFile, 
				this.chosenDirectory, 
				selectedBlocksNum,
				selectedConversionType);
	}
	@FXML
	public void specifyInput(){
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose image");
		fc.setInitialDirectory(new File(System.getProperty("user.home")));
		fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"),
										new FileChooser.ExtensionFilter("PNG", "*.png"),
										new FileChooser.ExtensionFilter("GIF", "*.gif"));
		File chosenFile = fc.showOpenDialog(null);
		if(chosenFile!=null){
			labelInputFile.setText(chosenFile.getName());
			this.chosenFile = chosenFile;
		}
	}
	@FXML
	public void specifyOutput(){
		DirectoryChooser dc = new DirectoryChooser();
		dc.setTitle("Choose ouput file directory");
		dc.setInitialDirectory(new File(System.getProperty("user.home")));
		File chosenDirectory = dc.showDialog(null);
		if(chosenDirectory!=null){
			labelOutputFile.setText(chosenDirectory.getName());
			this.chosenDirectory = chosenDirectory;
		}
	}
}
