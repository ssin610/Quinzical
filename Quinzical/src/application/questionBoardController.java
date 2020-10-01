package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javafx.event.EventHandler;

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

    // String quest="This native bird lays the largest egg in relation to their body
    // size of any species of bird in the world, (What is) the Kiwi";
    String showtext;
    String answer;
    String bracket;
    Alert a = new Alert(AlertType.NONE);

    public void initialize(URL url, ResourceBundle rb) {
        resetText.setVisible(false);
        reset.setVisible(false);
        winnings.setText("Winnings: $");
        File dir = new File("cat"); // get location of categories folder
        File[] categoryFolder = dir.listFiles();
        if (categoryFolder != null) {
            if (MainMenu.getRandom()) {
                for (int i = 0; i < 5; i++) { // iterate through each category
                    ArrayList<Integer> addedIndex = new ArrayList<Integer>();
                    ArrayList<String> values = new ArrayList<String>();
                    ArrayList<String> questions = new ArrayList<String>();
                    File randomCat = getRandom(categoryFolder);
                    while (MainMenu.getAddedCategories().contains(randomCat)) {
                        randomCat = getRandom(categoryFolder);
                    }
                    MainMenu.addCategory(randomCat);
                    index_y = 0;
                    Text categoryName = new Text(randomCat.getName().toUpperCase());
                    categoryName.setFont(Font.font("Agency FB", 20));
                    categoryName.setFill(Color.LIGHTSKYBLUE);
                    grid.add(categoryName, index_x, index_y);
                    GridPane.setHalignment(categoryName, HPos.CENTER);

                    TextFileReader reader = new TextFileReader();
                    List<String> lines = reader.read(randomCat);
                    for (int j = 0; j < 5; j++) { // go through every line of the category file and parse the results,
                                                  // forming
                                                  // the respective fields
                        int randomIndex = ThreadLocalRandom.current().nextInt(0, lines.size());
                        while (addedIndex.contains(randomIndex)) {
                            randomIndex = ThreadLocalRandom.current().nextInt(0, lines.size());
                        }

                        String value = lines.get(randomIndex).substring(lines.get(randomIndex).lastIndexOf(',') + 1)
                                .trim();
                        values.add(value);
                        questions.add(lines.get(randomIndex));
                        addedIndex.add(randomIndex);

                    }

                    Collections.sort(questions, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return Integer.valueOf(o1.substring(o1.lastIndexOf(',') + 1).trim())
                                    .compareTo(Integer.valueOf(o2.substring(o2.lastIndexOf(',') + 1).trim()));
                        }
                    });

                    for (int k = 0; k < 5; k++) {
                        index_y++;
                        if (index_y == 1) {
                            MainMenu.addAddedQuestion(questions.get(k));
                            trimString(questions.get(k));
                            System.out
                                    .println(questions.get(k).substring(questions.get(k).lastIndexOf(',') + 1).trim());
                            System.out.println(showtext);
                            System.out.println(answer);
                            System.out.println(bracket);
                            addButton(questions.get(k).substring(questions.get(k).lastIndexOf(',') + 1).trim(), true,
                                    showtext, answer, bracket); // add it to the board
                        } else {
                            MainMenu.addAddedQuestion(questions.get(k));
                            trimString(questions.get(k));
                            addButton(questions.get(k).substring(questions.get(k).lastIndexOf(',') + 1).trim(), false,
                                    showtext, answer, bracket); // add it to the board
                        }
                    }

                    index_x++;
                }
            }
            if (MainMenu.getRandom()) {
                MainMenu.setRandom();
            } else {
                for (File category : MainMenu.getAddedCategories()) {
                    Text categoryName = new Text(category.getName().toUpperCase());
                    categoryName.setFont(Font.font("Agency FB", 20));
                    categoryName.setFill(Color.LIGHTSKYBLUE);
                    grid.add(categoryName, index_x, index_y);
                    GridPane.setHalignment(categoryName, HPos.CENTER);
                    index_x++;
                }
                index_x = 0;
                for (int i = 0; i < MainMenu.getAddedQuestions().size(); i++) {

                    if (i % 5 == 0 && i != 0) {
                        index_x++;
                        index_y = 0;
                    }
                    index_y++;
                    trimString(MainMenu.getAddedQuestions().get(i));
                    if (index_y == 1) {
                        addButton(
                                MainMenu.getAddedQuestions().get(i)
                                        .substring(MainMenu.getAddedQuestions().get(i).lastIndexOf(',') + 1).trim(),
                                true, showtext, answer, bracket); // add it to the board
                    } else {
                        addButton(
                                MainMenu.getAddedQuestions().get(i)
                                        .substring(MainMenu.getAddedQuestions().get(i).lastIndexOf(',') + 1).trim(),
                                false, showtext, answer, bracket); // add it to the board
                    }
                }

            }

        } else {
            // the case when the category folder is not found
            System.out.println("Category folder not found");
        }

    }

    public static File getRandom(File[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    /**
     * Split the string into 3 parts
     * 
     * @param question
     */
    public void trimString(String question) {
        try {

            String temp[] = question.split("\\(");
            showtext = temp[0].substring(0, temp[0].length() - 2);
            ;
            String temp2[] = temp[1].split("\\)");
            bracket = "( " + temp2[0].trim() + " )";
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
        button.setFont(Font.font("Agency FB", 20));
        button.setStyle("-fx-background-color: #ffc100; ");
        if (!lowest) {
            button.setDisable(true);
            button.setStyle("-fx-background-color: #ACACAC; ");
        }
        button.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) { // when the player selections a question
                try {
                    gameAnswerController.setQuestion(question);
                    gameAnswerController.setAnswer(answer);
                    gameAnswerController.setValue(Integer.valueOf(text));
                    gameAnswerController.setBracket(bracket);
                    onQuestionButtonPushed(event); // send the event to the buttons event handler
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        grid.add(button, index_x, index_y);
        GridPane.setHalignment(button, HPos.CENTER);
    }

    public void onQuestionButtonPushed(ActionEvent event) throws IOException {
        Parent viewParent = FXMLLoader.load(getClass().getResource("gameAnswer.fxml"));
        Scene viewScene = new Scene(viewParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(viewScene);
        window.show();
    }

}