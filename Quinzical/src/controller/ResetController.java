package controller;


import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.Node;
import javafx.stage.Stage;

import helper.GameData;

/**
 * Controller for the Reset scene
 */
public class ResetController implements Initializable {

	@FXML
	Text resetText;

	@FXML
	Text beatenText;

	private Alert a = new Alert(AlertType.NONE);

	/**
	 * Initialize this controller by setting the relevant FXML elements
	 * and their values
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		resetText.setText("You have winnings of $" + GameData.getWinnings() + "! Do you want to reset the game?");
		resetText.setFont(Font.font("System", FontWeight.BOLD, 50));
		generateComparativeMessage();
	}

	/**
	 * This method generates a comparative message to the user so
	 * they can compare their results with others upon completion
	 * of the game
	 */
	public void generateComparativeMessage() {
		
		// Generate a comparative message to the user
		try {
			double percent = (GameData.getWinnings()/ GameData.getTotalWinnings()) * 100;
			percent=Math.round(percent * 100.0) / 100.0;

			if(percent >60) {
				beatenText.setText("=== Great Work! You've beaten "+ percent+"% players! ===");
			}else if (percent<60 && percent >0) {
				beatenText.setText("=== Keep Working! You've beaten "+ percent+"% players! ===");
			}else if (GameData.getWinnings()>0){
				beatenText.setText("=== Not bad! Next time you can do better! ===");
			}else {
				beatenText.setText("=== Keep practising! Next time you can do better! ===");
			}

		} catch (Exception e) {
			beatenText.setText("=== Its time to start Game mode! ==="); // If no previous total wings stored
		}
	}

	/**
	 * Called when the user presses the confirm reset button. A 
	 * method is called to reset the game data
	 */
	public void onYesButton(ActionEvent event) {
		
		GameData.resetData();

		try {
			Parent viewParent = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
			Scene viewScene = new Scene(viewParent);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(viewScene);
			window.setResizable(false);
			window.show();
		} catch (IOException e) {
			e.printStackTrace();

			a.setAlertType(AlertType.ERROR);
			a.show();
			a.setHeaderText("Fatal Error");
			a.setContentText("Please restart the game");
		}
	}

	/**
	 * Called when the user doesn't want to reset the game. This method
	 * then simply returns the user back to the main menu
	 */
	public void onNoButton(ActionEvent event) {
		
		try {
			Parent viewParent = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
			Scene viewScene = new Scene(viewParent);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(viewScene);
			window.setResizable(false);
			window.show();
		} catch (IOException e) {
			e.printStackTrace();

			a.setAlertType(AlertType.ERROR);
			a.show();
			a.setHeaderText("Fatal Error");
			a.setContentText("Please restart the game");
		}
	}
}