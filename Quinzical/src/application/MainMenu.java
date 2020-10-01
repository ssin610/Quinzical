package application;

import java.io.File;
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
	private static ArrayList<File> addedCategories = new ArrayList<File>();
	private static boolean random = true;
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public static void setWinnings(int value) {
        if (value == 0) {
            winnings = 0;
        }
        winnings += value;
	}

	public static void setRandom() {
        random = false;
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
	
	public static ArrayList<File> getAddedCategories() {
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
        addedQuestions.add(question);
	}
	
	public static void addCategory(File category) {
        addedCategories.add(category);
    }
}


