package controller;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

import application.Main;

public class ResetController {

    public void onYesButton(ActionEvent event) throws IOException {
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
        Parent viewParent = FXMLLoader.load(getClass().getResource("view/MainMenu.fxml"));
        Scene viewScene = new Scene(viewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(viewScene);
        window.show();
    }

    public void onNoButton(ActionEvent event) throws IOException {
        Parent viewParent = FXMLLoader.load(getClass().getResource("view/Home.fxml"));
        Scene viewScene = new Scene(viewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(viewScene);
        window.show();
    }
}
