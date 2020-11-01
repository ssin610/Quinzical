package main;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;

import helper.GameData;

public class Main extends Application {

	private Alert a = new Alert(AlertType.NONE); // Used to display warnings

	/**
	 * Set/show the primary stage and call method to read saved game data from files
	 * if they exist
	 * @param primaryStage the main stage for the application
	 */
	@Override
	public void start(Stage primaryStage) {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/view/Home.fxml"));
			primaryStage.setTitle("Quinzical");
			primaryStage.setScene(new Scene(root, 1300, 700));
			primaryStage.setResizable(false);
			primaryStage.show();

			GameData.readSavedData();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Main method to launch the application
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * This method is called when the application exits.
	 * Call the GameData method to save the game data
	 */
	@Override
	public void stop() throws IOException {
		
		GameData.writeSavedData();

		// Stop audio from playing when windows are closed
		Thread thread = new Thread() {
			@Override
			public void run() {
				String cmd = "spd-say \" \"";
				ProcessBuilder ttsBuilder = new ProcessBuilder("bash", "-c", cmd);
				try {
					ttsBuilder.start();
				} catch (IOException e) {
					a.setAlertType(AlertType.ERROR);
					a.show();
					a.setHeaderText("Audio System Crash");
					a.setContentText("Please make sure spd-say is installed (in README.md) and restart the game");
				}
			}
		};
		thread.start();
	}
}