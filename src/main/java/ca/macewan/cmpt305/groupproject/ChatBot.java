package ca.macewan.cmpt305.groupproject;

import java.util.Scanner;
import java.io.IOException;

public class ChatBot {
    // PropertyAssessments object that holds the property data from the CSV file.
    private PropertyAssessments propertyAssessments;

    // Constructor: Initializes the propertyAssessments object by reading from the given CSV file.
    public ChatBot(String csvFileName) throws IOException {
        this.propertyAssessments = new PropertyAssessments(csvFileName);
    }

    // Starts the chatbot, handling user input and responding to queries.
    public void start() {
        Scanner scanner = new Scanner(System.in);  // Create scanner to read user input.
        System.out.println("Welcome to the Property Assessment ChatBot! Type 'exit' to quit or 'help' to see available commands.");

        // Main loop to keep the chatbot running until the user types 'exit'.
        while (true) {
            System.out.print("Ask a question: ");
            String input = scanner.nextLine().trim();  // Read user input and trim any extra spaces.

            // Exit the chatbot if the user types 'exit'.
            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            }

            // Show help instructions if the user types 'help'.
            if (input.equalsIgnoreCase("help")) {
                showHelp();
                continue;  // Skip to the next iteration of the loop.
            }

            // Handle and respond to the user's query.
            String response = handleQuery(input);
            System.out.println(response);  // Display the response.
        }

        scanner.close();  // Close the scanner to prevent resource leaks.
    }

    // Displays a list of commands the user can ask the chatbot.
    private void showHelp() {
        System.out.println("Here are some things you can ask me:");
        System.out.println("- property assessment for ... (e.g., 'What is the property assessment for property 123?')");
        System.out.println("- filter by ...");
        System.out.println("- mean assessment value");
        System.out.println("- median assessment value");
        System.out.println("- total number of properties");
        System.out.println("- list all neighbourhoods (e.g., 'List all neighbourhoods')");
        System.out.println("- neighbourhood details for NEIGHBOURHOOD NAME");
        System.out.println("- location for a property (e.g., 'What is the location for property 123?')");
        System.out.println("- assessment class for a property (e.g., 'What is the assessment class for property 123?')");
        System.out.println("- exit to leave the application");
    }

    // Handles different types of queries the user may ask.
    private String handleQuery(String input) {
        String lowerInput = input.toLowerCase();  // Convert input to lowercase for case-insensitive matching.

        // Check if the query contains 'property assessment for' and extract property ID to get the assessment.
        if (lowerInput.contains("property assessment for")) {
            String propertyId = input.substring(lowerInput.lastIndexOf("for") + 4).trim();
            return propertyAssessments.getPropertyAssessment(propertyId);
        }

        // Check if the query contains 'filter by' and extract the filter to return filtered property data.
        if (lowerInput.contains("filter by")) {
            String filter = input.substring(lowerInput.indexOf("filter by") + 9).trim();
            PropertyAssessments filteredData = propertyAssessments.getFilteredData(filter);
            return (filteredData != null && filteredData.getSize() > 0) ? filteredData.toString() : "No properties found for the given filter.";
        }

        // Return the mean assessment value if the user asks for it.
        if (lowerInput.contains("mean assessment value")) {
            return "The mean assessed value is: $" + propertyAssessments.getMean();
        }

        // Return the median assessment value if the user asks for it.
        if (lowerInput.contains("median assessment value")) {
            return "The median assessed value is: $" + propertyAssessments.getMedian();
        }

        // Return the total number of properties if the user asks for it.
        if (lowerInput.contains("number of properties")) {
            return "Total number of properties: " + propertyAssessments.getSize();
        }

        // Return a list of all neighbourhoods if the user asks for them.
        if (lowerInput.contains("list all neighbourhoods")) {
            return "Neighbourhoods: " + propertyAssessments.getAllNeighbourhoods().toString();
        }

        // Return details of a specific neighbourhood if the user asks for it.
        if (lowerInput.contains("neighbourhood details for")) {
            String neighbourhoodName = input.substring(lowerInput.indexOf("for") + 4).trim();
            for (Neighbourhood n : propertyAssessments.getAllNeighbourhoods()) {
                if (n.getNeighbourhoodName().equalsIgnoreCase(neighbourhoodName)) {
                    return n.toString();
                }
            }
            return "No neighbourhood found with that name.";
        }

        // Return the location for a specific property if the user provides a property ID.
        if (lowerInput.contains("location for property")) {
            String propertyId = input.substring(lowerInput.lastIndexOf("property") + 8).trim();
            for (PropertyAssessment p : propertyAssessments.getData()) {
                if (String.valueOf(p.getId()).equals(propertyId)) {
                    return p.getLocation().toString();
                }
            }
            return "No property found with that ID.";
        }

        // Return the assessment class for a specific property if the user provides a property ID.
        if (lowerInput.contains("assessment class for property")) {
            String propertyId = input.substring(lowerInput.lastIndexOf("property") + 8).trim();
            for (PropertyAssessment p : propertyAssessments.getData()) {
                if (String.valueOf(p.getId()).equals(propertyId)) {
                    return p.getAssessmentClass().toString();
                }
            }
            return "No property found with that ID.";
        }

        // If the query doesn't match any of the above patterns, return an error message.
        return "I'm sorry, I don't understand that question.";
    }

    // Main method to initialize and start the chatbot.
    public static void main(String[] args) {
        try {
            // Create a new ChatBot instance and start the chatbot.
            ChatBot chatBot = new ChatBot("src/main/resources/Property_Assessment_Data_2024.csv");
            chatBot.start();
        } catch (IOException e) {
            // Handle any IO exceptions that occur while loading property data.
            System.err.println("Error loading property assessment data: " + e.getMessage());
        }
    }
}