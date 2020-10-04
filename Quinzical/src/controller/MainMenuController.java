package controller;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


public class MainMenuController {

	Alert a = new Alert(AlertType.NONE);

	public void onGameModePushed(ActionEvent event) {
		try {
			Parent viewParent = FXMLLoader.load(getClass().getResource("view/ClueGrid.fxml"));
			Scene viewScene = new Scene(viewParent);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(viewScene);
			window.show();
		} catch (IOException e) {
			e.printStackTrace();
			a.setAlertType(AlertType.ERROR); 
            // show the dialog 
            a.show(); 
            a.setHeaderText("File Reading Error");
            a.setContentText("Please check the file arrangment");
		}

	}
	
	public void onPracticeModePushed(ActionEvent event) {
    	try {
			Parent viewParent = FXMLLoader.load(getClass().getResource("view/PracticeStart.fxml"));
			Scene viewScene = new Scene(viewParent);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(viewScene);
			window.show();
			
		} catch(Exception e) {
			e.printStackTrace();
			a.setAlertType(AlertType.ERROR); 
            // show the dialog 
            a.show(); 
            a.setHeaderText("File Reading Error");
            a.setContentText("Please check the file arrangment");
		}
	}

	public void onExit() {
		Platform.exit();
	}
}
