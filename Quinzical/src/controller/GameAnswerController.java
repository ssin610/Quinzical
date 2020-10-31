package controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
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
import main.Main;

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
	private int total_time;

	private String _speed = "0";
	Alert a = new Alert(AlertType.NONE);

	private boolean clicked=false;
	
	/**
	 * Initialize this controller by setting the relevant FXML elements
	 * and their values
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		back_button.setDisable(true);
		question_label.setText(question);
		winnings.setText("Winnings: $" + Integer.toString(Main.getWinnings()));
//		user_input.setPromptText(bracket);
		Main.addAnsweredQuestion(question + " - " + category);
		// Add lisenter to the slider
		volume_slider.setOnMouseReleased(event -> {
			int temp = (int) volume_slider.getValue();
			_speed = Integer.toString(temp);
		});
		int tempendIndex = (bracket.trim().length())-1;
		bracketLabel.setText(bracket.trim().substring(1, tempendIndex)+":");
		setGif();
		countdown();
		speak(question);
	}
	
	/**
	 * Play a celebratory sound when the user gets a question correct
	 */
	public void playSound() {
		String cmd = "./aha.sh>/dev/null 2>&1";
		ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
		try {
			builder.start();
		} catch (IOException e) {
			a.setAlertType(AlertType.ERROR);
			// show the dialog
			a.show();
			a.setHeaderText("Audio System Crash");
			a.setContentText("Please make sure spd-say is installed (in READ.md) and restart the game");
		}
	}
