package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;


import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



public class PracStartController implements Initializable {
	private Object[] catArray;//Array stores all cats
	private Object[] ValueArray;
	private static String _allQuestions="";
	private List<String>cats=new ArrayList<String>();
	private List<String>_questionsselected=new ArrayList<String>();
	@FXML
	Button start_prac_button;
	@FXML
	ChoiceBox cat_choice;
	
	
//	String quest="This native bird lays the largest egg in relation to their body size of any species of bird in the world, (What is) the Kiwi";
	String showtext;
	String answer;
	String bracket;
	Alert a = new Alert(AlertType.NONE);
	
	/**
	 * Split the string into 3 parts
	 * @param question
	 */
	public void trimString(String question) {
		try {
			String temp[] = question.split("\\(");
			showtext=temp[0].substring(0, temp[0].length()-2);;
			String temp2[] = temp[1].split("\\)");
			bracket="( "+temp2[0].trim()+" )";
			answer=temp2[1].trim();
		
		}catch (Exception e) {
			
			 a.setAlertType(AlertType.ERROR); 
	            // show the dialog 
	            a.show(); 
	            a.setHeaderText("Question Reading Error");
	            a.setContentText("Please check the question format");
		}
	}
	
	
	public void initialize(URL url, ResourceBundle rb) {
		// create a alert 
     
        cats.clear();
        _questionsselected.clear();
		
		try {
			
			readfile();
			cat_choice.setItems(FXCollections.observableList(cats));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//Show alert
            a.setAlertType(AlertType.ERROR); 
            // show the dialog 
            a.show(); 
            a.setHeaderText("File Reading Error");
            a.setContentText("Please check the file arrangment");
		}
    }
	
	/**
	 * Return name
	 * @param filename
	 * @return
	 */
	public static String getFileNameNoEx(String filename) { 
        if ((filename != null) && (filename.length() > 0)) { 
            int dot = filename.lastIndexOf('.'); 
            if ((dot >-1) && (dot < (filename.length()))) { 
                return filename.substring(0, dot); 
            } 
        } 
        return filename; 
    }
	/**
	 * Read lines in the selected cat
	 * @throws IOException
	 */
	public void readSelectedfile() throws IOException {
		//Get the choice from the choicebox
    	String selected_cat = (String) cat_choice.getSelectionModel().getSelectedItem();
		System.out.println(selected_cat);
		String currentpath=System.getProperty("user.dir");
		
//		System.out.println(currentpath); 
		File file = new File(currentpath+"/cat/"+selected_cat); 
		 int index=0;
	        if(file.exists()){  
	        	 FileReader fileReader = new FileReader(file);  
	                BufferedReader br = new BufferedReader(fileReader);  
	                String lineContent = null;  
	                while((lineContent = br.readLine())!=null){  
	                	index++;
	                	System.out.println(lineContent);
	                	_questionsselected.add(lineContent);
	                }  
	                br.close();  
	                fileReader.close();  
	        } 
	        
	        Collections.shuffle(_questionsselected); //Shuffle the list
	        trimString(_questionsselected.get(0)); //Process the line after shuffle
	}
	
	/**
	 * Read files store to a list
	 * @throws IOException
	 */
	public void readfile() throws IOException{
		String currentpath=System.getProperty("user.dir");
//		System.out.println(currentpath); 
      File file = new File(currentpath+"/cat");   
      File[] array = file.listFiles();   

      for(int i=0;i<array.length;i++)
      {   
          if(array[i].isFile())
          {   
            String filename=getFileNameNoEx(array[i].getName());
       	   	cats.add(filename);
            
          } 
      } 
      
      catArray=cats.toArray(new String[cats.size()]);
  } 
	
	
    public void startPrac(ActionEvent event){
    	if (cat_choice.getSelectionModel().getSelectedItem()!=null) {
    		//Get the current stage
        	Stage thisstage = (Stage)start_prac_button.getScene().getWindow();
        	try {
    			readSelectedfile();
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    			a.setAlertType(AlertType.ERROR); 
                // show the dialog 
                a.show(); 
                a.setHeaderText("File Reading Error");
                a.setContentText("Please check the question format");
    		}
        	
        	
    		//Open new winodw start the game
        	try {
        		
        		//Pass parameter across
        		FXMLLoader loader = new FXMLLoader(getClass().getResource("PracticeAnswer.fxml"));
    			loader.load();
    	    	PracticeAnswerController controller = loader.getController();
    			//Pass value to the next page
    			controller.setStrings(showtext,answer,bracket);
    			System.out.println(bracket);
    			
    			
    			//Load GUI process
    			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("PracticeAnswer.fxml"));
    			Scene scene = new Scene(root);
    			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    			Stage secondStage = new Stage();
    			secondStage.setScene(scene);
    			secondStage.show();
    			
//    			FXMLLoader loader = new FXMLLoader(getClass().getResource("PracticeAnswer.fxml"));
//    			loader.load();
//    	    	PracticeAnswerController controller = loader.getController();
//    			controller.setQuestion("Shiit");
    			
    			thisstage.close();
    		} catch(Exception e) {
    			e.printStackTrace();
    			a.setAlertType(AlertType.ERROR); 
                // show the dialog 
                a.show(); 
                a.setHeaderText("File Reading Error");
                a.setContentText("Please check the file arrangment");
    		}
    			
    		}else {
    			
    		// If no cat is selected
   			 a.setAlertType(AlertType.WARNING); 
   	            // show the dialog 
   	            a.show(); 
   	            a.setHeaderText("Selection required");
   	            a.setContentText("You have to select a category");
    		}
    	
    	
    }
    
    
}
