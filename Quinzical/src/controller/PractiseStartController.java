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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

/**
 * Controller for the PractiseStart scene
 */
public class PractiseStartController implements Initializable {

	@FXML
	Button start_prac_button;

	@FXML
	ChoiceBox<String> cat_choice;

	// arrays to store categories and questions
	private List<String> categories = new ArrayList<String>();
	private List<String> questions = new ArrayList<String>();

	// store the different parts of a clue
	private String question;
	private String answer;
	private String bracket;

	private Alert a = new Alert(AlertType.NONE);

	/**
	 * Initialize this controller by setting the relevant FXML elements
	 * and their values
	 */
	public void initialize(URL url, ResourceBundle rb) {
		
		categories.clear();
		questions.clear();

		try {
			readfile();
			cat_choice.setItems(FXCollections.observableList(categories));
			cat_choice.setStyle("-fx-font: 18px \"Serif\";");
		} catch (IOException e) {
			e.printStackTrace();

			a.setAlertType(AlertType.ERROR);
			a.show();
			a.setHeaderText("File Reading Error");
			a.setContentText("Please check the file arrangment");
		}
	}

	/**
	 * Split the clue string into 3 parts: The question, the 'What/Who is' 
	 * part, and the answer
	 * @param clue the question that needs to be split
	 */
	public void trimString(String clue) {
		
		try {
			String temp[] = clue.split("\\(");
			question = temp[0].substring(0, temp[0].length() - 2);
			String temp2[] = temp[1].split("\\)");
			bracket = "(" + temp2[0].trim() + ")";
			answer = temp2[1].trim();

		} catch (Exception e) {
			e.printStackTrace();

			a.setAlertType(AlertType.ERROR);
			a.show();
			a.setHeaderText("Question Reading Error");
			a.setContentText("Please check the question format");
		}
	}

	/**
	 * Read lines in the selected category and add this to the
	 * questions array
	 * @throws IOException
	 */
	public void readSelectedfile() throws IOException {
		
		String selectedCat = cat_choice.getSelectionModel().getSelectedItem();
		String currentPath = System.getProperty("user.dir");

		File file = new File(currentPath + "/cat/" + selectedCat);
		if (file.exists()) {
			FileReader fileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);
			String lineContent = null;
			while ((lineContent = br.readLine()) != null) {
				questions.add(lineContent);
			}
			br.close();
			fileReader.close();
		}

		try {
			Collections.shuffle(questions); // Shuffle the list
			trimString(questions.get(0)); // Process the line after shuffle
		} catch(Exception e) {
			e.printStackTrace();

			a.setAlertType(AlertType.ERROR);
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
		
		String currentPath = System.getProperty("user.dir");

		File file = new File(currentPath + "/cat");
		File[] array = file.listFiles();
		for (int i = 0; i < array.length; i++) {
			if (array[i].isFile()) {
				String filename = array[i].getName();
				categories.add(filename);
			}
		}
	}

	/**
	 * Called when the user presses the start practise button.
	 * This method then switches the scene to the question answering
	 * scene and passes the relevant question parameters to the controller
	 */
	public void onStartPushed(ActionEvent event) {

		// if the user has selected a category
		if (cat_choice.getSelectionModel().getSelectedItem() != null) {

			try {
				readSelectedfile();
				// Pass parameter across
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PractiseAnswer.fxml"));
				loader.load();
				PractiseAnswerController controller = loader.getController();
				// Pass value to the next page
				controller.setStrings(question, answer, bracket);

				Parent viewParent = FXMLLoader.load(getClass().getResource("/view/PractiseAnswer.fxml"));
				Scene viewScene = new Scene(viewParent);
				Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
				window.setScene(viewScene);
				window.setResizable(false);
				window.show();
				
			} catch (Exception e) {
				e.printStackTrace();
				
				a.setAlertType(AlertType.ERROR);
				a.show();
				a.setHeaderText("File Reading Error");
				a.setContentText("Please check the file arrangment");
			}

		} else {
			// If no category is selected
			a.setAlertType(AlertType.WARNING);
			a.show();
			a.setHeaderText("Selection required");
			a.setContentText("You have to select a category");
		}
	}
}