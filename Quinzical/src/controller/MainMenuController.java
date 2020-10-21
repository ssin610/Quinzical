package controller;

import java.io.IOException;

import application.Main;
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
	
			Parent viewParent;
			if (Main.getRandom()) {
				viewParent = FXMLLoader.load(getClass().getResource("view/ChooseCategories.fxml"));
			}
			else {
				viewParent = FXMLLoader.load(getClass().getResource("view/ClueGrid.fxml"));
			}
			Scene viewScene = new Scene(viewParent);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(viewScene);
			window.setResizable(false);
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
			window.setResizable(false);
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
	
	public void onResetPushed(ActionEvent event){
        Parent viewParent;
        try {
            viewParent = FXMLLoader.load(getClass().getResource("view/Reset.fxml"));
            Scene viewScene = new Scene(viewParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(viewScene);
            window.setResizable(false);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
			a.setAlertType(AlertType.ERROR);
			// show the dialog
			a.show();
			a.setHeaderText("Fatal Error");
			a.setContentText("Please restart the game");
        }
    }

	public void onExit() {
		Platform.exit();
	}
}
