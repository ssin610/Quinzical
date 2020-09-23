package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class PracticeAnswerController implements Initializable{
	
	@FXML
	Button start_prac_button;
	
	@FXML
	Label question_label;
	
	@FXML
	Label chance_left;
	
	static String _question;
	static String _answer;
	private int _chance=3;

	
	
   
    public void submit(ActionEvent event){
    	_chance--;
    	chance_left.setText(Integer.toString(_chance));
    	if (_chance<=0) {
    		start_prac_button.setDisable(true);
    	}
    }
    
    public   void setQuestion(String question) {
    	_question=question;
	}
    public   void setAnswer(String answer) {
    	_answer=answer;
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		System.out.println("0");
		question_label.setText(_question);
		System.out.println(_answer);
		chance_left.setText(Integer.toString(_chance));
		
	}
    
    
}
