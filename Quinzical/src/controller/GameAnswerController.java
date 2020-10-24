package controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
	private static int value;
	private int total_time;
	private Thread _audioThread;
	private static MediaPlayer mp;
	private String _speed = "0";
	Alert a = new Alert(AlertType.NONE);

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		back_button.setDisable(true);
		question_label.setText(question);
		winnings.setText("Winnings: $" + Integer.toString(Main.getWinnings()));
//		user_input.setPromptText(bracket);
		Main.addAnsweredQuestion(question);
		// Add lisenter to the slider
		volume_slider.setOnMouseReleased(event -> {
			int temp = (int) volume_slider.getValue();
			_speed = Integer.toString(temp);
		});
		int tempendIndex = (bracket.trim().length())-1;
		bracketLabel.setText(bracket.trim().substring(1, tempendIndex)+":");
		setGif();
		setMusic();
		countdown();
		speak(question);
	}
	
	/**
	 * Set the sound effect and ready to be played
	 */
	public void setMusic() {
		String path = new File("src/resources/cash.wav").toURI().toString();
        Media media = new Media(path);
        mp = new MediaPlayer(media);
        mp.setVolume(0.5);
     
	}
	
	/**
	 * Set the gif and ready to be show
	 */
	public void setGif() {
		//Set the gif 
		Image i = new Image("resources/aha.gif");
        gif.setImage(i);
        gif2.setImage(i);
        
	}
	
	/**
	 * Including the gif and cash in sound effect as an reward if answered correctly
	 */
	public void ahaMoment() {
		//Set the sound Effect
		mp.play();
		//Start the gifs
		gif.setVisible(true);
		gif2.setVisible(true);
	
	}

	/**
	 * Use spd-say to speak & in the worker thread
	 * @param sentence the sentence to speak
	 */
	public void speak(String sentence) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				String cmd = "spd-say --wait -r" + _speed + " \"" + sentence + "\"";
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
		_audioThread=thread;
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
			window.setResizable(false);
			window.show();
			_audioThread.stop(); //Stop audio playing
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
	 * Refactor the String, so that all non alphabetic char are removed
	 * Leading a/the/an is also removed
	 * @param text
	 * @return
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
	
	@FXML
	public void onSubmitButtonPushed() {
		//Normalize 2 Strings to get rid of macrons
		String input = normal(user_input.getText().trim()).toLowerCase();
		String normalizedanswer = normal(answer.trim()).toLowerCase();
		normalizedanswer = refineString(normalizedanswer); //Refactor the answer
		
		// Only allow when equal or input contains answer
		if (input.equalsIgnoreCase(normalizedanswer) || input.contains(normalizedanswer)) {
			ahaMoment();
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
			ahaMoment();
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

	public void onDontKnowPushed(ActionEvent event) {
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
	 * Begin the count down
	 */
	public void countdown() {
		total_time=40;
		worker.addPropertyChangeListener(listener); // Add a poperty change listener to the worker
		worker.execute();
	}
	
	/**
	 * Replace macrons in Strings with normal letter lowercased
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
	 * Create a swing worker to do the count down
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
	 * Add property change listener to the worker so that we can update the GUI
	 */
	PropertyChangeListener listener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			
			// TODO Auto-generated method stub
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
				    	onSubmitButtonPushed();
				    	bar.setVisible(false);		
				    	time.setText("Done !!!"); 
				    	time.setStyle("-fx-text-fill: #ffa31a;");
				    }
				});
            }
		}
	};
	
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
