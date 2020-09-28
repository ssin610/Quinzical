package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class questionBoardController implements Initializable {

    @FXML
    GridPane grid;

    @FXML
    Button winnings;

    @FXML
    Button reset;

    @FXML
    Text resetText;

    // track the x and y positions within the gridpane
    int index_y = 0;
    int index_x = 0;

    public void initialize(URL url, ResourceBundle rb) {
        resetText.setVisible(false);
        reset.setVisible(false);
        winnings.setText("Winnings: $");
        File dir = new File("cat"); // get location of categories folder
        File[] categoryFolder = dir.listFiles();
        if (categoryFolder != null) {
            for (File category : categoryFolder) { // iterate through each category

                index_y = 0;
                Text categoryName = new Text(category.getName().toUpperCase());
                categoryName.setFont(Font.font("Agency FB", 20));
                categoryName.setFill(Color.LIGHTSKYBLUE);
                grid.add(categoryName, index_x, index_y);
                GridPane.setHalignment(categoryName, HPos.CENTER);
                index_x++;
            }
        } else {
            // the case when the category folder is not found
            System.out.println("Category folder not found");
        }

	}
	
}