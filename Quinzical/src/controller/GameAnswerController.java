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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import application.Main;

public class GameAnswerController implements Initializable {

	@FXML
	Button submit_button;

	@FXML
	Button dontknow_button;

	@FXML
	Button back_button;

	@FXML
	Button audio_replay_button;
	
	@FXML
	Button textshow_button;

	@FXML
	Label question_label;

	@FXML
	Label hint_label;

	@FXML
	ProgressBar reading_bar;

	@FXML
	Slider volume_slider;

	@FXML
	TextField user_input;

	@FXML
	Label winnings;

	// store information about the particular question
	private static String question;
	private static String answer;
	private static String bracket;
	private static int value;

	private String _speed = "0";
	Alert a = new Alert(AlertType.NONE);

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		back_button.setDisable(true);
		question_label.setText(question);
		winnings.setText("Winnings: $" + Integer.toString(Main.getWinnings()));
		user_input.setPromptText(bracket);
		Main.addAnsweredQuestion(question);
		speak(question);
		// Add lisenter to the slider
		volume_slider.setOnMouseReleased(event -> {
			int temp = (int) volume_slider.getValue();
			_speed = Integer.toString(temp);
		});
	}

	/**
	 * Use spd-say to speak & in the worker thread
	 * @param sentence the sentence to speak
	 */
	public void speak(String sentence) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				String cmd = "spd-say -r" + _speed + " \"" + sentence + "\"";
				ProcessBuilder ttsBuilder = new ProcessBuilder("bash", "-c", cmd);
				try {
					Process ttsProcess = ttsBuilder.start();
				} catch (IOException e) {
					e.printStackTrace();
					a.setAlertType(AlertType.ERROR);
					// show the dialog
					a.show();
					a.setHeaderText("Audio System Crash");
					a.setContentText("Please make sure spd-say is installed (in READ.md) and restart the game");
				}
			}
		};
		thread.setName("thread1");
		thread.start();
	}

	public void replay(ActionEvent event) {
		speak(question);
	}
	public void showText(ActionEvent event) {
		question_label.setVisible(true);
	}

	public void onMainMenuPushed(ActionEvent event) {
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
			a.setHeaderText("Fatal Error");
			a.setContentText("Please restart the game");
		}
	}

	public void onSubmitButtonPushed(ActionEvent event) {
		String input = user_input.getText();
		if (input.trim().equalsIgnoreCase(answer.trim()) || input.trim().equalsIgnoreCase(bracket + answer.trim())
				|| input.trim().equalsIgnoreCase(bracket + " " + answer.trim())) {
			hint_label.setVisible(true);
			hint_label.setText("Correct! $" + value + " has been added to your winnings!");
			speak("Correct! $" + value + " has been added to your winnings!");
			Main.setWinnings(value);
			submit_button.setDisable(true);
			audio_replay_button.setDisable(true);
			back_button.setDisable(false);
			back_button.setVisible(true);
			dontknow_button.setDisable(true);
			textshow_button.setDisable(true);
		} else {
			hint_label.setVisible(true);
			hint_label.setText("Incorrect. The correct answer was: " + bracket + " " + answer);
			speak("Incorrect. The correct answer was: " + bracket + " " + answer);
			Main.setWinnings(-value);
			submit_button.setDisable(true);
			audio_replay_button.setDisable(true);
			back_button.setDisable(false);
			back_button.setVisible(true);
			dontknow_button.setDisable(true);
			textshow_button.setDisable(true);

		}
		winnings.setText("Winnings: $" + Integer.toString(Main.getWinnings()));
	}

	public void onDontKnowPushed(ActionEvent event) {
		hint_label.setVisible(true);
		hint_label.setText("The correct answer was: " + bracket + " " + answer);
		speak("The correct answer was: " + bracket + " " + answer);
		Main.setWinnings(-value);
		submit_button.setDisable(true);
		dontknow_button.setDisable(true);
		audio_replay_button.setDisable(true);
		back_button.setDisable(false);
		back_button.setVisible(true);
		winnings.setText("Winnings: $" + Integer.toString(Main.getWinnings()));
		textshow_button.setDisable(true);
	}

	public static void setQuestion(String questionString) {
		question = questionString;
	}

	public static void setAnswer(String answerString) {
		answer = answerString;
	}

	public static void setValue(int valueInt) {
		value = valueInt;
	}

	public static void setBracket(String bracketString) {
		bracket = bracketString;
	}
}
