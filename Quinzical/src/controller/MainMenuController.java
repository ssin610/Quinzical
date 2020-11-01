package controller;

import java.io.IOException;

import helper.GameData;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * Controller for the MainMenu scene
 */
public class MainMenuController {

	private Alert a = new Alert(AlertType.NONE);

	/**
	 * Called when the user presses the game mode button. Depending on whether
	 * the user has chosen their 5 categories or not, they are directed to 
	 * the relevant screen
	 */
	public void onGameModePushed(ActionEvent event) {

		try {

			Parent viewParent;
			if (GameData.getCatSelected()) {
				viewParent = FXMLLoader.load(getClass().getResource("/view/ChooseCategories.fxml"));
			}
			else {
				viewParent = FXMLLoader.load(getClass().getResource("/view/ClueGrid.fxml"));
			}
			Scene viewScene = new Scene(viewParent);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(viewScene);
			window.setResizable(false);
			window.show();
		} catch (IOException e) {
			e.printStackTrace();

			a.setAlertType(AlertType.ERROR); 
			a.show(); 
			a.setHeaderText("File Reading Error");
			a.setContentText("Please check the file arrangment");
		}
	}

	/**
	 * Called when the user presses the practise mode button. The user
	 * is then directed to the practise start scene
	 */
	public void onPractiseModePushed(ActionEvent event) {

		try {
			Parent viewParent = FXMLLoader.load(getClass().getResource("/view/PractiseStart.fxml"));
			Scene viewScene = new Scene(viewParent);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(viewScene);
			window.setResizable(false);
			window.show();

		} catch(Exception e) {
			e.printStackTrace();

			a.setAlertType(AlertType.ERROR); 
			a.show(); 
			a.setHeaderText("File Reading Error");
			a.setContentText("Please check the file arrangment");
		}
	}

	/**
	 * Called when the user presses the reset button. The user
	 * is then directed to the reset scene
	 */
	public void onResetPushed(ActionEvent event){

		try {
			Parent viewParent = FXMLLoader.load(getClass().getResource("/view/Reset.fxml"));
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
	 * Called when the user presses the help button. A dialog box
	 * then pops up indicating that they should read the user manual
	 * if they have any queries about the app
	 */
	public void onHelpPushed(){

		// show alert
		a.setAlertType(AlertType.INFORMATION);
		a.setHeaderText("Please refer to the manual for help!");
		a.setContentText("There is a detailed user manual that comes included with this app! It is recommended to read the manual before using this app to"
				+ " clarify all aspects of the tool.");

		a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		a.show();
	}

	/**
	 * Called when the user presses the exit button. This leads
	 * to the stop method in Main
	 */
	public void onExit() {
		Platform.exit();
	}
}