//	
	/**
	 * Show the celebratory gif when the user gets a question correct
	 */
	public void setGif() {
		//Set the gif 
		Image i = new Image("resources/aha.gif");
        gif.setImage(i);
        gif2.setImage(i);
        
	}
	
	/**
	 * Show the celebratory gif and play the sound effect when the user gets
	 * a question correct
	 */
	public void reward() {
		//Set the sound Effect
		playSound();
		//Start the gifs
		gif.setVisible(true);
		gif2.setVisible(true);
	
	}

	/**
	 * Use spd-say to speak the sentence using a worker thread
	 * @param sentence the sentence to speak
	 */
	public void speak(String sentence) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				String cmd = "spd-say --wait -r" + _speed + " \"" + sentence + "\"";
				ProcessBuilder ttsBuilder = new ProcessBuilder("bash", "-c", cmd);
				try {
					ttsBuilder.start();
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
	
	/**
	 * Called when the user presses the replay button.
	 * This method speaks the clue again
	 */
	public void replay(ActionEvent event) {
		speak(question);
	}
	
	/**
	 * Called when the user presses the show text button.
	 * A label containing the question then appears on the screen
	 */
	public void showText(ActionEvent event) {
		question_label.setVisible(true);
	}
	
	/**
	 * Called when the user presses the main menu button.
	 * This method changes the scene to the main menu 
	 */
	public void onMainMenuPushed(ActionEvent event) {
		speak(" "); // To prevent the audio keep playing after going back to the menu
		try {
			Parent viewParent = FXMLLoader.load(getClass().getResource("../view/ClueGrid.fxml"));
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
	
	/**
	 * Perform input normalization on the users answer so that all non alphabetic 
	 * characters are removed and leading a/the/an are also removed
	 * @param text the user answer to normalize
	 * @return the normalized text
	 */
	public String refineString(String text) {
		// Remove any symbols but not /
		if (text.contains("/")) {
			;
		}else {
			text = text.replaceAll("\\p{P}", "");
		}

		//  Remove leading a/the/an
		String leading = text.split(" ")[0].trim();
		//Make sure removing the leading wont cause empty String
		if (text.split(" ").length>1 && (leading.equalsIgnoreCase("the") || leading.equalsIgnoreCase("a") || leading.equalsIgnoreCase("an"))) {
			text=text.replaceFirst(leading+" ", "").trim();
		}
		return text;
	}
	
	/**
	 * Called when the user presses the submit button.
	 * This method normalizes and then evaluates the user answer and performs actions based on
	 * whether the answer is correct or not
	 */
	@FXML
	public void onSubmitButtonPushed() {
		clicked=true;
		//Normalize 2 Strings to get rid of macrons
		String input = normal(user_input.getText().trim()).toLowerCase();
		String normalizedanswer = normal(answer.trim()).toLowerCase();
		normalizedanswer = refineString(normalizedanswer); //Refactor the answer
		
		// Only allow when equal or input contains answer
		if (input.equalsIgnoreCase(normalizedanswer) || input.contains(normalizedanswer)) {
			reward();
			hint_label.setVisible(true);
			hint_label.setText("Correct! $" + value + " has been added to your winnings!");
			speak("Correct!");
			Main.setWinnings(value);
			submit_button.setVisible(false);
			audio_replay_button.setDisable(true);
			back_button.setDisable(false);
			back_button.setVisible(true);
			dontknow_button.setVisible(false);
			textshow_button.setDisable(true);
			
		// If answer has multiple answer which is not expected
		}else if (answer.contains("/") && normalizedanswer.contains(input) && input!="" ) { 
			reward();
			hint_label.setVisible(true);
			hint_label.setText("Correct! $" + value + " has been added to your winnings!");
			speak("Correct!");
			Main.setWinnings(value);
			submit_button.setVisible(false);
			audio_replay_button.setDisable(true);
			back_button.setDisable(false);
			back_button.setVisible(true);
			dontknow_button.setVisible(false);
			textshow_button.setDisable(true);
		}else {
			hint_label.setVisible(true);
			hint_label.setText("Incorrect. The correct answer was: " + bracket + " " + answer);
			speak("Incorrect. The correct answer was: " + bracket + " " + answer);
			Main.setWinnings(-value);
			submit_button.setVisible(false);
			audio_replay_button.setDisable(true);
			back_button.setDisable(false);
			back_button.setVisible(true);
			dontknow_button.setVisible(false);
			textshow_button.setDisable(true);
		
		}
		winnings.setText("Winnings: $" + Integer.toString(Main.getWinnings()));
		worker.cancel(true); //Stop count down
		
	}
	
	/**
	 * Called when the user presses the don't know button.
	 * This counts as getting the question wrong, so the 
	 * appropriate actions are taken
	 */
	public void onDontKnowPushed(ActionEvent event) {
		clicked=true;
		hint_label.setVisible(true);
		hint_label.setText("The correct answer was: " + bracket + " " + answer);
		speak("The correct answer was: " + bracket + " " + answer);
		Main.setWinnings(-value);
		submit_button.setVisible(false);
		dontknow_button.setVisible(false);
		audio_replay_button.setDisable(true);
		back_button.setDisable(false);
		back_button.setVisible(true);
		winnings.setText("Winnings: $" + Integer.toString(Main.getWinnings()));
		textshow_button.setDisable(true);
		worker.cancel(true); //Stop count down
	}
	
	/**
	 * This method gets called when the user presses enter to submit their
	 * question rather than pressing the submit button
	 */
	public void onEnterKeyPressed(ActionEvent event) {
		if (clicked == false) {
			onSubmitButtonPushed();
		}
	
	}
	
	/**
	 * Begin the timer count down for the question
	 */
	public void countdown() {
		total_time=40;
		worker.addPropertyChangeListener(listener); // Add a poperty change listener to the worker
		worker.execute();
	}
	
	/**
	 * Replace macrons in the users answer with normal letters. This normalizes
	 * the user input so it can be evaluated correctly taking into account macrons
	 * @param text the user answer to normalize
	 * @return the normalized text
	 */
	public String normal(String text) {
		String[] macrons = new String[] {"ā","ē","ī","ō","ū","Ā","Ē","Ī","Ō","Ū"};
		String[] normalLetter = new String[] {"a","e","i","o","u","a","e","i","o","u"};
		for(String i :  macrons){
			if (text.contains(i)) {
				text=text.replace(i, normalLetter[Arrays.asList(macrons).indexOf(i)]);
			}
		}
		return text.toLowerCase();
		

	};
	
	/**
	 * Create a swing worker to do the count down for each question
	 */
	SwingWorker<Integer,Integer> worker = new SwingWorker<Integer,Integer>(){
		@Override
		protected Integer doInBackground() throws Exception {
			total_time=40;
			while (total_time!=0) {
				publish(total_time); 
				Thread.sleep(1000);
				total_time--;
			}
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
			
			if("progress"==evt.getPropertyName()){			// During the progress update the GUI
				
                int progress = (Integer)evt.getNewValue();
                //When 10 seconds left, Highlight the Text and the bar
                if (total_time==10) {
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
				    	if(clicked) {
				    		bar.setVisible(false);		
					    	time.setText("Done !!!"); 
					    	time.setStyle("-fx-text-fill: #ffa31a;");
				    	}else {
				    		onSubmitButtonPushed();
					    	bar.setVisible(false);		
					    	time.setText("Done !!!"); 
					    	time.setStyle("-fx-text-fill: #ffa31a;");
				    	}
				    }
				});
            }
		}
	};
	
	/**
	 * Set the question
	 */
	public static void setQuestion(String questionString) {
		question = questionString;
	}
	
	/**
	 * Set the answer to the question
	 */
	public static void setAnswer(String answerString) {
		answer = answerString;
	}

	/**
	 * Set the dollar value of the clue
	 */
	public static void setValue(int valueInt) {
		value = valueInt;
	}
	
	/**
	 * Set the 'What/Who is' part of the clue
	 */
	public static void setBracket(String bracketString) {
		bracket = bracketString;
	}
	
	/**
	 * Set the category the clue belongs to
	 */
	public static void setCategory(String categoryString) {
		category = categoryString;
	}
}