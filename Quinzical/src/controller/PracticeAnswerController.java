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

public class PracticeAnswerController implements Initializable {

	@FXML
	Button submit_button;

	@FXML
	Button back_button;

	@FXML
	Button audio_replay_button;

	@FXML
	Label question_label;

	@FXML
	Label chance_left;

	@FXML
	Label hint_label;

	@FXML
	ProgressBar reading_bar;

	@FXML
	Slider volume_slider;

	@FXML
	TextField user_input;

	private static String _question; // What is expected from the user
	private static String _answer = ""; // What is displayed to the user
	private static String _hint;
	private static String _bracket;
	private int _chance = 3;
	private String _speed = "0";
	Alert a = new Alert(AlertType.NONE);

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		back_button.setDisable(true);
		question_label.setText(_answer);
		chance_left.setText(Integer.toString(_chance));
		user_input.setPromptText(_bracket);
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

	public void onReplayPushed(ActionEvent event) {
		speak(_answer);
	}

	public void onMainMenuPushed(ActionEvent event) {
		try {
			Parent viewParent = FXMLLoader.load(getClass().getResource("view/MainMenu.fxml"));
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
		if (input.trim().equalsIgnoreCase(_question.trim())
				|| input.trim().equalsIgnoreCase(_bracket + _question.trim())
				|| input.trim().equalsIgnoreCase(_bracket + " " + _question.trim())) {
			hint_label.setVisible(true);
			hint_label.setText("Correct! The answer was: " + _bracket + " " + _question);

			submit_button.setDisable(true);
			audio_replay_button.setDisable(true);
			back_button.setDisable(false);
			back_button.setVisible(true);
		} else {
			// IF input is not correct
			_chance = _chance - 1;
			chance_left.setText(Integer.toString(_chance));
			hint_label.setVisible(true);
			hint_label.setText("Sorry, incorrect!");

			// Compare the chance to see whether finished or show hint
			if (_chance == 0) { // Game over
				hint_label.setVisible(true);
				hint_label.setText("Incorrect. The correct answer was: " + _bracket + " " + _question);
				submit_button.setDisable(true);
				audio_replay_button.setDisable(true);
				back_button.setDisable(false);
				back_button.setVisible(true);

			} else if (_chance == 1) { // Show hint
				hint_label.setVisible(true);
				hint_label.setText(_hint);
			}
		}
	}

	/**
	 * Set the fields which contain information about the question
	 */
	public void setStrings(String answer, String question, String bracket) {
		_answer = answer;
		_question = question.split(",")[0];
		_bracket = bracket;
		_hint = "Hint: " + gethint(_question);
		speak(_answer);
	}

	/**
	 * Perform splitting and trimming to get the hint to display to the user
	 * @param hint the string to perform formatting on
	 * @return the hint
	 */
	public String gethint(String hint) {
		String leading = hint.trim().split(" ")[0];
		if (leading.equalsIgnoreCase("the") || leading.equalsIgnoreCase("a") || leading.equalsIgnoreCase("an")) {
			String temp = leading + " ";
			hint = hint.replace(temp, "");
			hint = hint.trim();
		}
		return hint.substring(0, 1);
	}
}
