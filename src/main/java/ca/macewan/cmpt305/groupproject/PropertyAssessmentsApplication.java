package ca.macewan.cmpt305.groupproject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;

//imports for map
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.tasks.geocode.GeocodeParameters;
import com.esri.arcgisruntime.tasks.geocode.GeocodeResult;
import com.esri.arcgisruntime.tasks.geocode.LocatorTask;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyAssessmentsApplication extends Application {

    TableView table;
    ObservableList<PropertyAssessment> data;

    //for map
    private static TextField searchBox;
    private static MapView mapView;
    private static GeocodeParameters geocodeParameters;
    private static GraphicsOverlay graphicsOverlay;
    private static LocatorTask locatorTask;
    private ChatBot chatBot;
    private TextArea chatArea;
    private TextField inputField;
    private boolean isElemSchoolVisible = false;
    private boolean isJrSchoolVisible = false;
    private boolean isSrSchoolVisible = false;
    private boolean isParkVisible = false;

    @Override
    public void start(Stage stage) throws IOException {
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

        // Create the property assessments object using the csv file
        String csvFileName1 = "src/main/resources/Property_Assessment_Data_2024.csv";
        PropertyAssessments propertyAssessments = new PropertyAssessments(csvFileName1);
        // Filter the object by Residential assessment type
        PropertyAssessments residentialFilteredPropertyAssessments = propertyAssessments.getFilteredData("Residential");

        // Create the neighbourhood catchments object using the Edmonton_neighbourhoods csv file
        String csvFileName3 = "Edmonton_Neighbourhoods.csv";
        NeighbourhoodCatchments neighbourhoodCatchments = new NeighbourhoodCatchments(csvFileName3);

        // Create a BorderPane
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10,10,10,10));

        // Create new scene using the border pane and set the size to 1600x700
        Scene scene = new Scene(bp, 1600, 700);
        stage.setTitle("Map search");

        // Create a new VBox
        VBox vb1 = new VBox();

        // Call the setVB1 method to set up the display
        vb1 = setVB1(vb1, residentialFilteredPropertyAssessments);

        // Set the location of the VBox in the border pane
        bp.setLeft(vb1);

        // ChatBot UI on the right
        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPrefHeight(400);
        chatArea.setPrefWidth(300);

        inputField = new TextField();
        inputField.setPromptText("Type your message...");
        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> handleUserInput());

        VBox chatLayout = new VBox(10, chatArea, inputField, sendButton);
        chatLayout.setPadding(new Insets(10));
        chatLayout.setStyle("-fx-border-color: gray; -fx-border-width: 1px;");

        // **Add chat layout to the right side of the BorderPane**
        bp.setRight(chatLayout);

        //set map in right side of application
        StackPane stackPane = createMap();
        bp.setCenter(stackPane);

        //create buttons within map feature
        createButtons(stackPane);

        // Call the onSearch method and to search the data using the filters when pressing the button
        onSearch(vb1, residentialFilteredPropertyAssessments, neighbourhoodCatchments);

        // Call onReset method to reset the filters back to the original state
        onReset(vb1, residentialFilteredPropertyAssessments);

        // set stage name/title and display scene
        stage.setTitle("Map Search");
        stage.setScene(scene);
        stage.show();
    }

    VBox setVB1(VBox vb, PropertyAssessments propertyAssessments) throws IOException {
        // Create a list of neighbourhoods in alphabetical order using the property assessments class
        List<Neighbourhood> neighbourhoods = propertyAssessments.getNeighbourhoodsInAlphabeticalOrder(propertyAssessments.getAllNeighbourhoods());

        // create a new table view and set the data stored in the table view to be the propertyAssessments class data
        table = new TableView();
        data = FXCollections.observableArrayList(propertyAssessments.getData());

        // Set the first column in the table to be the neighbourhood
        TableColumn<PropertyAssessment, String> neighbourhood = new TableColumn<>("Neighbourhood");
        neighbourhood.setMinWidth(250);
        neighbourhood.setCellValueFactory(new PropertyValueFactory<>("neighbourhood"));

        // Set the second column in the table to be the address
        TableColumn<PropertyAssessment, String> address = new TableColumn<>("Address");
        address.setMinWidth(250);
        address.setCellValueFactory(new PropertyValueFactory<>("address"));

        // Set the third column in the table to be the assessed value
        TableColumn<PropertyAssessment, String> value = new TableColumn<>("Assessed value");
        value.setMinWidth(250);
        value.setCellValueFactory(new PropertyValueFactory<>("assessmentClass"));

        // Set the priority for the growth of the table
        vb.setVgrow(table, Priority.ALWAYS);

        // Set the columns of the table to the values set above (neighbourhood, address, value)
        // and add the data to the table
        table.getColumns().setAll(neighbourhood, address, value);
        table.setItems(data);

        // Allow for the table to be clicked
        table.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                int index = table.getSelectionModel().getSelectedIndex();
                // When selected obtain the property from the table
                PropertyAssessment property = (PropertyAssessment) table.getItems().get(index);
                // Get the location of the property and then create the points on the map using the
                // longitude and latitude coordinates
                Location propertyLocation = property.getLocation();
                String longitude = propertyLocation.getLongitude();
                String latitude = propertyLocation.getLatitude();
                String location = String.format("%s %s,", longitude, latitude);
                System.out.println(location);
                getPointPlacement(location, "address");
                // Obtain the properties ID number and display it in the chatbot textbox
                chatArea.appendText("Property ID: "+property.getId()+"\n");
            }
        });

        // Choice box of all possible neighbourhoods with wards
        ChoiceBox<Neighbourhood> neighbourhoodFilter = new ChoiceBox<>();
        neighbourhoodFilter.getItems().addAll(neighbourhoods);

        // Choice box for price range filter
        ChoiceBox<String> priceFilter = new ChoiceBox<>();
        priceFilter.getItems().addAll("0 - 99,999","100,000 - 499,999", "500,000 - 999,999", "1,000,000+");

        // Create search and reset button
        Button search = new Button("Search");
        Button reset = new Button("Reset");
        HBox hb = new HBox(10);

        // Add the filters search and reset button to a HBox
        hb.getChildren().addAll(neighbourhoodFilter, priceFilter, search, reset);

        // Display the HBox and table
        vb.getChildren().addAll(hb, table);

        return vb;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    void onSearch(VBox vb, PropertyAssessments propertyAssessments, NeighbourhoodCatchments neighbourhoodCatchments) {
        // Retrieve neighbourhoodFilter from vertical box
        ChoiceBox<Neighbourhood> neighbourhoodFilter = (ChoiceBox<Neighbourhood>) ((HBox) vb.getChildren().getFirst()).getChildren().get(0);

        // Retrieve priceFilter from vertical box
        ChoiceBox<String> priceFilter = (ChoiceBox<String>) ((HBox) vb.getChildren().getFirst()).getChildren().get(1);

        // Retrieve search button from vertical box
        Button search = (Button) ((HBox) vb.getChildren().getFirst()).getChildren().get(2);

        // Event handler for search button on click
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // Obtain the neighbourhood value from the filter
                Neighbourhood neighbourhood = neighbourhoodFilter.getValue();
                // If the filter is selected get the neighbourhood name and price filter
                if (neighbourhood != null) {
                    String filter1 = neighbourhood.getNeighbourhoodName();
                    System.out.println(filter1);
                    String filter2 = priceFilter.getValue();
                    System.out.println(filter2);

                    // If the neighbourhood filter is selected and the price filter is not active
                    if (filter1 != null && filter2 == null) {
                        // Filter the property assessment data by the filter display on the table and obtain
                        // the Multipolygon value for the neighbourhood and display it on the map
                        applyFilters(filter1, neighbourhoodCatchments.getNeighbourHoodCatchmentByName(filter1), propertyAssessments, neighbourhoodCatchments);

                        // If both the neighbourhood and price filter are active
                    } else if (filter1 != null) {
                        // Filter the property assessment data by the filters display on the table and obtain
                        // the Multipolygon value for the neighbourhood and display it on the map
                        PropertyAssessments filteredPropertyAssessments1 = propertyAssessments.getFilteredData(filter1);
                        applyFilters(filter2, neighbourhoodCatchments.getNeighbourHoodCatchmentByName(filter1), filteredPropertyAssessments1, neighbourhoodCatchments);

                        // If the price filter is changed and the neighbourhood filter is active
                    } else if (filter2 != null) {
                        // Filter the property assessment data by the filter display on the table and obtain
                        // the Multipolygon value for the neighbourhood and display it on the map
                        PropertyAssessments filteredPropertyAssessments1 = propertyAssessments.getFilteredData(filter2);
                        System.out.println(filteredPropertyAssessments1);
                        table.getItems().clear();
                        table.getItems().addAll(filteredPropertyAssessments1.getData());
                    }
                // In the case where the neighbourhood filter is left blank and the price filter is selected
                } else {
                    // Filter the property assessment data by the filter display on the table
                    String filter2 = priceFilter.getValue();
                    System.out.println(filter2);
                    PropertyAssessments filteredPropertyAssessments1 = propertyAssessments.getFilteredData(filter2);
                    table.getItems().clear();
                    table.getItems().addAll(filteredPropertyAssessments1.getData());
                }
            }
        };
        search.setOnAction(event);
    }

    private void applyFilters(String filter, NeighbourhoodCatchment neighbourHoodCatchmentByName, PropertyAssessments propertyAssessments, NeighbourhoodCatchments neighbourhoodCatchments) {
        try{
        // Filter the property assessment data by the filter display on the table and obtain
        // the Multipolygon value for the neighbourhood and display it on the map

        PropertyAssessments filteredPropertyAssessments = propertyAssessments.getFilteredData(filter);
        NeighbourhoodCatchment neighbourhoodCatchment = neighbourHoodCatchmentByName;
        if (neighbourhoodCatchment == null) {
            throw new NullPointerException("Neighbourhood catchment is null for: " + filter);
        }

        Catchment neighbourhoodPolygon = neighbourhoodCatchment.getCatchment();
        if (neighbourhoodPolygon == null) {
            throw new NullPointerException("Polygon is null for: " + filter);
        }

        System.out.println(neighbourhoodPolygon);
        getEdmontonBounds(String.valueOf(neighbourhoodPolygon));
        table.getItems().clear();
        table.getItems().addAll(filteredPropertyAssessments.getData());
        }
        catch(Exception e){
            System.err.println("This is our catch and error msg: Error filtering and drawing polygon for neighbourhood: " + filter);
            showError("This neighbourhood does not have an outline for the map");
        }
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
            System.exit(0);
        }
        if (userInput.equalsIgnoreCase("help")) {
            chatArea.appendText("ChatBot: Here are some things you can ask me:\n"
                    + "- property assessment for [ID]\n"
                    + "- filter by [criteria]\n"
                    + "- mean assessment value\n"
                    + "- median assessment value\n"
                    + "- total number of properties\n"
                    + "- list all neighbourhoods\n"
                    + "- neighbourhood details for [NAME]\n"
                    + "- location for property [ID]\n"
                    + "- assessment class for property [ID]\n"
                    + "- total number of schools\n"
                    + "- school details for [ID]\n"
                    + "- exit to leave the application\n");
            inputField.clear();
            return;
        }

        String response = chatBot.handleQuery(userInput);
        chatArea.appendText("ChatBot: " + response + "\n");
        inputField.clear();
    }

    public StackPane createMap() throws FileNotFoundException {
        StackPane stackPane = new StackPane();
        //collect arcgis key from external text file
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/ArcGIS_ApiKey"))) {
            String key;
            key = reader.readLine();
            ArcGISRuntimeEnvironment.setApiKey((String) key);

            //create new map view within map stackPane with a topographic basemap
            mapView = new MapView();
            stackPane.getChildren().add(mapView);
            ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_TOPOGRAPHIC);
            mapView.setMap(map);

            //set viewpoint to edmonton coordinates
            mapView.setViewpoint(new Viewpoint(53.5461, -113.4937, 360000));

            //create new alterable graphics overlay to map for points to be added
            graphicsOverlay = new GraphicsOverlay();
            mapView.getGraphicsOverlays().add(graphicsOverlay);
            graphicsOverlay.getGraphics().clear();
            return stackPane;

        }catch (IOException e) {
            throw new RuntimeException("Invalid ArcGIS key", e);
        }
    }

    private void createButtons(StackPane stackPane){
        //button to show all Elementary schools in Edmonton
        Button schoolButton = new Button("Elementary School");
        schoolButton.setStyle("-fx-background-color: #649aef; -fx-background-size: 20px 40px");

        // add button to the map stackPane in the top left slightly right of margin between map and table
        stackPane.getChildren().add(schoolButton);
        StackPane.setMargin(schoolButton, new Insets(0, 0, 0, 10) );
        stackPane.setAlignment(schoolButton, Pos.TOP_LEFT);

        //when Elementary School button pressed by user
        schoolButton.setOnAction(event -> {
            if(!isElemSchoolVisible){
                try {
                    elemSchoolButtonUsage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                isElemSchoolVisible = true;
            }
            else {
                // Remove elementary school graphics
                graphicsOverlay.getGraphics().removeIf(graphic -> "elem".equals(graphic.getAttributes().get("type")));
                isElemSchoolVisible = false;
            }
        });

        //button for showing all High Schools in Edmonton
        Button highSchoolButton = new Button("High School");
        highSchoolButton.setStyle("-fx-background-color: #c571f6; -fx-background-size: 20px 50px");

        // add button to the map stackPane in the top left slightly right of margin between map and table
        stackPane.getChildren().add(highSchoolButton);
        StackPane.setMargin(highSchoolButton, new Insets(60, 0, 0, 10) );
        stackPane.setAlignment(highSchoolButton, Pos.TOP_LEFT);

        //when High School button is pressed by user
        highSchoolButton.setOnAction(event -> {
            if (!isSrSchoolVisible)
            {
                try {
                    highSchoolButtonusage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                isSrSchoolVisible = true;
            }
            else {
                // Remove High School graphics
                graphicsOverlay.getGraphics().removeIf(graphic -> "high".equals(graphic.getAttributes().get("type")));
                isSrSchoolVisible = false;
            }
        });

        //button for showing all Jr. High Schools in Edmonton
        Button jrSchoolButton = new Button("Jr. High School");
        jrSchoolButton.setStyle("-fx-background-color: #f3914d; -fx-background-size: 20px 40px");

        // add button to the map stackPane in the top left slightly right of margin between map and table
        stackPane.getChildren().add(jrSchoolButton);
        StackPane.setMargin(jrSchoolButton, new Insets(30, 0, 0, 10) );
        stackPane.setAlignment(jrSchoolButton, Pos.TOP_LEFT);

        //when Jr. High button is pressed by user
        jrSchoolButton.setOnAction(event -> {
            if (!isJrSchoolVisible){
                try {
                    jrSchoolButtonUsage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                isJrSchoolVisible = true;
            }
            else {
                graphicsOverlay.getGraphics().removeIf(graphic -> "jr".equals(graphic.getAttributes().get("type")));
                isJrSchoolVisible = false;
            }
        });

        //button for showing all parks and playgrounds in Edmonton
        Button parksButton = new Button("parks");
        parksButton.setStyle("-fx-background-color: #7cf34d; -fx-background-size: 20px 40px");

        // add button to the map stackPane in the top left slightly right of margin between map and table
        stackPane.getChildren().add(parksButton);
        StackPane.setMargin(parksButton, new Insets(90, 0, 0, 10) );
        stackPane.setAlignment(parksButton, Pos.TOP_LEFT);

        //when parks button is pressed by user
        parksButton.setOnAction(event -> {
            if(!isParkVisible){

                    try {
                        parksButtonUsage();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                isParkVisible = true;
            }
            else {
                graphicsOverlay.getGraphics().removeIf(graphic -> "park".equals(graphic.getAttributes().get("type")));
                isParkVisible = false;
            }
        });
    }

    private void elemSchoolButtonUsage() throws IOException {
        //collect all points of relevant elementary schools in edmonton and send to be printed
        Schools schoolsInstance = new Schools("Edmonton_Public_School_Board.csv");
        String schools = schoolsInstance.getElementarySchools().getAllCoordinates();
        getPointPlacement(schools, "elem");
    }

    private void highSchoolButtonusage() throws IOException {
        //collect all points of relevant high schools in edmonton and send to be printed
        Schools schoolsInstance = new Schools("Edmonton_Public_School_Board.csv");
        String schools = schoolsInstance.getSeniorSchools().getAllCoordinates(); //
        getPointPlacement(schools, "high");
    }

    private void jrSchoolButtonUsage() throws IOException {
        //collect all points of relevant jr. high schools in edmonton and send to be printed
        Schools schoolsInstance = new Schools("Edmonton_Public_School_Board.csv");
        String schools = schoolsInstance.getJuniorSchools().getAllCoordinates();
        getPointPlacement(schools, "jr");
    }

    private void parksButtonUsage() throws IOException {
        //collect all points of relevant parks in edmonton and send to be printed
        Parks parksInstance = new Parks("Parks_20250326.csv");
        String parks = parksInstance.getAllCoordinates();
        getPointPlacement(parks, "park");
    }

    private void getEdmontonBounds(String multiPolygon) {
        //set style for outline and fill colour of multipolygon
        SimpleLineSymbol simpleLineSymbol1 = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLACK, 2);
        SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.web("#32A4A8", .5), simpleLineSymbol1);

        //point collection for all added points within multipolygon string
        PointCollection pointCollection = new PointCollection(SpatialReference.create(4326));

        //pattern matcher to separate the longitude and latitude from the multipolygon string
        Pattern pattern = Pattern.compile("(-?[0-9]+\\.[0-9]+)\\s([0-9]+\\.[0-9]+)");
        Matcher matcher = pattern.matcher(multiPolygon);

        //take seperated longitude and latitude as a new point and add to the point collection
        while (matcher.find()) {
            String longitude = matcher.group(1);
            String latitude = matcher.group(2);
            pointCollection.add(new Point(Double.parseDouble(longitude), Double.parseDouble(latitude)));
        }

        //create polygon from collected point collection and send it to the map graphics overlay
        Polygon polygon = new Polygon(pointCollection);
        Graphic polygonGraphic = new Graphic(polygon, simpleFillSymbol);
        try {
            graphicsOverlay.getGraphics().add(polygonGraphic);
        } catch (Exception ex) {
            System.err.println("Failed to add polygon graphic to map: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void getPointPlacement(String locations, String type) {
        //set color for differentiation of point type
        Color c = null;
        if (type.equals("address")) {
            c = Color.RED;
        } else if (type.equals("elem")) {
            c = Color.BLUE;
        } else if (type.equals("high")) {
            c = Color.MEDIUMPURPLE;
        }else if (type.equals("jr")) {
            c = Color.ORANGE;
        }else if (type.equals("park")) {
            c = Color.GREEN;
        }

        //setting style for marker drawing of singular point
        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, c, 5);

        //pattern matcher to separate latitude and longitude from a string of independent points
        Pattern pattern = Pattern.compile("(-?[0-9]+\\.[0-9]+)\\s([0-9]+\\.[0-9]+)");
        Matcher matcher = pattern.matcher(locations);

        //take separate latitude and longitude points and send to graphics overlay for each point independently
        while (matcher.find()) {
            String longitude = matcher.group(1);
            String latitude = matcher.group(2);
            Point point = new Point(Double.parseDouble(longitude), Double.parseDouble(latitude), SpatialReference.create(4326));
            Graphic markerGraphic = new Graphic(point, markerSymbol);
            markerGraphic.getAttributes().put("type", type); // <-- Tagging
            graphicsOverlay.getGraphics().add(markerGraphic);
        }
    }

    void onReset(VBox vb, PropertyAssessments propertyAssessments) {
        // retrieve neighbourhoodFilter from vertical box
        ChoiceBox<Neighbourhood> neighbourhoodFilter = (ChoiceBox<Neighbourhood>) ((HBox) vb.getChildren().getFirst()).getChildren().get(0);

        // retrieve priceFilter from vertical box
        ChoiceBox<String> priceFilter = (ChoiceBox<String>) ((HBox) vb.getChildren().getFirst()).getChildren().get(1);

        // retrieve search button from vertical box
        Button reset = (Button) ((HBox) vb.getChildren().getFirst()).getChildren().get(3);

        // Event handler for search button on click
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                table.getItems().clear();
                table.setItems(FXCollections.observableArrayList(propertyAssessments.getData()));
                neighbourhoodFilter.getSelectionModel().clearSelection();
                priceFilter.getSelectionModel().clearSelection();
                graphicsOverlay.getGraphics().clear(); // clears the map graphics overlay of any previous result

            }
        };
        reset.setOnAction(event);
    }

    public static void main(String[] args) {
        Application.launch();
    }
}