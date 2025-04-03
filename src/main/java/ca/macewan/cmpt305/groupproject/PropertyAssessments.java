package ca.macewan.cmpt305.groupproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class PropertyAssessments {
    private String csvFileName;
    private List<PropertyAssessment> data;
    private List<Integer> assessedValues;
    private List<Integer> sortedAssessedValues;
    private double meanValue;
    private int medianValue;
    private int size;

    /**
     * Constructs a PropertyAssessments object from a given CSV file.
     *
     * @param csvFileName The name of the CSV file containing property assessment data.
     * @throws IOException if there's an error reading the file.
     */
    public PropertyAssessments(String csvFileName) throws IOException {
        this.csvFileName = csvFileName;
        this.data = readData(this.csvFileName);
        this.assessedValues = getAllAssessedValues(this.data);
        this.sortedAssessedValues = getSortedAssessedValues(this.assessedValues);
        this.meanValue = getMean();
        this.medianValue = getMedian();
        this.size = getSize();
    }

    /**
     * Constructs a PropertyAssessments object by copying data from an existing PropertyAssessments object.
     *
     * @param data Existing PropertyAssessments object.
     */
    public PropertyAssessments(List<PropertyAssessment> data) {
        this.data = data;
        this.assessedValues = getAllAssessedValues(this.data);
        this.sortedAssessedValues = getSortedAssessedValues(this.assessedValues);
        this.meanValue = getMean();
        this.medianValue = getMedian();
        this.size = getSize();
    }

    /**
     * Retrieves a property assessment based on the property ID.
     *
     * @param propertyId The ID of the property.
     * @return Property details as a string or an error message.
     */
    public String getPropertyAssessment(String propertyId) {
        try {
            for (PropertyAssessment property : this.data) {
                if (property.getId() == (Integer.parseInt(propertyId))) {
                    return String.valueOf(property);
                }
            }
            return "Error: Invalid account number...\n";
            // Error check to catch if the propertyId is not a number
        } catch (NumberFormatException e) {
            return "Error: Invalid account number...\n";
        }
    }

    /**
     * Filters the property data based on a neighbourhood, assessment class, or garage presence.
     *
     * @param filterName Filter criterion.
     * @return Filtered PropertyAssessments object or null if no data matches.
     */
    public PropertyAssessments getFilteredData(String filterName) {
        // lambda expression that filters through the data and stores in a List of PropertyAssessments
        List<PropertyAssessment> filteredData = this.data.stream()
                .filter(property -> property.getNeighbourhood().getNeighbourhoodName().equals(filterName.toUpperCase()) ||
                        Arrays.asList(property.getAssessmentClass().getClassNames()).contains(filterName.toUpperCase()) ||
                        (property.getGarage().equals(filterName.toUpperCase())) || (withinPriceRange(property.getAssessmentClass().getAssessedValue(), filterName.toUpperCase())))
                .collect(Collectors.toList());
        // Checks if the list is empty and returns null
        if (filteredData.isEmpty()) {
            return null;
        }

        // Creating new property assessments value using the newly filtered data
        return new PropertyAssessments(filteredData);
    }

    private boolean withinPriceRange(int assessedValue, String filterName) {
        return switch (filterName) {
            case "0 - 99,999" -> assessedValue >= 0 && assessedValue <= 99999;
            case "100,000 - 499,999" -> assessedValue >= 100000 && assessedValue <= 499999;
            case "500,000 - 999,999" -> assessedValue >= 500000 && assessedValue <= 999999;
            case "1,000,000+" -> assessedValue >= 1000000;
            default -> false;
        };
    }

    /**
     * Reads property data from a CSV file and constructs PropertyAssessment objects.
     *
     * @param csvFileName Name of the CSV file.
     * @return List of PropertyAssessment objects.
     * @throws IOException if the file can't be read.
     */
    public List<PropertyAssessment> readData(String csvFileName) throws IOException {
        // Create a stream to read the CSV file
        List<PropertyAssessment> data = new ArrayList<>();
        int index = 0;
        int counter = 0;
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(csvFileName))) {
            // Skip the header - this assumes the first line is a header
            reader.readLine();

            // Read the file line by line and store all rows into a 2D array
            String line;
            String[] values = null;
            while ((line = reader.readLine()) != null) {
                // Split a line by comma works for simple CSV files
                values = line.split(",");
                // creates location and address values using the values read from the data
                Location location = new Location(values[9], values[10]);
                Address address = new Address(values[3], values[2], values[1]);
                // calls method getAssessmentClasses and getAssessmentPercent to create two arrays of Strings
                // containing the assessment classes (RESIDENTIAL, COMMERCIAL, etc.) and the percent (100, 75, etc.)
                // from the read dataset
                String[] assessmentClasses = getAssessmentClasses(values);
                String[] assessmentPercent = getAssessmentPercent(values);
                // creates assessmentClass value using the two above arrays and the assessed value from the read dataset
                AssessmentClass assessmentClass = new AssessmentClass(Integer.parseInt(values[8]), assessmentClasses, assessmentPercent);
                // creates neighbourhood value using the read dataset
                Neighbourhood neighbourhood = new Neighbourhood(values[7], values[6], values[5]);
                // creates a property value using the above values and the ID of the property form the read dataset
                PropertyAssessment property = new PropertyAssessment(Integer.parseInt(values[0]), location, address, assessmentClass, neighbourhood, values[4]);
                // added to the data
                data.add(property);
            }
        }
        return data;
    }

    /**
     * Gets the size of the Property Assessments object
     *
     * @return this.data.size - Size of the data
     */
    public int getSize() {
        return this.data.size();
    }

    /**
     * Gets the data stored in the Property Assessments object
     *
     * @return data - list of properties
     */
    public List<PropertyAssessment> getData() {
        return data;
    }

    /**
     * Save all the assessed values from the data to a list of integers.
     *
     * @param data - list of properties
     * @return assessedValues - list of int values
     */
    public List<Integer> getAllAssessedValues(List<PropertyAssessment> data) {
        // New list of int with the length of data (number of entries)
        List<Integer> assessedValues = new ArrayList<>();

        // Loop through data and save all assessed values to the
        // assessed value list if the value from the data is not null
        for (PropertyAssessment property : data) {
            assessedValues.add(Integer.valueOf(property.getAssessmentClass().getAssessedValue()));
        }
        // Return the assessedValues list
        return assessedValues;
    }

    public List<Neighbourhood> getAllNeighbourhoods() {
        Set<Neighbourhood> neighbourhoods = this.data.stream().map(PropertyAssessment::getNeighbourhood).collect(Collectors.toSet());
        //System.out.println(neighbourhoods.size());
        return new ArrayList<>(neighbourhoods);
    }

    public List<Neighbourhood> getNeighbourhoodsInAlphabeticalOrder(List<Neighbourhood> neighbourhoods) {
        List<Neighbourhood> sortedNeighbourhoods =  new ArrayList<>(neighbourhoods);
        sortedNeighbourhoods.sort(Comparator.comparing(Neighbourhood::getNeighbourhoodName));
        return sortedNeighbourhoods;
    }

    /**
     * From the account info obtains the three possible assessment classes
     *
     * @param accountInfo - row from the csv file
     * @return assessmentClasses - Array of Stings
     */
    public String[] getAssessmentClasses(String[] accountInfo) {
        // Create a new array of strings to save the property assessment class type
        String[] assessmentClasses = new String[3];

        // Go through data and check the assessment class type and add to the new
        // array if the entry is not null
        for (int i = 15; i < accountInfo.length; i++) {
            if (accountInfo[i] != null) {
                assessmentClasses[i - 15] = accountInfo[i];
            }
        }
        return assessmentClasses;
    }

    /**
     * From the account info obtains the three possible assessment class percents
     *
     * @param accountInfo - row from the csv file
     * @return assessmentPercent - Array of Stings
     */
    public String[] getAssessmentPercent(String[] accountInfo) {
        // Create a new array of strings to save the property assessment class type
        String[] assessmentPercent = new String[3];

        // Go through data and check the assessment class type and add to the new
        // array if the entry is not null
        for (int i = 12; i < 15; i++) {
            if (accountInfo[i] != null) {
                assessmentPercent[i - 12] = accountInfo[i];
            }
        }
        return assessmentPercent;
    }

    /**
     * Method used to obtain the maximum value from the
     * assessedValues list
     *
     * @return Collections.max(this.assessedValues) - int value (largest assessed value)
     */
    public int maxAssessedValue() {
        return Collections.max(this.assessedValues);
    }

    /**
     * Method used to obtain the minimum value from the
     * assessedValues list
     *
     * @return Collections.min(this.assessedValues) - int value (smallest assessed value)
     */
    public int minAssessedValue() {
        return Collections.min(this.assessedValues);
    }

    /**
     * Method used to obtain the range of assessed values
     *
     * @return maxAssessedValue() - minAssessedValue() - int value of the range (max - min)
     */
    public int getRange() {
        return maxAssessedValue() - minAssessedValue();
    }

    /**
     * Method used to sort the assessed values in order from smallest to largest
     *
     * @return new ArrayList<>(assessedValues) - list of int values
     */
    public List<Integer> getSortedAssessedValues(List<Integer> assessedValues) {
        Collections.sort(assessedValues);
        return new ArrayList<>(assessedValues);
    }

    /**
     * Method used to obtain the mean of the assessed values
     *
     * @return Math.ceil(sum / assessedValues.size()) - int value
     */
    public double getMean() {
        double sum = 0;
        // Add up every assessed value in the dataset
        for (int sortedAssessedValue : this.assessedValues) {
            sum += sortedAssessedValue;
        }
        return Math.ceil(sum / assessedValues.size());
    }

    /**
     * Method used to obtain the median of the assessed values
     *
     * @return median - int value
     */
    public int getMedian() {
        int median = 0;
        // Checks to see if the number of assessed values is odd or even to preform appropriate calculation
        if (sortedAssessedValues.size() % 2 == 0) {
            median = (sortedAssessedValues.get(sortedAssessedValues.size() / 2) + sortedAssessedValues.get(sortedAssessedValues.size() / 2 - 1)) / 2;
            return median;
        } else {
            median = sortedAssessedValues.get(sortedAssessedValues.size() / 2);
            return median;
        }
    }

    /**
     * Override toString Object method to obe able to print PropertyAssessments values
     *
     * @return formated string - contains number of entries, min, max, range, mean, and median values
     */
    @Override
    public String toString() {
        return String.format("n = %d \nmin = $%,d \nmax = $%,d \nrange = $%,d \nmean = $%,.0f \nmedian = $%,d \n%n", getSize(), minAssessedValue(), maxAssessedValue(), getRange(), this.meanValue, this.medianValue);
    }
}