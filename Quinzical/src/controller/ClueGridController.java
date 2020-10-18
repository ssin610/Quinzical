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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.event.EventHandler;

import application.Main;
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

	public void initialize(URL url, ResourceBundle rb) {
		resetText.setVisible(false);
		reset.setVisible(false);
		winnings.setText("Winnings: $" + Integer.toString(Main.getWinnings()));

		File dir = new File("cat"); // get location of categories folder
		File[] categoryFolder = dir.listFiles();
		if (categoryFolder != null) {
			if (Main.getRandom()) {
				selectedAndDisplayCluesFromCBS(categoryFolder);
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

		if (completedCategoryCounter == 5) { // when the game has been completed
			resetText.setVisible(true);
			reset.setVisible(true);
		}
	}

	/**
	 * Randomly select clues from the list of categories selected and display them on the
	 * clue grid
	 * @param categoryFolder the list of categories
	 */
	public void selectedAndDisplayCluesFromCBS(File[] categoryFolder) {
		for (CheckBox cb : cbs) { // iterate through each category
			
			// arraylist's to store and keep track of which questions have
			// been randomly selected
			ArrayList<Integer> addedIndex = new ArrayList<Integer>();
			ArrayList<String> values = new ArrayList<String>();
			ArrayList<String> questions = new ArrayList<String>();
			Main.addCategory(cb.getText());
			index_y = 0;
			Text categoryName = new Text(cb.getText().toUpperCase());
			categoryName.setFont(Font.font("System", FontWeight.BOLD, 20));
			categoryName.setFill(Color.LIGHTSKYBLUE);
			grid.add(categoryName, index_x, index_y);
			GridPane.setHalignment(categoryName, HPos.CENTER);
			File categoryFile = null;
			for (File file : categoryFolder) {
				if (file.getName().equals(cb.getText())) {
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
					Main.addAddedQuestion(questions.get(k));
					trimString(questions.get(k));
					addButton(questions.get(k).substring(questions.get(k).lastIndexOf(',') + 1).trim(), true, showtext,
							answer, bracket); // add it to the board
				} else {
					Main.addAddedQuestion(questions.get(k));
					trimString(questions.get(k));
					addButton(questions.get(k).substring(questions.get(k).lastIndexOf(',') + 1).trim(), false, showtext,
							answer, bracket); // add it to the board
				}
			}
			index_x++;
		}
		Main.setRandom(false); // as the categories and clues have been randomly selected, they do not need to
							   // be randomly selected again
	}

	/**
	 * Select and display categories and clues from previous save data (files) and
	 * display them on the clue grid
	 */
	public void selectAndDisplayCluesFromFile() {

		for (String category : Main.getAddedCategories()) {
			Text categoryName = new Text(category.toUpperCase());
			categoryName.setFont(Font.font("System", FontWeight.BOLD, 23));
			categoryName.setFill(Color.LIGHTSKYBLUE);
			grid.add(categoryName, index_x, index_y);
			GridPane.setHalignment(categoryName, HPos.CENTER);
			index_x++;
		}

		index_x = 0;
		// track the amount of valid questions for each of the 5 categories
		int[] validQuestionArray = new int[] { 0, 0, 0, 0, 0 };
		// index counter for the valid question array
		int k = 0;

		// iterate through each of the selected questions and add them to the
		// grid if they haven't been answered yet
		for (int i = 0; i < Main.getAddedQuestions().size(); i++) {
			trimString(Main.getAddedQuestions().get(i));

			if (!(Main.getAnsweredQuestions().contains(showtext))) {
				index_y++;
				trimString(Main.getAddedQuestions().get(i));
				// increment array as this is a valid question (hasn't been answered yet)
				validQuestionArray[k] = validQuestionArray[k] + 1;
				// make sure the user can only click the lowest money value for each category
				if (index_y == 1) {
					addButton(
							Main.getAddedQuestions().get(i)
							.substring(Main.getAddedQuestions().get(i).lastIndexOf(',') + 1).trim(),
							true, showtext, answer, bracket); // add it to the board
				} else {
					addButton(
							Main.getAddedQuestions().get(i)
							.substring(Main.getAddedQuestions().get(i).lastIndexOf(',') + 1).trim(),
							false, showtext, answer, bracket); // add it to the board
				}
			}

			// after each category's questions have been iterated through, determine whether
			// that category has a valid question or not
			if (i == 4 || i == 9 || i == 14 || i == 19 || i == 24) {
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
	 * Split the string into 3 parts
	 * 
	 * @param question the question
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

	public void addButton(String text, Boolean lowest, String question, String answer, String bracket) {
		Button button = new Button("$" + text);
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
				GameAnswerController.setValue(Integer.valueOf(text));
				GameAnswerController.setBracket(bracket);
				onQuestionButtonPushed(event); // send the event to the buttons event handler
			}
		});
		grid.add(button, index_x, index_y);
		GridPane.setHalignment(button, HPos.CENTER);
	}

	public static void setCheckBoxes(ArrayList<CheckBox> checkboxes) {
		cbs = checkboxes;
	}

	public void onQuestionButtonPushed(ActionEvent event) {
		Parent viewParent;
		try {
			viewParent = FXMLLoader.load(getClass().getResource("view/GameAnswer.fxml"));
			Scene viewScene = new Scene(viewParent);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(viewScene);
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

	public void onMainMenuPushed(ActionEvent event){
		Parent viewParent;
		try {
			viewParent = FXMLLoader.load(getClass().getResource("view/MainMenu.fxml"));
			Scene viewScene = new Scene(viewParent);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(viewScene);
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
	 * Called when the game is completed
	 */
	public void onResetPushed(ActionEvent event){
		Parent viewParent;
		try {
			viewParent = FXMLLoader.load(getClass().getResource("view/Reset.fxml"));
			Scene viewScene = new Scene(viewParent);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(viewScene);
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