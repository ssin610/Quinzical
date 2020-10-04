package controller;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Node;
import javafx.stage.Stage;

import application.Main;

public class ResetController {

    Alert a = new Alert(AlertType.NONE);

    public void onYesButton(ActionEvent event) {
        Main.setWinnings(0);
        Main.addAnsweredQuestion(null);
        Main.addAddedQuestion(null);
        Main.addCategory(null);
        Main.setRandom(true);
        File w = new File("winnings");
        w.delete();
        File an = new File("answeredQuestions");
        an.delete();
        File ad = new File("addedQuestions");
        ad.delete();
        File ac = new File("addedCategories");
        ac.delete();

        try {
            Parent viewParent = FXMLLoader.load(getClass().getResource("view/MainMenu.fxml"));
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

    public void onNoButton(ActionEvent event) {

        try {
            Parent viewParent = FXMLLoader.load(getClass().getResource("view/Home.fxml"));
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
