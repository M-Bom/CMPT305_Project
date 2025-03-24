package ca.macewan.cmpt305.groupproject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Class that reads a CSV file and prints out the
 * info required for the lab.
 */
public class Lab2Main {
    /**
     * Main method that reads property assessment data from a CSV file, displays general
     * statistics, and allows the user to search for specific property information based
     * on account number or neighbourhood.
     *
     * @param args Command-line arguments (not used in this program).
     * @throws IOException If an error occurs while reading the CSV file.
     */
    public static void main(String[] args) throws IOException {
        // File name of the CSV file
        // "src/main/resources/Property_Assessment_Data_2024.csv"
        Scanner scanner = new Scanner(System.in);
        System.out.print("CSV filename: ");
        String filename = scanner.next();
        String csvFileName = "src/main/resources/" + filename;

        // Checks if the file name exists
        if (!Files.exists(Paths.get(csvFileName))) {
            System.out.println("Error in opening " + filename);
            return;
        }

        // Creates propertyAssessments value using PropertyAssessments class
        PropertyAssessments propertyAssessments = new PropertyAssessments(csvFileName);

        System.out.println("Descriptive statistics of all property assessments");
        System.out.print(propertyAssessments);

        // Asks the user for a property account number
        System.out.print("Find a property assessment class by account number: ");
        String idNumber = scanner.next();

        System.out.println(propertyAssessments.getPropertyAssessment(idNumber));

        // Asks user to input a neighbourhood
        System.out.print("Neighbourhood: ");
        String neighbourhoodName = scanner.next();

        // Creates a new PropertyAssessment value using the filter
        PropertyAssessments neighbourhoodPropertyAssessments = propertyAssessments.getFilteredData(neighbourhoodName);
        if (neighbourhoodPropertyAssessments == null) {
            System.out.println("Neighbourhood is not found");
        } else {
            System.out.println("Statistics (neighbourhood = " + neighbourhoodName + ")");
            System.out.print(neighbourhoodPropertyAssessments);
        }
    }
}