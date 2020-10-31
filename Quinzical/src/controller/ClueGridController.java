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

public class ClueGridController implements Initializable {

	@FXML
	GridPane grid;

	@FXML
	Label winnings;

	@FXML
	Button reset;

	@FXML
	Text resetText;

	// track the x and y positions within the gridpane
	int index_y = 0;
	int index_x = 0;

	// track the progress of the game
	int completedCategoryCounter = 0;

	// store the selected categories
	private static ArrayList<CheckBox> cbs = new ArrayList<CheckBox>();

	// store the different parts of a question
	String showtext;
	String answer;
	String bracket;
	Alert a = new Alert(AlertType.NONE);

	

	private int sumValues;
	
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
			if (GameData.getRandom()) {
				for (CheckBox cb : cbs) { // iterate through each category
					selectedAndDisplayCluesFromCBS(categoryFolder, cb.getText());
				}
				GameData.setTotalWings(sumValues); // Set and to be used in RestController
				GameData.setRandom(false); // as the categories and clues have been randomly selected, they do not need to
				// be randomly selected again
			} else {
				selectAndDisplayCluesFromFile();
			}

		} else {
			// the case when the category folder is not found
			// show alert
			a.setAlertType(AlertType.ERROR);
			// show the dialog
			a.show();
			a.setHeaderText("File Reading Error");
			a.setContentText("Please check the file arrangment");
		}

		if (completedCategoryCounter == 2 && GameData.getInternational() == false) { // when the international section has been unlocked
			// show alert
			a.setAlertType(AlertType.INFORMATION);
			a.setHeaderText("Congratulations!");
			a.setContentText("You have unlocked the international question section!");
			
			a.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		
			// show the dialog
			a.show();
			
			GameData.setInternational(true);
			selectedAndDisplayCluesFromCBS(categoryFolder, "International");

		}

		if (completedCategoryCounter == 6) { // when the game has been completed
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
	public void selectedAndDisplayCluesFromCBS(File[] categoryFolder, String category) {


		// arraylist's to store and keep track of which questions have
		// been randomly selected
		ArrayList<Integer> addedIndex = new ArrayList<Integer>();
		ArrayList<String> values = new ArrayList<String>();
		ArrayList<String> questions = new ArrayList<String>();
		GameData.addCategory(category);
		index_y = 0;
		Text categoryName = new Text(category.toUpperCase());
		categoryName.setFont(Font.font("System", FontWeight.BOLD, 20));
		categoryName.setFill(Color.LIGHTSKYBLUE);
		grid.add(categoryName, index_x, index_y);
		GridPane.setHalignment(categoryName, HPos.CENTER);
		File categoryFile = null;
		for (File file : categoryFolder) {
			if (file.getName().equals(category)) {
				categoryFile = file;
			}
		}

		TextFileReader reader = new TextFileReader();
		List<String> lines = reader.read(categoryFile);

		for (int j = 0; j < 5; j++) { // select 5 questions from the category and parse the results,
			// forming the respective fields
			int randomIndex = ThreadLocalRandom.current().nextInt(0, lines.size());

			while (addedIndex.contains(randomIndex)) { // if the question has already been selected previously
				randomIndex = ThreadLocalRandom.current().nextInt(0, lines.size());
			}

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

		// add each question to the grid under the respective category
		for (int k = 0; k < 5; k++) {
			index_y++;

			// make sure the user can only click the lowest money value for each category
			if (index_y == 1) {
				GameData.addAddedQuestion(questions.get(k));
				trimString(questions.get(k));
				addButton(questions.get(k).substring(questions.get(k).lastIndexOf(',') + 1).trim(), true, showtext,
						answer, bracket, category); // add it to the board
			} else {
				GameData.addAddedQuestion(questions.get(k));
				trimString(questions.get(k));
				addButton(questions.get(k).substring(questions.get(k).lastIndexOf(',') + 1).trim(), false, showtext,
						answer, bracket, category); // add it to the board
			}
		}
		index_x++;
		// Get the max possible value for a complete game
		for (String e: values) {
			int temp = Integer.parseInt(e);
			sumValues=sumValues+temp;
		}


	}

	/**
	 * Select and display categories and clues from previous save data (files) and
	 * display them on the clue grid
	 */
	public void selectAndDisplayCluesFromFile() {

		for (String category : GameData.getAddedCategories()) {
			Text categoryName = new Text(category.toUpperCase());
			categoryName.setFont(Font.font("System", FontWeight.BOLD, 23));
			categoryName.setFill(Color.LIGHTSKYBLUE);
			grid.add(categoryName, index_x, index_y);
			GridPane.setHalignment(categoryName, HPos.CENTER);
			index_x++;
		}

		index_x = 0;
		// track the amount of valid questions for each of the 5 categories
		int[] validQuestionArray = new int[] { 0, 0, 0, 0, 0, 0 };
		// index counter for the valid question array
		int k = 0;

		// iterate through each of the selected questions and add them to the
		// grid if they haven't been answered yet
		for (int i = 0; i < GameData.getAddedQuestions().size(); i++) {
			trimString(GameData.getAddedQuestions().get(i));

			if (!(GameData.getAnsweredQuestions().contains(showtext + " - " + GameData.getAddedCategories().get(i/5)))) {
				index_y++;
				trimString(GameData.getAddedQuestions().get(i));
				// increment array as this is a valid question (hasn't been answered yet)
				validQuestionArray[k] = validQuestionArray[k] + 1;
				// make sure the user can only click the lowest money value for each category
				if (index_y == 1) {
					addButton(
							GameData.getAddedQuestions().get(i)
							.substring(GameData.getAddedQuestions().get(i).lastIndexOf(',') + 1).trim(),
							true, showtext, answer, bracket, GameData.getAddedCategories().get(i/5)); // add it to the board
				} else {
					addButton(
							GameData.getAddedQuestions().get(i)
							.substring(GameData.getAddedQuestions().get(i).lastIndexOf(',') + 1).trim(),
							false, showtext, answer, bracket, GameData.getAddedCategories().get(i/5)); // add it to the board
				}
			}

			// after each category's questions have been iterated through, determine whether
			// that category has a valid question or not
			if (i == 4 || i == 9 || i == 14 || i == 19 || i == 24 || i == 29) {
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
			answer = temp2[1].trim().split(",")[0];

		} catch (Exception e) {

			a.setAlertType(AlertType.ERROR);
			// show the dialog
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
		// if the money value isn't the lowest for the category
		if (!lowest) {
			button.setDisable(true);
			button.setStyle("-fx-background-color: #ACACAC; ");
		}
		button.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) { // when the player selections a clue
				// set the respective fields of the question for the game answering scene
				GameAnswerController.setQuestion(question);
				GameAnswerController.setAnswer(answer);
				GameAnswerController.setValue(Integer.valueOf(value));
				GameAnswerController.setBracket(bracket);
				GameAnswerController.setCategory(category);
				onQuestionButtonPushed(event); // send the event to the buttons event handler
			}
		});
		grid.add(button, index_x, index_y);
		GridPane.setHalignment(button, HPos.CENTER);
	}

	public static void setCheckBoxes(ArrayList<CheckBox> checkboxes) {
		cbs = checkboxes;
	}
	
	/**
	 * Called when the user clicks one of the buttons containing a clue.
	 * They are then taken to the answering scene for that particular clue
	 */
	public void onQuestionButtonPushed(ActionEvent event) {
		Parent viewParent;
		try {
			viewParent = FXMLLoader.load(getClass().getResource("/view/GameAnswer.fxml"));
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
	 * Called when the user presses the main menu button.
	 * This method changes the scene to the main menu
	 */
	public void onMainMenuPushed(ActionEvent event){
		Parent viewParent;
		try {
			viewParent = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
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
	 * Called when the game is completed and the user presses
	 * the 'reward screen' button
	 */
	public void onResetPushed(ActionEvent event){
		Parent viewParent;
		try {
			viewParent = FXMLLoader.load(getClass().getResource("/view/Reset.fxml"));
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
}