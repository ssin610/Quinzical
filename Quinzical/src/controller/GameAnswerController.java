package controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.SwingWorker;
import javafx.application.Platform;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import helper.GameData;
import helper.InputNormalization;
import helper.SoundUtil;

/**
 * Controller for the GameAnswer scene
 */
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

	@FXML  
	Label time;

	@FXML  
	Label bracketLabel;

	@FXML  
	ProgressBar bar;

	@FXML
	ImageView gif;

	@FXML
	ImageView gif2;

	// store information about the particular question
	private static String question;
	private static String answer;
	private static String bracket;
	private static String category; 
	private static int value;

	private String speed = "0";
	private int totalTime;
	private boolean submitted = false;

	private Alert a = new Alert(AlertType.NONE);

	/**
	 * Initialize this controller by setting the relevant FXML elements
	 * and their values
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		back_button.setDisable(true);
		question_label.setText(question);
		winnings.setText("Winnings: $" + Integer.toString(GameData.getWinnings()));
		GameData.addAnsweredQuestion(question + " - " + category);

		// Add listener to the slider
		volume_slider.setOnMouseReleased(event -> {
			int temp = (int) volume_slider.getValue();
			speed = Integer.toString(temp);
		});
		int tempendIndex = (bracket.trim().length())-1;
		bracketLabel.setText(bracket.trim().substring(1, tempendIndex)+":");
		countdown();
		SoundUtil.speak(question, speed, a);
	}

	/**
	 * Show the celebratory gif and play the sound effect when the user gets
	 * a question correct
	 */
	public void showReward() {

		//Set the gif 
		Image i = new Image("resources/aha.gif");
		gif.setImage(i);
		gif2.setImage(i);
		//Set the sound Effect
		SoundUtil.playSound(a);
		//Start the gifs
		gif.setVisible(true);
		gif2.setVisible(true);
	}


	/**
	 * Called when the user presses the replay button.
	 * This method speaks the clue again
	 */
	public void onReplayPushed(ActionEvent event) {
		SoundUtil.speak(question, speed, a);
	}

	/**
	 * Called when the user presses the main menu button.
	 * This method changes the scene to the main menu 
	 */
	public void onMainMenuPushed(ActionEvent event) {

		SoundUtil.speak(" ", speed, a); // prevent the audio from playing after going back to main menu
		try {
			Parent viewParent = FXMLLoader.load(getClass().getResource("/view/ClueGrid.fxml"));
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
	@FXML
	public void onSubmitButtonPushed() {

		submitted = true;
		//Normalize the strings to get rid of macrons
		String input = InputNormalization.normal(user_input.getText().trim()).toLowerCase();
		String normalizedanswer = InputNormalization.normal(answer.trim()).toLowerCase();
		normalizedanswer = InputNormalization.refineString(normalizedanswer); // Further refinement of strings

		// If answer has multiple answers
		if (input.equalsIgnoreCase(normalizedanswer) || input.contains(normalizedanswer) || 
				(answer.contains("/") && normalizedanswer.contains(input) && input!="")) {
			showReward();
			hint_label.setVisible(true);
			hint_label.setText("Correct! $" + value + " have been added to your winnings!");
			SoundUtil.speak("Correct! " + value + " dollars have been added to your winnings", speed, a);
			GameData.setWinnings(value);
			submit_button.setVisible(false);
			audio_replay_button.setDisable(true);
			back_button.setDisable(false);
			back_button.setVisible(true);
			dontknow_button.setVisible(false);
			textshow_button.setDisable(true);
			winnings.setText("Winnings: $" + Integer.toString(GameData.getWinnings()));
			worker.cancel(true); //Stop count down

		}else {
			onDontKnowPushed();
		}
	}

	/**
	 * Called when the user presses the don't know button or gets the question incorrect.
	 * This counts as getting the question wrong, so the 
	 * appropriate actions are taken
	 */
	public void onDontKnowPushed() {
		submitted = true;
		hint_label.setVisible(true);
		hint_label.setText("Sorry, the correct answer was: " + bracket + " " + answer);
		SoundUtil.speak("Sorry, the correct answer was: " + bracket + " " + answer, speed, a);
		GameData.setWinnings(-value);
		submit_button.setVisible(false);
		dontknow_button.setVisible(false);
		audio_replay_button.setDisable(true);
		back_button.setDisable(false);
		back_button.setVisible(true);
		textshow_button.setDisable(true);
		winnings.setText("Winnings: $" + Integer.toString(GameData.getWinnings()));
		worker.cancel(true); //Stop count down
	}

	/**
	 * This method gets called when the user presses enter to submit their
	 * question rather than pressing the submit button
	 */
	public void onEnterKeyPressed(ActionEvent event) {
		if (submitted == false) {
			onSubmitButtonPushed();
		}
	}

	/**
	 * Called when the user presses the show text button.
	 * A label containing the question then appears on the screen
	 */
	public void showText(ActionEvent event) {
		question_label.setVisible(true);
	}

	/**
	 * Create a swing worker to do the count down for each question
	 */
	SwingWorker<Integer,Integer> worker = new SwingWorker<Integer,Integer>(){
		@Override
		protected Integer doInBackground() throws Exception {
			totalTime=40;
			while (totalTime!=0) {
				publish(totalTime); 
				Thread.sleep(1000);
				totalTime--;
			}
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		protected void process(List<Integer> chunks) { //Used to send value to the progress bar
			super.process(chunks);
			for (int i :chunks)
			{
				this.setProgress(i);
			}
		}
	};

	/**
	 * Add property change listener to the worker so that the GUI can be updated
	 * according to the timer
	 */
	PropertyChangeListener listener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			// TODO Auto-generated method stub
			if("progress"==evt.getPropertyName()){			// During the progress update the GUI

				int progress = (Integer)evt.getNewValue();
				//When 10 seconds left, Highlight the Text and the bar
				if (totalTime==10) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							bar.setStyle("-fx-accent: #CF1708");
							time.setStyle("-fx-text-fill: #ffa31a;");
						}
					});
				}

				// Run Swing on JavaFx Thread to Update GUI
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						bar.setProgress(progress*0.025);//Change the progress bar
						time.setText("Time Left: "+progress); 
					}
				});
			}else if ("DONE"==evt.getNewValue().toString()) { //Disable buttons when the progress is finished
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						if(submitted) {
							bar.setVisible(false);		
							time.setText("Times Up!!!"); 
							time.setStyle("-fx-text-fill: #ffa31a;");
						}else {
							onSubmitButtonPushed();
							bar.setVisible(false);		
							time.setText("Times Up!!!"); 
							time.setStyle("-fx-text-fill: #ffa31a;");
						}
					}
				});
			}
		}
	};

	/**
	 * Begin the timer count down for the question
	 */
	public void countdown() {
		totalTime=40;
		worker.addPropertyChangeListener(listener); // Add a poperty change listener to the worker
		worker.execute();
	}

	/**
	 * Set the relevant fields which contain information about the question
	 */
	public static void setFields(String questionString, String answerString, String bracketString, String categoryString, int valueInt) {
		question = questionString;
		answer = answerString;
		bracket = bracketString;
		category = categoryString;
		value = valueInt;
	}
}