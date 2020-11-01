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

/**
 * Controller for the ChooseCategories scene
 */
public class ChooseCategoriesController implements Initializable {

	@FXML
	GridPane grid;

	@FXML
	Button cont;

	// store all categories
	private ArrayList<CheckBox> categories = new ArrayList<CheckBox>();

	// track the x and y positions within the gridpane
	private int index_y = 0;
	private int index_x = 0;

	// track how many categories have been selected
	private int counter = 0;

	private Alert a = new Alert(AlertType.NONE);

	/**
	 * Initialize this controller by setting the relevant FXML elements
	 * and their values
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		cont.setVisible(false);

		File dir = new File("cat"); // get location of categories folder
		File[] categoryFolder = dir.listFiles();

		if (categoryFolder != null) {
			displayCategories(categoryFolder);
		} else {
			// the case when the category folder is not found

			a.setAlertType(AlertType.ERROR);
			a.show();
			a.setHeaderText("File Reading Error");
			a.setContentText("Please check the file arrangment");
		}
	}

	/**
	 * Display each category as a checkbox for the user to select.
	 * The user must select 5 categories
	 * @param categoryFolder the list of categories
	 */
	public void displayCategories(File[] categoryFolder) {
		
		for (int i = 0; i < categoryFolder.length; i++) { // iterate through each category and add each category as a checkbox
														  // except the international category
			if (!(categoryFolder[i].getName().equalsIgnoreCase("International"))) {
				
				CheckBox cb = new CheckBox(categoryFolder[i].getName());
				cb.setFont(Font.font("System", FontWeight.BOLD, 20));
				cb.setTextFill(Color.LIGHTSKYBLUE);
				grid.add(cb, index_x, index_y);
				categories.add(cb);
				
				EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
					public void handle(ActionEvent e) 
					{ 
						if (cb.isSelected()) {
							counter++;
						}
						else {
							counter--;
						}
						checkIfCanContinue();
					} 
				}; 

				// set event to checkbox 
				cb.setOnAction(event); 
				
				index_x++;
				// show every 4th checkbox on a new grid row
				if (index_x == 3) {
					index_x = 0;
					index_y++;
				}
			}
		}
	}

	/**
	 * Check whether the user has selected 5 categories.
	 * Disable/enable the checkboxes accordingly
	 */
	public void checkIfCanContinue() {
		
		if (counter == 5) {
			cont.setVisible(true);
			for (CheckBox cb : categories) {
				if (!(cb.isSelected())) {
					cb.setDisable(true);
				}
			}
		}
		else {
			cont.setVisible(false);
			for (CheckBox cb : categories) {
				cb.setDisable(false);
			}
		}
	}

	/**
	 * Called when the user presses the main menu button.
	 * This method changes the scene to the main menu 
	 */
	public void onMainMenuPushed(ActionEvent event) {
		
		try {
			Parent viewParent = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
			Scene viewScene = new Scene(viewParent);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(viewScene);
			window.setResizable(false);
			window.show();
		} catch (IOException e) {
			e.printStackTrace();
			
			a.setAlertType(AlertType.ERROR);
			a.show();
			a.setHeaderText("Fatal Error");
			a.setContentText("Please restart the game");
		}
	}

	/**
	 * Called when the user selects 5 categories and then presses
	 * the continue button. The scene then changes to the clue grid
	 */
	public void onContinuePushed(ActionEvent event) {
		
		ArrayList<CheckBox> selectedCategories = new ArrayList<CheckBox>();
		for (CheckBox cb : categories) {
			if (cb.isSelected()) {
				selectedCategories.add(cb);
			}
		}
		
		ClueGridController.setCategories(selectedCategories);
		try {
			Parent viewParent = FXMLLoader.load(getClass().getResource("/view/ClueGrid.fxml"));
			Scene viewScene = new Scene(viewParent);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(viewScene);
			window.setResizable(false);
			window.show();
		} catch (IOException e) {
			e.printStackTrace();
			
			a.setAlertType(AlertType.ERROR);
			a.show();
			a.setHeaderText("Fatal Error");
			a.setContentText("Please restart the game");
		}
	}
}