package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.event.EventHandler; 
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ChooseCategoriesController implements Initializable {

	@FXML
	GridPane grid;

	@FXML
	Button cont;

	// track the x and y positions within the gridpane
	int index_y = 0;
	int index_x = 0;
	
	// track how many categories have been selected
	int counter = 0;

	ArrayList<CheckBox> cbs = new ArrayList<CheckBox>();
	Alert a = new Alert(AlertType.NONE);


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		cont.setVisible(false);
		

		File dir = new File("cat"); // get location of categories folder
		File[] categoryFolder = dir.listFiles();

		if (categoryFolder != null) {
			displayCategories(categoryFolder);
		} else {
			// the case when the category folder is not found
			// show alert
			a.setAlertType(AlertType.ERROR);
			// show the dialog
			a.show();
			a.setHeaderText("File Reading Error");
			a.setContentText("Please check the file arrangment");
		}
	}

	/**
	 * Randomly select categories and clues from the list and display them on the
	 * clue grid
	 * 
	 * @param categoryFolder the list of categories
	 */
	public void displayCategories(File[] categoryFolder) {
		for (int i = 0; i < categoryFolder.length; i++) { // iterate through each category

			CheckBox cb = new CheckBox(categoryFolder[i].getName());
			cb.setFont(Font.font("System", FontWeight.BOLD, 20));
			cb.setTextFill(Color.LIGHTSKYBLUE);
			grid.add(cb, index_x, index_y);
			cbs.add(cb);
			EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 

				public void handle(ActionEvent e) 
				{ 
					if (cb.isSelected()) {
						counter++;
					}

					else {
						counter--;
					}
					checkIfContinue();
				} 
			}; 

			// set event to checkbox 
			cb.setOnAction(event); 
			index_x++;

			if (index_x == 3) {
				index_x = 0;
				index_y++;
			}
		}

	}

	public void checkIfContinue() {
		if (counter == 5) {
			cont.setVisible(true);
			for (CheckBox cb : cbs) {
				if (!(cb.isSelected())) {
					cb.setDisable(true);
				}
			}
		}
		else {
			cont.setVisible(false);
			for (CheckBox cb : cbs) {
				cb.setDisable(false);
			}
		}
	}

	public void onMainMenuPushed(ActionEvent event) {
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

	public void onContinuePushed(ActionEvent event) {
		ArrayList<CheckBox> selectedcbs = new ArrayList<CheckBox>();
		for (CheckBox cb : cbs) {
			if (cb.isSelected()) {
				selectedcbs.add(cb);
			}
		}
		ClueGridController.setCheckBoxes(selectedcbs);
		try {
			Parent viewParent = FXMLLoader.load(getClass().getResource("view/ClueGrid.fxml"));
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
