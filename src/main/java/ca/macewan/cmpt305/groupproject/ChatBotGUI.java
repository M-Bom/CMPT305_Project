package ca.macewan.cmpt305.groupproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatBotGUI extends Application {
    private ChatBot chatBot;
    private TextArea chatArea;
    private TextField inputField;

    @Override
    public void start(Stage primaryStage) {
        try {
            chatBot = new ChatBot(
                    "src/main/resources/Property_Assessment_Data_2024.csv",
                    "Edmonton_Public_School_Board.csv",
                    "Edmonton_Neighbourhoods.csv"
            );
        } catch (IOException e) {
            showError("Error loading data: " + e.getMessage());
            return;
        }

        primaryStage.setTitle("ChatBot Application");

        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPrefHeight(400);

        inputField = new TextField();
        inputField.setPromptText("Type your message...");

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> handleUserInput());

        VBox layout = new VBox(10, chatArea, inputField, sendButton);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleUserInput() {
        String userInput = inputField.getText().trim();
        if (userInput.isEmpty()) return;

        chatArea.appendText("You: " + userInput + "\n");

        if (userInput.equalsIgnoreCase("exit")) {
            chatArea.appendText("ChatBot: Goodbye!\n");
            try {
                Thread.sleep(1000); // Short delay before closing (optional)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.exit(0); // Closes the entire application
        }
        if (userInput.equalsIgnoreCase("help")) {
            chatArea.appendText("ChatBot: Here are some things you can ask me:\n- property assessment for [ID]\n- filter by [criteria]\n- mean assessment value\n- median assessment value\n- total number of properties\n- list all neighbourhoods\n- neighbourhood details for [NAME]\n- location for property [ID]\n- assessment class for property [ID]\n- total number of schools\n- school details for [ID]\n- exit to leave the application\n");
            inputField.clear();
            return;
        }

        String response = chatBot.handleQuery(userInput);
        chatArea.appendText("ChatBot: " + response + "\n");

        inputField.clear();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}