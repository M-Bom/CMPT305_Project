package ca.macewan.cmpt305.groupproject;

import java.util.Scanner;
import java.io.IOException;

public class ChatBot {
    private PropertyAssessments propertyAssessments;
    private Schools schools;

    // Constructor initializes property assessments and schools from CSV files
    public ChatBot(String propertyCsvFileName, String schoolCsvFileName) throws IOException {
        this.propertyAssessments = new PropertyAssessments(propertyCsvFileName);
        this.schools = new Schools(schoolCsvFileName);
    }

    /**
     * this method gives available commands to the chatbot which is used to handle inputs from the
     * user. This allows the user to interact with the chatbot and get information back from it
     * @param input
     * @return a String
     */
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

        return "I'm sorry, I don't understand that question.";
    }
}