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

import helper.InputNormalization;
import helper.SoundUtil;

/**
 * Controller for the PractiseAnswer scene
 */
public class PractiseAnswerController implements Initializable {

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
	Label bracketLabel;

	@FXML
	ProgressBar reading_bar;

	@FXML
	Slider volume_slider;

	@FXML
	TextField user_input;

	private static String question; // What is expected from the user
	private static String answer = ""; // What is displayed to the user
	private static String hint;
	private static String bracket="         ";

	private String speed = "0";
	private int chances = 3;
	private boolean submitted = false; // track whether the user has pressed the submit button

	private Alert a = new Alert(AlertType.NONE);

	/**
	 * Initialize this controller by setting the relevant FXML elements
	 * and their values
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		back_button.setDisable(true);
		question_label.setText(answer);
		chance_left.setText(Integer.toString(chances));

		// Add listener to the slider
		volume_slider.setOnMouseReleased(event -> {
			int temp = (int) volume_slider.getValue();
			speed = Integer.toString(temp);
		});

		int tempendIndex = (bracket.trim().length())-1;
		if (tempendIndex>=0) {
			bracketLabel.setText(bracket.trim().substring(1, tempendIndex)+":");
		}
	}

	/**
	 * Called when the user presses the replay button.
	 * This method speaks the clue again
	 */
	public void onReplayPushed() {
		SoundUtil.speak(answer, speed, a);
	}

	/**
	 * Called when the user presses the main menu button.
	 * This method changes the scene to the main menu 
	 */
	public void onMainMenuPushed(ActionEvent event) {

		SoundUtil.speak(" ", speed, a); // Prevents the audio from playing after going back to menu

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
	 * Called when the user presses the submit button.
	 * This method normalizes and then evaluates the user answer and performs actions based on
	 * whether the answer is correct or not
	 */
	public void onSubmitButtonPushed() {

		
		//Normalize strings to get rid of macrons
		String input = InputNormalization.normal(user_input.getText().trim()).toLowerCase();
		String normalizedanswer = InputNormalization.normal(question.trim()).toLowerCase();
		normalizedanswer = InputNormalization.refineString(normalizedanswer); // refine the string further

		// If answer has multiple answers
		if (input.equalsIgnoreCase(normalizedanswer) || input.contains(normalizedanswer) || 
				(question.contains("/") && normalizedanswer.contains(input))) {
			hint_label.setVisible(true);
			hint_label.setText("Correct! The answer was: " + bracket + " " + question);
			SoundUtil.speak("Correct! The answer was: " + bracket + " " + question, speed, a);
			submit_button.setVisible(false);
			audio_replay_button.setDisable(true);
			back_button.setDisable(false);
			back_button.setVisible(true);
			submitted = true;
		} else {

			// IF input is not correct
			chances = chances - 1;
			chance_left.setText(Integer.toString(chances));
			hint_label.setVisible(true);
			hint_label.setText("Sorry, incorrect!");

			// Compare the chance to see whether finished or show hint
			if (chances == 0) { // Game over
				submitted = true;
				hint_label.setVisible(true);
				hint_label.setText("Incorrect. The correct answer was: " + bracket + " " + question);
				SoundUtil.speak("Incorrect. The correct answer was: " + bracket + " " + question, speed, a);
				submit_button.setVisible(false);
				audio_replay_button.setDisable(true);
				back_button.setDisable(false);
				back_button.setVisible(true);
				user_input.setText(question.trim());

			} else if (chances == 1) { // Show hint
				hint_label.setVisible(true);
				hint_label.setText(hint);
			}
		}
	}

	/**
	 * This method gets called when the user presses enter to submit their
	 * question rather than pressing the submit button
	 */
	public void onEnterKeyPressed() {
		if (submitted == false) {
			onSubmitButtonPushed();
		}
	}

	/**
	 * Set the relevant fields which contain information about the question
	 */
	public void setStrings(String answerString, String clue, String bracketString) {
		answer = answerString;
		question = clue.split(",")[0];
		bracket = bracketString;
		hint = "Hint: " + getHint(question);
		SoundUtil.speak(answer, speed, a);
	}

	/**
	 * Perform splitting and trimming on the question string to get the hint 
	 * to display to the user
	 * @param hint the string to perform formatting on
	 * @return the hint
	 */
	public String getHint(String hint) {

		String leading = hint.trim().split(" ")[0];
		if (leading.equalsIgnoreCase("the") || leading.equalsIgnoreCase("a") || leading.equalsIgnoreCase("an")) {
			String temp = leading + " ";
			hint = hint.replace(temp, "");
			hint = hint.trim();
		}
		return hint.substring(0, 1);
	}
}