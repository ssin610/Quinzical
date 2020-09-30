package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.SwingWorker;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class PracticeAnswerController implements Initializable{
	
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
	
	static String _question;	//What is expected from the user
	static String _answer=""; //What display to the user
	static String _hint;	
	static String _bracket;
	private int _chance=3;
	private String _speed="0";
	Alert a = new Alert(AlertType.NONE);
	
	

	
	/**
	 * Use spd-say to speak & in the worker thread
	 * @param sentence
	 */
	public void speak(String sentence) {
		 Thread thread = new Thread() {
             @Override
             public void run() {
//            	 String cmd = "echo \""+sentence+"\" | festival --tts"; //spd-say -i100 "hello"
         		String cmd = "spd-say -r"+_speed+" \""+sentence+"\"";
//         		String cmd = "espeak -a"+_volume+" \""+sentence+"\"";
//         		String cmd = "echo $pwd";
         		
         		ProcessBuilder ttsBuilder = new ProcessBuilder("bash","-c",cmd);
//         		String[] cmds = {"/bin/sh", "-c", cmd};
//         		System.out.println(cmd);
//         		Process process;
         		try {
         			Process ttsProcess = ttsBuilder.start();
//         			process = Runtime.getRuntime().exec(cmds);
//         			System.out.println(cmd);
//         			process.waitFor();
         		} catch (IOException e) {
         			// TODO Auto-generated catch block
         			e.printStackTrace();
         			a.setAlertType(AlertType.ERROR); 
                     // show the dialog 
                     a.show(); 
//                     a.setHeaderText("spd-say not installed");
//                     a.setContentText("Please check the READ.md and install spd-say");
                     a.setHeaderText("Audio System Crash");
                     a.setContentText("Please make sure spd-say is installed (in READ.md) and restart the game");
         		}
             }
              
         };
         thread.setName("thread1");
         thread.start();
//		
	}
	
	
	public void replay(ActionEvent event){
    	speak(_answer);
    }
	public void backMenu() {

		try {
			Stage thisstage = (Stage)submit_button.getScene().getWindow();
			//Load GUI process
			AnchorPane root;
			root = (AnchorPane)FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage secondStage = new Stage();
			secondStage.setScene(scene);
			secondStage.show();
			thisstage.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			a.setAlertType(AlertType.ERROR); 
            // show the dialog 
            a.show(); 
            a.setHeaderText("Fatal Error");
            a.setContentText("Please restart the game");
		}
		
	}
   
    public void submit(ActionEvent event){
    	String input = user_input.getText();
    	if (input.trim().equalsIgnoreCase(_question.trim()) || input.trim().equalsIgnoreCase(_bracket +_question.trim()) || input.trim().equalsIgnoreCase(_bracket +" "+_question.trim())) {
			hint_label.setVisible(true);
			hint_label.setText("Correct! The question is " + _bracket+ " " + _question);
			
			submit_button.setDisable(true);
    		audio_replay_button.setDisable(true);
    		back_button.setDisable(false);
    		back_button.setVisible(true);

    	}else {
    		//IF input is not correct
    		_chance=_chance-1;
//    		System.out.println(_chance);
    		chance_left.setText(Integer.toString(_chance));
    		hint_label.setVisible(true);
    		hint_label.setText("Sorry, incorrect!");
    		
    		//Compare the chance to see whether finished or show hint
    		if (_chance==0) { 											//Game over
        		hint_label.setVisible(true);
        		hint_label.setText("Sorry, The question is " + _bracket +" "+ _question);
        		
        		submit_button.setDisable(true);
        		audio_replay_button.setDisable(true);
        		back_button.setDisable(false);
        		back_button.setVisible(true);
        	}else if (_chance == 1) {									//Show hint
        		hint_label.setVisible(true);
        		hint_label.setText(_hint);
        	}
    	}
   
    }
    /**
     * Get rid off the useless leading char when giving hint
     * @param hint
     * @return
     */
    public String gethint(String hint) {
//    	System.out.println("Before:"+ hint);
    	String leading = hint.trim().split(" ")[0]; 
    	if (leading.equalsIgnoreCase("the") || leading.equalsIgnoreCase("a") || leading.equalsIgnoreCase("an")) {
    		String temp = leading+" ";
    		hint=hint.replace(temp,"");
    		hint=hint.trim();
    	}
//    	System.out.println("After:"+ hint);
    	return hint.substring(0, 1);
    }
    
    public   void setQuestion(String question) {
    	_question=question;
	}
    public   void setStrings(String answer, String question, String bracket) {
    	_answer=answer;
    	_question=question.split(",")[0];
    	_bracket=bracket;
//    	System.out.println(_bracket);
    	System.out.println(_question);
    	_hint="Hint: "+gethint(_question);
    	speak(_answer);
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		back_button.setDisable(true);
		// TODO Auto-generated method stub
		question_label.setText(_answer);
		chance_left.setText(Integer.toString(_chance));
		user_input.setPromptText(_bracket);
		//Add lisenter to the slider
		volume_slider.setOnMouseReleased(event -> {
            int temp = (int)volume_slider.getValue();
            _speed=Integer.toString(temp);
//            System.out.println(_speed);
        });
	}
    
    
}
