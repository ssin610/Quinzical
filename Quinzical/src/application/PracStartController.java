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
	
	
	
	public void initialize(URL url, ResourceBundle rb) {
		// create a alert 
        Alert a = new Alert(AlertType.NONE); 
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
	public void readSelectedfile() {
		//Get the choice from the choicebox
    	String selected_cat = (String) cat_choice.getSelectionModel().getSelectedItem();
		System.out.println(selected_cat);
		String currentpath=System.getProperty("user.dir");
//		System.out.println(currentpath); 
		File file = new File(currentpath+"/cat/"+selected_cat); 
		 int index=0;
	        if(file.exists()){  
	            try {  
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
	            } catch (FileNotFoundException e) {  
//	                System.out.println("no this file");  
	                e.printStackTrace();  
	            } catch (IOException e) {  
//	                System.out.println("io exception");  
	                e.printStackTrace();  
	            }  
	        }  
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
      System.out.println(cats);
      catArray=cats.toArray(new String[cats.size()]);
  } 
	
	 /**
	  * Read line in files based on the path
	  */
	 public static void readline(String filePath, String filename){  
//	        System.out.println("------Read file-------");  
	        File file = new File(filePath); 
	        int index=0;
	        if(file.exists()){  
	            try {  
	                FileReader fileReader = new FileReader(file);  
	                BufferedReader br = new BufferedReader(fileReader);  
	                String lineContent = null;  
	                while((lineContent = br.readLine())!=null){  
	                	index++;
	                    System.out.println(lineContent);  
	                    List<String> elephantList = Arrays.asList(lineContent.split(","));
	                    System.out.println(elephantList);
	                }  
	                br.close();  
	                fileReader.close();  
	            } catch (FileNotFoundException e) {  
//	                System.out.println("no this file");  
	                e.printStackTrace();  
	            } catch (IOException e) {  
//	                System.out.println("io exception");  
	                e.printStackTrace();  
	            }  
	        }  
	    }
	
    public void startPrac(ActionEvent event){
    	//Get the current stage
    	Stage thisstage = (Stage)start_prac_button.getScene().getWindow();
    	readSelectedfile();
    	
		//Open new winodw start the game
    	try {
    		//Pass parameter across
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("PracticeAnswer.fxml"));
			loader.load();
	    	PracticeAnswerController controller = loader.getController();
			
			controller.setAnswer("What");
			
			
			//Load GUI process
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("PracticeAnswer.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Stage secondStage = new Stage();
			secondStage.setScene(scene);
			secondStage.show();
			
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("PracticeAnswer.fxml"));
//			loader.load();
//	    	PracticeAnswerController controller = loader.getController();
//			controller.setQuestion("Shiit");
			
			thisstage.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    
}
