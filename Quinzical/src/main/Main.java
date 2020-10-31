package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;

import helper.TextFileReader;
import helper.TextFileWriter;

public class Main extends Application {

	// store the players winnings, questions they have answered, questions and categories that have been randomly selected,
	// and whether the categories and clues need to be randomly generated again
	private static int winnings = 0;
	private static ArrayList<String> answeredQuestions = new ArrayList<String>();
	private static ArrayList<String> addedQuestions = new ArrayList<String>();
	private static ArrayList<String> addedCategories = new ArrayList<String>();
	private static boolean random = true;
	private static int _totalWin=0;
	private static boolean international = false;
	
	Alert a = new Alert(AlertType.NONE); // Use for warning display
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../view/Home.fxml"));
			primaryStage.setTitle("Quinzical");
			primaryStage.setScene(new Scene(root, 1300, 700));
			primaryStage.setResizable(false);
			primaryStage.show();
			TextFileReader reader = new TextFileReader();
			File w = new File("winnings");
			if (w.exists()) {
				winnings = Integer.valueOf(reader.read(w).get(0)); // if winnings have previously been saved, read from
				_totalWin = Integer.valueOf(reader.read(w).get(1)); // if total winnings have previously been saved, read from
			}
			File an = new File("answeredQuestions");
			if (an.exists()) {
				answeredQuestions = (ArrayList<String>) reader.read(an); // if answered questions have previously been
																			// saved, read from this file
			}
			File ad = new File("addedQuestions");
			if (ad.exists()) {
				addedQuestions = (ArrayList<String>) reader.read(ad); // if added questions have previously been
																		// saved, read from this file
				random = false;
			}
			File ac = new File("addedCategories");
			if (ac.exists()) {
				addedCategories = (ArrayList<String>) reader.read(ac); // if answered questions have previously been
																		// saved, read from this file
			}
			File ib = new File("international");
			if (ib.exists()) {
				international = Boolean.valueOf(reader.read(ib).get(0)); // if answered questions have previously been
																		// saved, read from this file
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * This method will be called at any stage the window/programme is closed 
	 */
	@Override
	public void stop() throws IOException {
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(Integer.toString(winnings));
		temp.add(Integer.toString(_totalWin));
	
		if (addedCategories.size() != 0) {
			TextFileWriter.write("winnings", String.valueOf(winnings), null);
			TextFileWriter.write("international", String.valueOf(international), null);
			TextFileWriter.write("winnings", null, temp);// save winngs to winnings and total winnings as the second line
			TextFileWriter.write("answeredQuestions", null, answeredQuestions);
			TextFileWriter.write("addedQuestions", null, addedQuestions);
			TextFileWriter.write("addedCategories", null, addedCategories);
		}
		
		// Stop audios from playing when windows are closed
		Thread thread = new Thread() {
			@Override
			public void run() {
				String cmd = "spd-say \" \"";
				ProcessBuilder ttsBuilder = new ProcessBuilder("bash", "-c", cmd);
				try {
					ttsBuilder.start();
				} catch (IOException e) {
					a.setAlertType(AlertType.ERROR);
					a.show();
					a.setHeaderText("Audio System Crash");
					a.setContentText("Please make sure spd-say is installed (in READ.md) and restart the game");
				}
			}
		};
		thread.start();
		
	}
	
	public static void setTotalWings(int value) {
		_totalWin=value;
	}
	
	public static int getTotalWings() {
		return _totalWin;
	}

	public static void setWinnings(int value) {
		if (value == 0) {
			winnings = 0;
		}
		winnings += value;
	}

	/**
     * Set whether categories and clues need to be randomly selected rather than
	 * reading from a file (on first iteration of game / after game reset)
     * @param bool whether categories and clues need to be randomly selected
     */
	public static void setRandom(Boolean bool) {
		random = bool;
	}

	public static void addAnsweredQuestion(String question) {
		if (question == null) {
			answeredQuestions.clear();
		} else {
			answeredQuestions.add(question);
		}
	}

	public static void addAddedQuestion(String question) {
		if (question == null) {
			addedQuestions.clear();
		} else {
			addedQuestions.add(question);
		}
	}

	public static void addCategory(String category) {
		if (category == null) {
			addedCategories.clear();
		} else {
			addedCategories.add(category);
		}
	}

	public static int getWinnings() {
		return winnings;
	}

	public static boolean getRandom() {
		return random;
	}
	
	public static boolean getInternational() {
		return international;
	}
	
	public static void setInternational(Boolean bool) {
		international = bool;
	}

	public static ArrayList<String> getAddedQuestions() {
		return addedQuestions;
	}

	public static ArrayList<String> getAnsweredQuestions() {
		return answeredQuestions;
	}

	public static ArrayList<String> getAddedCategories() {
		return addedCategories;
	}
}