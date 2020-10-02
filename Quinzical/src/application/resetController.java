package application;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

public class resetController {

    public void onYesButton(ActionEvent event) throws IOException {
        MainMenu.setWinnings(0);
        MainMenu.addAnsweredQuestion(null);
        MainMenu.addAddedQuestion(null);
        MainMenu.addCategory(null);
        MainMenu.setRandom(true);
        File w = new File("winnings");
        w.delete();
        File an = new File("answeredQuestions");
        an.delete();
        File ad = new File("addedQuestions");
        ad.delete();
        File ac = new File("addedCategories");
        ac.delete();
        Parent viewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene viewScene = new Scene(viewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(viewScene);
        window.show();
    }

    public void onNoButton(ActionEvent event) throws IOException {
        Parent viewParent = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene viewScene = new Scene(viewParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(viewScene);
        window.show();
    }
}
