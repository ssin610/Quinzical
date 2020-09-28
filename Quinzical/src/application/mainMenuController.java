package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;




public class mainMenuController implements Initializable {
	Alert a = new Alert(AlertType.NONE);
	@FXML
	Button prac;
	
	public void quit() {
		Platform.exit();
	}
	
	public void gameMode(ActionEvent event) throws IOException {
		Parent viewParent = FXMLLoader.load(getClass().getResource("questionBoard.fxml"));
		Scene viewScene = new Scene(viewParent);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(viewScene);
		window.show();
	}
	
	public void practiceMode() {
		
		//Open new winodw start the game
    	try {
    		
    		Stage thisstage = (Stage)prac.getScene().getWindow();
    		//Load GUI process
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("PracticeStart.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage secondStage = new Stage();
			secondStage.setScene(scene);
			secondStage.show();
			thisstage.close();
			
		} catch(Exception e) {
			e.printStackTrace();
			a.setAlertType(AlertType.ERROR); 
            // show the dialog 
            a.show(); 
            a.setHeaderText("File Reading Error");
            a.setContentText("Please check the file arrangment");
		}
	}

	
	public void initialize(URL url, ResourceBundle rb) {
		
    }
	
	
    
}
