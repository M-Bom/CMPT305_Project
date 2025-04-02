package ca.macewan.cmpt305.groupproject;

import java.util.Scanner;
import java.io.IOException;

public class ChatBot {
    private PropertyAssessments propertyAssessments;
    private Schools schools;
    private NeighbourhoodCatchments neighbourhoodCatchments;

    // Constructor initializes property assessments and schools from CSV files
    public ChatBot(String propertyCsvFileName, String schoolCsvFileName, String neighCsvFile) throws IOException {
        this.propertyAssessments = new PropertyAssessments(propertyCsvFileName);
        this.schools = new Schools(schoolCsvFileName);
        this.neighbourhoodCatchments = new NeighbourhoodCatchments(neighCsvFile);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the ChatBot! Type 'exit' to quit or 'help' to see available commands.");

        while (true) {
            System.out.print("Ask a question: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            }
            if (input.equalsIgnoreCase("help")) {
                showHelp();
                continue;
            }

            String response = handleQuery(input);
            System.out.println(response);
        }
        scanner.close();
    }

    public void showHelp() {
        System.out.println("Here are some things you can ask me:");
        System.out.println("- property assessment for [ID]");
        System.out.println("- filter by [criteria]");
        System.out.println("- mean assessment value");
        System.out.println("- median assessment value");
        System.out.println("- total number of properties");
        System.out.println("- list all neighbourhoods");
        System.out.println("- neighbourhood details for [NAME]");
        System.out.println("- location for property [ID]");
        System.out.println("- assessment class for property [ID]");
        System.out.println("- total number of schools");
        System.out.println("- school details for [ID]");
        System.out.println("- exit to leave the application");
    }

    public String handleQuery(String input) {
        String lowerInput = input.toLowerCase();

        if (lowerInput.contains("property assessment for")) {
            String propertyId = input.substring(lowerInput.lastIndexOf("for") + 4).trim();
            return propertyAssessments.getPropertyAssessment(propertyId);
        }
        if (lowerInput.contains("filter by")) {
            String filter = input.substring(lowerInput.indexOf("filter by") + 9).trim();
            PropertyAssessments filteredData = propertyAssessments.getFilteredData(filter);
            return (filteredData != null && filteredData.getSize() > 0) ? filteredData.toString() : "No properties found for the given filter.";
        }
        if (lowerInput.contains("mean assessment value")) {
            return "The mean assessed value is: $" + propertyAssessments.getMean();
        }
        if (lowerInput.contains("median assessment value")) {
            return "The median assessed value is: $" + propertyAssessments.getMedian();
        }
        if (lowerInput.contains("number of properties")) {
            return "Total number of properties: " + propertyAssessments.getSize();
        }
        if (lowerInput.contains("list all neighbourhoods")) {
            return "Neighbourhoods: " + propertyAssessments.getAllNeighbourhoods().toString();
        }
        if (lowerInput.contains("neighbourhood details for")) {
            String neighbourhoodName = input.substring(lowerInput.indexOf("for") + 4).trim();
            for (Neighbourhood n : propertyAssessments.getAllNeighbourhoods()) {
                if (n.getNeighbourhoodName().equalsIgnoreCase(neighbourhoodName)) {
                    return n.toString();
                }
            }
            return "No neighbourhood found with that name.";
        }
        if (lowerInput.contains("location for property")) {
            String propertyId = input.substring(lowerInput.lastIndexOf("property") + 8).trim();
            for (PropertyAssessment p : propertyAssessments.getData()) {
                if (String.valueOf(p.getId()).equals(propertyId)) {
                    return p.getLocation().toString();
                }
            }
            return "No property found with that ID.";
        }
        if (lowerInput.contains("assessment class for property")) {
            String propertyId = input.substring(lowerInput.lastIndexOf("property") + 8).trim();
            for (PropertyAssessment p : propertyAssessments.getData()) {
                if (String.valueOf(p.getId()).equals(propertyId)) {
                    return p.getAssessmentClass().toString();
                }
            }
            return "No property found with that ID.";
        }

        // School Queries
        if (lowerInput.contains("total number of schools")) {
            return "Total number of schools: " + schools.getSize();
        }
        if (lowerInput.contains("school details for")) {
            String schoolId = input.substring(lowerInput.indexOf("for") + 4).trim();
            School school = schools.getSchoolByID(schoolId);

            return (school != null) ? school.toString() : "No school found with that ID.";
        }
        if (lowerInput.contains("Neighbourhood details")) {

        }

        return "I'm sorry, I don't understand that question.";
    }

    public static void main(String[] args) {
        try {
            ChatBot chatBot = new ChatBot(
                    "src/main/resources/Property_Assessment_Data_2024.csv",
                    "Edmonton_Public_School_Board.csv",
                    "Edmonton_Neighbourhoods.csv"
            );
            chatBot.start();
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
}