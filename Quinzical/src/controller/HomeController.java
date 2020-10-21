package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;   
import javafx.stage.Stage;

public class HomeController {

    Alert a = new Alert(AlertType.NONE);
    
    public void onMainMenuPushed(ActionEvent event) 
    {
        Parent viewParent;
        try {
            viewParent = FXMLLoader.load(getClass().getResource("view/MainMenu.fxml"));
            Scene viewScene = new Scene(viewParent);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
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
}
