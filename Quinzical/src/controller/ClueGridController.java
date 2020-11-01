package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.event.EventHandler;

import helper.GameData;
import helper.TextFileReader;

/**
 * Controller for the ClueGrid scene
 */
public class ClueGridController implements Initializable {

	@FXML
	GridPane grid;

	@FXML
	Label winnings;

	@FXML
	Button reset;

	@FXML
	Text resetText;

	// store the selected categories
	private static ArrayList<CheckBox> categories = new ArrayList<CheckBox>();

	// store the different parts of a question
	private String question;
	private String answer;
	private String bracket;

	// track the x and y positions within the gridpane
	private int index_y = 0;
	private int index_x = 0;

	// track the progress of the game
	private int completedCategoryCounter = 0;
	
	// track the max possible score a player can achieve
	private int sumValues;

	private Alert a = new Alert(AlertType.NONE);

	/**
	 * Initialize this controller by setting the relevant FXML elements
	 * and their values
	 */
	public void initialize(URL url, ResourceBundle rb) {

		resetText.setVisible(false);
		reset.setVisible(false);
		winnings.setText("Winnings: $" + Integer.toString(GameData.getWinnings()));

		File dir = new File("cat"); // get location of categories folder
		File[] categoryFolder = dir.listFiles();
		if (categoryFolder != null) {
			if (GameData.getCatSelected()) {
				
				for (CheckBox category : categories) { // iterate through each category, randomly generating and displaying clues
					selectAndDisplayCluesRandomly(categoryFolder, category.getText());
				}
				
				GameData.setTotalWinnings(sumValues); 
				GameData.setCatSelected(false); // as 5 categories have been selected, they do not need to
												// be selected again
			} else {
				// read data from file
				selectAndDisplayCluesFromFile();
			}

		} else {

			// the case when the category folder is not found
			a.setAlertType(AlertType.ERROR);
			a.show();
			a.setHeaderText("File Reading Error");
			a.setContentText("Please check the file arrangment");
		}
		
		// when the international section has been unlocked
		if (completedCategoryCounter == 2 && GameData.getInternational() == false) { 
	
			a.setAlertType(AlertType.INFORMATION);
			a.setHeaderText("Congratulations!");
			a.setContentText("You have unlocked the international question section!");
			a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			a.show();

			GameData.setInternational(true); // indicate that the international section has been unlocked
			selectAndDisplayCluesRandomly(categoryFolder, "International"); // add the international section to the list of categories
		}
		
		// when the game has been completed
		if (completedCategoryCounter == 6) { 
			resetText.setVisible(true);
			reset.setVisible(true);
		}
	}

