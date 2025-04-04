package ca.macewan.cmpt305.groupproject;
import java.awt.*;
import javafx.scene.layout.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


public class Schools {
    private List<School> schools;
    private String filePath;


    /**
     * Constructor for Schools object that takes a list of School Objects
     * @param schools - list of School Objects
     * */
    public Schools(List<School> schools) {this.schools = schools;}

    /**
     * Constructor a Schools object by getting data from an existing School objects.
     *
     * @param csvFileName Existing School objects.
     */
    public Schools(String csvFileName) throws IOException {
        this.schools = new ArrayList<>();
        this.filePath = "src/main/resources/" + csvFileName;
        // Here I will catch if we can load the file so the program doesn't crash
        try {
            loadFromCSV(filePath); // try to load the file
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Reads the contents of the CSV and creates School objects
     * @param csvFileName - full path to the CSV file
     * @throws IOException - if there's a read error
     */
    public void loadFromCSV(String csvFileName) throws IOException {
        String[][] data = readData(csvFileName);

        // Throw exception if the file is empty
        if (data.length == 0){
            throw new IOException("Empty CSV file");
        }
        for (String[] row : data) {
            // Ensure every row has exactly 20 columns by filling missing values
            row = Arrays.copyOf(row, 20);
            for (int i = 0; i < row.length; i++) {
                if (row[i] == null || row[i].trim().equals("<null>")) {
                    row[i] = ""; // Replace <null> or missing values with empty string
                }
            }

            String id = row[4];
            String year = row[0];
            SchoolName name = new SchoolName(row[5], row[2]);
            SchoolType schoolType = new SchoolType(row[6], row[7]);
            Address address = new Address(row[8], "", "");
            Location location = new Location(row[14], row[15]);
            String catchmentPolygon = row[19];
            Catchment catchment = new Catchment(catchmentPolygon);

            // Make a School object with all the gathered data
            School school = new School(id, year, name, schoolType, address,catchment, location);
            // Add the School object to Schools
            schools.add(school);

        }
    }

    /**
     * Read the contents of a CSV file and return data as a 2D array of String.
     * @param csvFileName - the CSV file name
     * @return data - the values in the CSV file
     * @throws IOException - input/output error
     */
    public static String[][] readData(String csvFileName) throws IOException {

        // Create a stream to read the CSV file
        String[][] data;
        int index = 0;

        // Here we will catch is if we can read the file
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(csvFileName))) {
            // Skip the header - this assumes the first line is a header
            reader.readLine();

            // Create 2D array to store all rows of data as String
            int initialSize = 50000; // Made bigger so resizing doesn't happen too many times cause time/memory overhead
            data = new String[initialSize][];

            // Read the file line by line and store all rows into a 2D array
            String line;
            while ((line = reader.readLine()) != null) {
                // Split a line by comma works for simple CSV files
                String[] values = line.split(",");

                // for multipolygons, if we end up using catchment then we got to switch
                // this however is very slow so maybe import opencsv or other method
                //String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);


                // Check if the array is full
                if (index == data.length)
                    // Array is full, create and copy all values to a larger array
                    data = Arrays.copyOf(data, data.length * 2);

                data[index++] = values;
            }
        }

        // If the file is empty or only contains headers throw exception
        if (index == 0) {
            throw new IOException("Error reading CSV file: " + csvFileName);
        }

        return Arrays.copyOf(data, index);
    }

    public int getSize(){
        return schools.size(); // return size of list
    }

    /**
     * This method will return a School object by given ID
     * @param id - string
     * @return school - School Object
     */
    public School getSchoolByID(String id){
        for(School school : schools){
            if(school.getId().equals(id)){
                return school;
            }
        }
        return null;
    }

