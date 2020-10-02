package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class MainMenu extends Application {
	
	private static int winnings = 0;
	private static ArrayList<String> answeredQuestions = new ArrayList<String>();
	private static ArrayList<String> addedQuestions = new ArrayList<String>();
	private static ArrayList<String> addedCategories = new ArrayList<String>();
	private static boolean random = true;
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			TextFileReader reader = new TextFileReader();
			File w = new File("winnings");
			if (w.exists()) {
				winnings = Integer.valueOf(reader.read(w).get(0)); // if winnings have previously been saved, read from this
																   // file
				random = false;
			}
			File an = new File("answeredQuestions");
			if (an.exists()) {
				answeredQuestions = (ArrayList<String>) reader.read(an); // if answered questions have previously been saved,
																		// read from this file
			}
			File ad = new File("addedQuestions");
			if (ad.exists()) {
				addedQuestions = (ArrayList<String>) reader.read(ad); // if answered questions have previously been saved,
																		// read from this file
			}
			File ac = new File("addedCategories");
			if (ac.exists()) {
				addedCategories = (ArrayList<String>) reader.read(ac); // if answered questions have previously been saved,
																		// read from this file
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
    public void stop() throws IOException {
        TextFileWriter.write("winnings", winnings, null); // save fields in files
		TextFileWriter.write("answeredQuestions", null, answeredQuestions);
		TextFileWriter.write("addedQuestions", null, addedQuestions);
		TextFileWriter.write("addedCategories", null, addedCategories);
    }

	public static void setWinnings(int value) {
        if (value == 0) {
            winnings = 0;
        }
        winnings += value;
	}

	public static void setRandom(Boolean bool) {
        random = bool;
	}
	
	public static int getWinnings() {
        return winnings;
	}

	public static boolean getRandom() {
        return random;
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
}