	/**
	 * Randomly select clues from the list of categories selected and display them on the
	 * clue grid
	 * @param categoryFolder the list of categories
	 * @param category the category for which we want to display clues
	 */
	public void selectAndDisplayCluesRandomly(File[] categoryFolder, String category) {

		// arraylist's to store and keep track of which questions have
		// been randomly selected
		ArrayList<Integer> addedIndex = new ArrayList<Integer>();
		ArrayList<String> values = new ArrayList<String>();
		ArrayList<String> questions = new ArrayList<String>();
		index_y = 0;
		
		GameData.addCategory(category);
		
		// display the name of the category on the grid
		Text categoryName = new Text(category.toUpperCase());
		categoryName.setFont(Font.font("System", FontWeight.BOLD, 20));
		categoryName.setFill(Color.LIGHTSKYBLUE);
		grid.add(categoryName, index_x, index_y);
		GridPane.setHalignment(categoryName, HPos.CENTER);
		
		File categoryFile = null;
		// get the category file corresponding to the category string
		for (File file : categoryFolder) {
			if (file.getName().equals(category)) {
				categoryFile = file;
			}
		}

		List<String> lines = TextFileReader.read(categoryFile);

		for (int j = 0; j < 5; j++) { // randomly select 5 clue from the category and parse the results,
									// forming the respective fields
			int randomIndex = ThreadLocalRandom.current().nextInt(0, lines.size());

			while (addedIndex.contains(randomIndex)) { // if the question has already been selected previously
				randomIndex = ThreadLocalRandom.current().nextInt(0, lines.size());
			}
			
			// add the clue and its data to the respective fields
			String value = lines.get(randomIndex).substring(lines.get(randomIndex).lastIndexOf(',') + 1).trim();
			values.add(value);
			questions.add(lines.get(randomIndex));
			addedIndex.add(randomIndex);
		}

		// sort the questions by money value
		Collections.sort(questions, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return Integer.valueOf(o1.substring(o1.lastIndexOf(',') + 1).trim())
						.compareTo(Integer.valueOf(o2.substring(o2.lastIndexOf(',') + 1).trim()));
			}
		});

		// add each clue to the grid under the respective category
		for (int k = 0; k < 5; k++) {
			
			index_y++;
			// make sure the user can only click the lowest money value for each category
			// and then add a button for that clue
			if (index_y == 1) {
				GameData.addQuestion(questions.get(k));
				trimString(questions.get(k));
				addButton(questions.get(k).substring(questions.get(k).lastIndexOf(',') + 1).trim(), true, question,
						answer, bracket, category); // add it to the board
			} else {
				GameData.addQuestion(questions.get(k));
				trimString(questions.get(k));
				addButton(questions.get(k).substring(questions.get(k).lastIndexOf(',') + 1).trim(), false, question,
						answer, bracket, category); // add it to the board
			}
		}
		index_x++;
		
		// Get the max possible value for a complete game
		for (String e: values) {
			sumValues = sumValues + Integer.parseInt(e);
		}
	}

	/**
	 * Select and display categories and clues from previous save data (files) and
	 * display them on the clue grid
	 */
	public void selectAndDisplayCluesFromFile() {
		
		// display the name of the category on the grid
		for (String category : GameData.getAddedCategories()) {
			Text categoryName = new Text(category.toUpperCase());
			categoryName.setFont(Font.font("System", FontWeight.BOLD, 23));
			categoryName.setFill(Color.LIGHTSKYBLUE);
			grid.add(categoryName, index_x, index_y);
			GridPane.setHalignment(categoryName, HPos.CENTER);
			index_x++;
		}
		
		index_x = 0;
		
		// track the amount of valid questions for each of the categories
		int[] validQuestionArray = new int[] { 0, 0, 0, 0, 0, 0 };
		
		// index counter for the valid question array
		int k = 0;

		// iterate through each of the selected questions and add them to the
		// grid if they haven't been answered yet
		for (int i = 0; i < GameData.getAddedQuestions().size(); i++) {
			
			trimString(GameData.getAddedQuestions().get(i));
			if (!(GameData.getAnsweredQuestions().contains(question + " - " + GameData.getAddedCategories().get(i/5)))) {
				
				index_y++;
				trimString(GameData.getAddedQuestions().get(i));
				
				// increment array as this is a valid question (hasn't been answered yet)
				validQuestionArray[k] = validQuestionArray[k] + 1;
				
				// make sure the user can only click the lowest money value for each category
				if (index_y == 1) {
					addButton(
							GameData.getAddedQuestions().get(i)
							.substring(GameData.getAddedQuestions().get(i).lastIndexOf(',') + 1).trim(),
							true, question, answer, bracket, GameData.getAddedCategories().get(i/5)); // add it to the board
				} else {
					addButton(
							GameData.getAddedQuestions().get(i)
							.substring(GameData.getAddedQuestions().get(i).lastIndexOf(',') + 1).trim(),
							false, question, answer, bracket, GameData.getAddedCategories().get(i/5)); // add it to the board
				}
			}

			// after each category's questions have been iterated through, determine whether
			// that category has any valid questions or not (every time 'i' reaches a mulitple of 5)
			if (i == 4 || i == 9 || i == 14 || i == 19 || i == 24 || i == 29) {
				
				// show that the category has been completed
				if (validQuestionArray[k] == 0) {
					Text complete = new Text("Category complete!");
					complete.setFont(Font.font("System", FontWeight.BOLD, 25));
					complete.setFill(Color.LIGHTGREEN);
					complete.setWrappingWidth(150);
					grid.add(complete, index_x, 1);
					GridPane.setHalignment(complete, HPos.CENTER);
					completedCategoryCounter++;
				}
				k++;
				index_x++;
				index_y = 0;
			}
		}
	}

	/**
	 * Split the clue string into 3 parts: The question, the 'What/Who is' 
	 * part, and the answer
	 * @param clue the clue that needs to be split
	 */
	public void trimString(String clue) {
		try {

			String temp[] = clue.split("\\(");
			question = temp[0].substring(0, temp[0].length() - 2);
			String temp2[] = temp[1].split("\\)");
			bracket = "(" + temp2[0].trim() + ")";
			answer = temp2[1].trim().split(",")[0];

		} catch (Exception e) {

			a.setAlertType(AlertType.ERROR);
			a.show();
			a.setHeaderText("Question Reading Error");
			a.setContentText("Please check the question format");
		}
	}

	/**
	 * Add a button to the clue grid containing the dollar value of the clue as
	 * text. Also set the appropriate clue fields in the game answer controller
	 * for when each button is pressed
	 * @param value the dollar value of the clue
	 * @param lowest whether this clue has the lowest value
	 * @param question the question
	 * @param answer the answer to the question
	 * @param bracket the 'What/Who is' part of the question
	 * @param category the category the clue belongs to
	 */
	public void addButton(String value, Boolean lowest, String question, String answer, String bracket, String category) {
		
		Button button = new Button("$" + value);
		button.setPrefSize(190, 80);
		button.setFont(Font.font("System", FontWeight.BOLD, 25));
		button.setStyle("-fx-background-color: #ffc100; ");
		
		// if the money value isn't the lowest for the category, disable the button
		if (!lowest) {
			button.setDisable(true);
			button.setStyle("-fx-background-color: #ACACAC; ");
		}
		
		button.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) { // when the player selections a clue
													// set the respective fields of the question for the game answering scene
				GameAnswerController.setFields(question, answer, bracket, category, Integer.valueOf(value));
				onQuestionButtonPushed(event); // send the event to the buttons event handler
			}
		});
		
		grid.add(button, index_x, index_y);
		GridPane.setHalignment(button, HPos.CENTER);
	}

	/**
	 * Called when the user clicks one of the buttons containing a clue.
	 * They are then taken to the answering scene for that particular clue
	 */
	public void onQuestionButtonPushed(ActionEvent event) {
	
		try {
			Parent viewParent = FXMLLoader.load(getClass().getResource("/view/GameAnswer.fxml"));
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
	 * Called when the user presses the main menu button.
	 * This method changes the scene to the main menu
	 */
	public void onMainMenuPushed(ActionEvent event){
		
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
	 * Called when the game is completed and the user presses
	 * the 'reward screen' button
	 */
	public void onResetPushed(ActionEvent event){

		try {
			Parent viewParent = FXMLLoader.load(getClass().getResource("/view/Reset.fxml"));
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
	
	public static void setCategories(ArrayList<CheckBox> checkboxes) {
		categories = checkboxes;
	}
}