    /**
     * This method will return a Sting of coordinates for all School objects in Schools
     * @return coordinates - String containing all coordinates in Schools Objects
     * */
    public String getAllCoordinates() {
        //ArrayList<String> coordinates = new ArrayList<>();
        String coordinates = "";
        // loop through each School in Schools
        for (School school : schools) {
            // get the latitude and longitude using methods from School and Location
            String latitude = school.getLocation().getLatitude();
            String longitude = school.getLocation().getLongitude();
            // if not null
            if (latitude != null && longitude != null && !latitude.isEmpty() && !longitude.isEmpty()) {
                //coordinates.add("(" + latitude + " " + longitude + ")");

                // Add coordinates to list following this format
                coordinates += latitude + " " + longitude + ", ";
            }
        }
        // DEBUG to check if coordinates are being returned
        //System.out.println(coordinates);
        return coordinates;
    }

    /**
     * This method will get all School object types in Schools
     * @return types - list of school types
     * */
    public List<String> getAllSchoolTypes() {
        // Initialize an empty list for the target neighbourhood
        List<String> types = new ArrayList<>();
        // Loop through each School in Schools
        for (School school : schools) {
            // SchoolType is an object that's in School that contains type and grades
            // If type is not null and not in list types then add to list
            if (school.getSchoolType() != null
                    && school.getSchoolType().getType() != null &&
                    !types.contains(school.getSchoolType().getType())) {
                types.add(school.getSchoolType().getType());
            }
        }
        return types;
    }


    /**
     * This method will get School objects by a specfic type
     * @param partialType - string with partial type letters
     * @return Schools - Schools object with that type
     * */
    public Schools getSchoolsByType(String partialType) {
        // Initialize an empty list for the School objects
        List<School> result = new ArrayList<>();
        // Loop through each School in Schools
        for (School school : schools) {
            // if the SchoolType is not null and type is not null
            // then add school to list
            if (school.getSchoolType() != null &&
                    school.getSchoolType().getType() != null &&
                    school.getSchoolType().getType().toLowerCase().contains(partialType.toLowerCase())) {
                result.add(school);
            }
        }
        // Create new Schools object with the list of School objects
        return new Schools(result);
    }

    /**
     * This method will get School objects by a specfic grade
     * @param grade - string for grades
     * @return Schools - Schools object with that grade
     * */
    public Schools getSchoolsByGrade(String grade) {
        // Initialize an empty list for the School objects
        List<School> result = new ArrayList<>();
        // Loop through each School in Schools
        for (School school : schools) {
            // if the SchoolType is not null and grade is not null
            // then add school to list
            if (school.getSchoolType() != null &&
                    school.getSchoolType().getGrades() != null &&
                    school.getSchoolType().getGrades().toLowerCase().contains(grade.toLowerCase())) {
                result.add(school);
            }
        }
        // Create new Schools object with the list of School objects
        return new Schools(result);
    }

    /**
     * This method will return Schools object containing all Elementary School objects
     * @return Schools - All elementary school objects
     * */
    public Schools getElementarySchools() {
        // In the dataset anything that has an E is elementary [EL, EJ, EJS]
        return getSchoolsByType("E");
    }

    /**
     * This method will return Schools object containing all Junior High School objects
     * @return Schools - All junior high school objects
     * */
    public Schools getJuniorSchools() {
        // In the dataset anything that has a J is Junior high [JR, EJ, EJS]
        return getSchoolsByType("J");
    }

    /**
     * This method will return Schools object containing all Senior High School objects
     * @return Schools - All senior high school objects
     * */
    public Schools getSeniorSchools() {
        // Initialize an empty list for the School objects
        List<School> result = new ArrayList<>();
        // Loop through each School in Schools
        for (School school : schools) {
            // get type for each school
            String type = school.getSchoolType().getType().toUpperCase();
            // if type has an S and does not have a P then add to list
            if (type.contains("S") && !type.contains("P")) {
                // in the data set all senior highs have an S but there is another type SP
                // which aren't general senior high schools there special programs with ranging grades
                result.add(school);
            }
        }
        // Return Schools object with Senior high School objects
        return new Schools(result);
    }

    /**
     * toSting method for Schools
     * */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (School school : schools) {
            sb.append(school.getName())
                    .append(" - ")
                    .append(school.getAddress())
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }

}
