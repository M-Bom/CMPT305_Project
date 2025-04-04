package ca.macewan.cmpt305.groupproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parks {
    private List<Park> parks;
    private String filePath;

    /**
     * Constructor for Parks object that takes a list of Park Objects
     * @param parks - list of Park Objects
     * */
    public Parks(List<Park> parks) {
        this.parks = parks;
    }

    /**
     * Constructor a Parks object by getting data from an existing Park objects.
     *
     * @param csvFileName Existing Park objects.
     */
    public Parks(String csvFileName) throws IOException {
        this.parks = new ArrayList<>();
        this.filePath = "src/main/resources/" + csvFileName;
        // Here I will catch if we can load the file so the program doesn't crash
        try {
            loadFromCSV(filePath); // try to load the file
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Reads the contents of the CSV and creates Park objects
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
            row = Arrays.copyOf(row, 11);
            for (int i = 0; i < row.length; i++) {
                if (row[i] == null || row[i].trim().equals("<null>")) {
                    row[i] = ""; // Replace <null> or missing values with empty string
                }
            }

            String id = row[0];
            String officialName = row[1];
            String commonName = row[2];
            Address address = new Address(row[6], null, null);
            Location location = new Location(row[8], row[9]);

            Park park = new Park(id, officialName, commonName, address, location);
            parks.add(park);

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

                //for multipolygons, if we end up using catchment then we got to switch to this
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

    /**
     * This method will return a Sting of coordinates for all Park objects in Parks
     * @return coordinates - String containing all coordinates in Schools Objects
     */
    public String getAllCoordinates() {
        //ArrayList<String> coordinates = new ArrayList<>();
        String coordinates = "";
        // Loop through each Park in Parks
        for (Park park : parks) {
            // get the latitude and longitude for that Park
            String latitude = park.getLocation().getLatitude();
            String longitude = park.getLocation().getLongitude();
            // If not null add them to the coordinates string
            if (latitude != null && longitude != null && !latitude.isEmpty() && !longitude.isEmpty()) {
                //coordinates.add("(" + latitude + " " + longitude + ")");
                coordinates += latitude + " " + longitude + ",";
            }
        }

        //System.out.println(coordinates);
        // long string of coordinates for each Park in Parks
        return coordinates;
    }

}
