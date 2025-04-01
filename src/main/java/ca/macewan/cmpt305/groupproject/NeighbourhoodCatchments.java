package ca.macewan.cmpt305.groupproject;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NeighbourhoodCatchments {
    private List<NeighbourhoodCatchment> neighbourhoods;
    private String filePath;

    public NeighbourhoodCatchments(List<NeighbourhoodCatchment> neighbourhoods){this.neighbourhoods = neighbourhoods;}

    // Constructor that initialize Schools list from a file
    // Constructor for CSV loading
    public NeighbourhoodCatchments(String csvFileName) throws IOException {
        this.neighbourhoods = new ArrayList<>();
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
            row = Arrays.copyOf(row, 9);
            for (int i = 0; i < row.length; i++) {
                if (row[i] == null || row[i].trim().equals("<null>")) {
                    row[i] = ""; // Replace <null> or missing values with empty string
                }
            }

            Neighbourhood neighbourhood = new Neighbourhood(row[6],row[2],row[1]);
            String catchmentPolygon = row[8];
            Catchment catchment = new Catchment(catchmentPolygon);

            neighbourhoods.add(new NeighbourhoodCatchment(neighbourhood,catchment));

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
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);


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

    public NeighbourhoodCatchment getNeighbourHoodCatchmentByName(String name){
        for(NeighbourhoodCatchment catchment : neighbourhoods){
            if(catchment.getNeighbourhood().getNeighbourhoodName().trim().equalsIgnoreCase(name.trim())){
                return catchment;
            }
        }
        return null;
    }
}
