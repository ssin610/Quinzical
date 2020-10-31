package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class PractiseStartController implements Initializable {

	// arrays to store categories and questions
	private List<String> cats = new ArrayList<String>();
	private List<String> questionsSelected = new ArrayList<String>();

	@FXML
	Button start_prac_button;

	@FXML
	ChoiceBox<String> cat_choice;

	// store the different parts of a question
	String showtext;
	String answer;
	String bracket;

	Alert a = new Alert(AlertType.NONE);
	
	/**
	 * Initialize this controller by setting the relevant FXML elements
	 * and their values
	 */
	public void initialize(URL url, ResourceBundle rb) {

		cats.clear();
		questionsSelected.clear();

		try {
			readfile();
			cat_choice.setItems(FXCollections.observableList(cats));
			cat_choice.setStyle("-fx-font: 18px \"Serif\";");
		} catch (IOException e) {
			e.printStackTrace();
			// Show alert
			a.setAlertType(AlertType.ERROR);
			// show the dialog
			a.show();
			a.setHeaderText("File Reading Error");
			a.setContentText("Please check the file arrangment");
		}
	}

	/**
	 * Split the question string into 3 parts to split the
	 * clue, 'What/Who is' part, and answer
	 * @param question the question that needs to be split
	 */
	public void trimString(String question) {
		try {
			String temp[] = question.split("\\(");
			showtext = temp[0].substring(0, temp[0].length() - 2);
			String temp2[] = temp[1].split("\\)");
			bracket = "(" + temp2[0].trim() + ")";
			answer = temp2[1].trim();

		} catch (Exception e) {

			a.setAlertType(AlertType.ERROR);
			// show the dialog
			a.show();
			a.setHeaderText("Question Reading Error");
			a.setContentText("Please check the question format");
		}
	}

	/**
	 * Return the name of the file given as input
	 * @param file the file to read
	 * @return the file's name
	 */
	public static String getFileNameNoEx(String file) {
		if ((file != null) && (file.length() > 0)) {
			int dot = file.lastIndexOf('.');
			if ((dot > -1) && (dot < (file.length()))) {
				return file.substring(0, dot);
			}
		}
		return file;
	}

	/**
	 * Read lines in the selected category and add this to the
	 * questions array
	 * @throws IOException
	 */
	public void readSelectedfile() throws IOException {

		String selected_cat = cat_choice.getSelectionModel().getSelectedItem();
		String currentpath = System.getProperty("user.dir");

		File file = new File(currentpath + "/cat/" + selected_cat);
		if (file.exists()) {
			FileReader fileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);
			String lineContent = null;
			while ((lineContent = br.readLine()) != null) {
				questionsSelected.add(lineContent);
			}
			br.close();
			fileReader.close();
			
			
		}

		try {
			Collections.shuffle(questionsSelected); // Shuffle the list
			trimString(questionsSelected.get(0)); // Process the line after shuffle
		}catch(Exception e) {
			a.setAlertType(AlertType.ERROR);
			// show the dialog
			a.show();
			a.setHeaderText("File Reading Error");
			a.setContentText("Please check the question file");
		}
	}

	/**
	 * This method reads files from the cat folder and then stores
	 * them in the categories list
	 * @throws IOException
	 */
	public void readfile() throws IOException {
		String currentpath = System.getProperty("user.dir");

		File file = new File(currentpath + "/cat");
		File[] array = file.listFiles();

		for (int i = 0; i < array.length; i++) {
			if (array[i].isFile()) {
				String filename = getFileNameNoEx(array[i].getName());
				cats.add(filename);
			}
		}

	}
	
	/**
	 * Called when the user presses the start practise button.
	 * This method then switches the scene to the question answering
	 * scene and passes the relevant question parameters to the controller
	 */
	public void onStartPushed(ActionEvent event) {
		if (cat_choice.getSelectionModel().getSelectedItem() != null) {
			// Get the current stage
			Stage thisStage = (Stage) start_prac_button.getScene().getWindow();
			try {
				readSelectedfile();
			} catch (IOException e1) {
				a.setAlertType(AlertType.ERROR);
				// show the dialog
				a.show();
				a.setHeaderText("File Reading Error");
				a.setContentText("Please check the question format");
			}

			// Open new winodw start the game
			try {
				// Pass parameter across
				FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/PractiseAnswer.fxml"));
				loader.load();
				PractiseAnswerController controller = loader.getController();
				// Pass value to the next page
				controller.setStrings(showtext, answer, bracket);
				// Load GUI process
				AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/PractiseAnswer.fxml"));
				Scene scene = new Scene(root);
				Stage secondStage = new Stage();
				secondStage.setScene(scene);
				secondStage.setResizable(false);
				secondStage.show();
				thisStage.close();
//				
			} catch (Exception e) {
				e.printStackTrace();
				a.setAlertType(AlertType.ERROR);
				// show the dialog
				a.show();
				a.setHeaderText("File Reading Error");
				a.setContentText("Please check the file arrangment");
			}

		} else {
			// If no category is selected
			a.setAlertType(AlertType.WARNING);
			// show the dialog
			a.show();
			a.setHeaderText("Selection required");
			a.setContentText("You have to select a category");
		}

	}

}