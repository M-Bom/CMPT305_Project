package ca.macewan.cmpt305.groupproject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PropertyAssessmentsController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}