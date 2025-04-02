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


    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 1280, 960);

        String csvFileName1 = "src/main/resources/Property_Assessment_Data_2024.csv";
        PropertyAssessments propertyAssessments = new PropertyAssessments(csvFileName1);
        PropertyAssessments residentialFilteredPropertyAssessments = propertyAssessments.getFilteredData("Residential");

        //String csvFileName2 = "src/main/resources/Edmonton_Public_School_Board.csv";
        //Schools schools = new Schools(csvFileName2);

        String csvFileName3 = "Edmonton_Neighbourhoods.csv";
        NeighbourhoodCatchments neighbourhoodCatchments = new NeighbourhoodCatchments(csvFileName3);

        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10,10,10,10));

        Scene scene = new Scene(bp, 1800, 960);//new Scene(fxmlLoader.load(), win_width, win_height);
        stage.setTitle("Map search");

        VBox vb1 = new VBox();

        vb1 = setVB1(vb1, residentialFilteredPropertyAssessments);

        bp.setLeft(vb1);

        //set map in right side of application
        StackPane stackPane = createMap();
        bp.setCenter(stackPane);
        //create address search within map
        //setupTextField();
        //stackPane.getChildren().add(searchBox);
        //show schools button
        Button schoolButton = new Button("School");
        StackPane.setMargin(schoolButton, new Insets(0, 0, 0, 10) );
        schoolButton.setStyle("-fx-background-color: #649aef; -fx-background-size: 20px 40px");
        stackPane.getChildren().add(schoolButton);
        stackPane.setAlignment(schoolButton, Pos.TOP_LEFT);
        createLocatorTask();
        schoolButton.setOnAction(event -> {
            try {
                schoolButtonUsage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        onSearch(vb1, residentialFilteredPropertyAssessments, neighbourhoodCatchments);

        onReset(vb1, residentialFilteredPropertyAssessments);

        stage.setTitle("Map Search");
        stage.setScene(scene);
        stage.show();
    }

    VBox setVB1(VBox vb, PropertyAssessments propertyAssessments) throws IOException {
        List<Neighbourhood> neighbourhoods = propertyAssessments.getAllNeighbourhoods();
        //List<Address> addresses = propertyAssessments.getAllAddresses();

        //vb.setPrefWidth(700);

        table = new TableView();
        data = FXCollections.observableArrayList(propertyAssessments.getData());

        TableColumn<PropertyAssessment, String> neighbourhood = new TableColumn<>("Neighbourhood");
        neighbourhood.setMinWidth(250);
        neighbourhood.setCellValueFactory(new PropertyValueFactory<>("neighbourhood"));

        TableColumn<PropertyAssessment, String> address = new TableColumn<>("Address");
        address.setMinWidth(250);
        address.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<PropertyAssessment, String> value = new TableColumn<>("Assessed value");
        value.setMinWidth(250);
        value.setCellValueFactory(new PropertyValueFactory<>("assessmentClass"));

        vb.setVgrow(table, Priority.ALWAYS);

        table.getColumns().setAll(neighbourhood, address, value);
        table.setItems(data);

        table.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                int index = table.getSelectionModel().getSelectedIndex();
                PropertyAssessment property = (PropertyAssessment) table.getItems().get(index);

            }
        });

        // Choice box of all possible neighbourhoods with wards
        ChoiceBox<Neighbourhood> neighbourhoodFilter = new ChoiceBox<>();
        neighbourhoodFilter.getItems().addAll(neighbourhoods);

        // Choice box for price range filter
        ChoiceBox<String> priceFilter = new ChoiceBox<>();
        priceFilter.getItems().addAll("0 - 99,999","100,000 - 499,999", "500,000 - 999,999", "1,000,000+");

        //create search and reset button
        Button search = new Button("Search");
        Button reset = new Button("Reset");
        HBox hb = new HBox(10);

        hb.getChildren().addAll(neighbourhoodFilter, priceFilter, search, reset);

        vb.getChildren().addAll(hb, table);

        return vb;
    }

    void onSearch(VBox vb, PropertyAssessments propertyAssessments, NeighbourhoodCatchments neighbourhoodCatchments) {
        // retrieve neighbourhoodFilter from vertical box
        ChoiceBox<Neighbourhood> neighbourhoodFilter = (ChoiceBox<Neighbourhood>) ((HBox) vb.getChildren().getFirst()).getChildren().get(0);

        // retrieve priceFilter from vertical box
        ChoiceBox<String> priceFilter = (ChoiceBox<String>) ((HBox) vb.getChildren().getFirst()).getChildren().get(1);

        // retrieve search button from vertical box
        Button search = (Button) ((HBox) vb.getChildren().getFirst()).getChildren().get(2);

        // Event handler for search button on click
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Neighbourhood neighbourhood = neighbourhoodFilter.getValue();
                if (neighbourhood != null) {
                    String filter1 = neighbourhood.getNeighbourhoodName();
                    System.out.println(filter1);
                    String filter2 = priceFilter.getValue();
                    System.out.println(filter2);
                    if (filter1 != null && filter2 == null) {
                        PropertyAssessments filteredPropertyAssessments = propertyAssessments.getFilteredData(filter1);
                        NeighbourhoodCatchment neighbourhoodCatchment = neighbourhoodCatchments.getNeighbourHoodCatchmentByName(filter1);
                        Catchment neighbourhoodPolygon = neighbourhoodCatchment.getCatchment();
                        System.out.println(neighbourhoodPolygon);
                        table.getItems().clear();
                        table.getItems().addAll(filteredPropertyAssessments.getData());
                    } else if (filter1 != null) {
                        PropertyAssessments filteredPropertyAssessments1 = propertyAssessments.getFilteredData(filter1);
                        PropertyAssessments filteredPropertyAssessments2 = filteredPropertyAssessments1.getFilteredData(filter2);
                        NeighbourhoodCatchment neighbourhoodCatchment = neighbourhoodCatchments.getNeighbourHoodCatchmentByName(filter1);
                        Catchment neighbourhoodPolygon = neighbourhoodCatchment.getCatchment();
                        System.out.println(neighbourhoodPolygon);
                        table.getItems().clear();
                        table.getItems().addAll(filteredPropertyAssessments2.getData());
                    } else if (filter2 != null) {
                        PropertyAssessments filteredPropertyAssessments1 = propertyAssessments.getFilteredData(filter2);
                        table.getItems().clear();
                        table.getItems().addAll(filteredPropertyAssessments1.getData());
                    }
                } else {
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

//map functions start

    public StackPane createMap() throws FileNotFoundException {
        StackPane stackPane = new StackPane();
        //arcgis key
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/ArcGIS_ApiKey"))) {
            String key;
            key = reader.readLine();
            ArcGISRuntimeEnvironment.setApiKey((String) key);

            //ArcGISRuntimeEnvironment.setApiKey("AAPT85fOqywZsicJupSmVSCGrmw17cs_rcl0BcRxYYPXDQIEDYKLWF6hpVqlEGU8ImN9k2U8H9E-GP50Hk8Q4xYd5EPPiCoN9Z9giRHe-DOTplphIwMlkuJfMeVxZTInrxQRUk3mlrjVDRUPu8-Crn875qcQfF_SWo7P4kM1QsthL_F_-VXzLHmgczeyGhjaMlYdTS9rcSZvxTasQN3hLX8H9inAOXaOdMhZxCdxMeYSgpI.AT2_s86nj1rq");
            mapView = new MapView();
            stackPane.getChildren().add(mapView);
            //set to topographic map for basemap to be able to see plain map
            ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_TOPOGRAPHIC);
            mapView.setMap(map);
            //set viewpoint to edmonton coordinates
            mapView.setViewpoint(new Viewpoint(53.5461, -113.4937, 360000));
            graphicsOverlay = new GraphicsOverlay();
            mapView.getGraphicsOverlays().add(graphicsOverlay);
            graphicsOverlay.getGraphics().clear();
            return stackPane;

        }catch (IOException e) {
            throw new RuntimeException("Invalid ArcGIS key", e);
        }
    }

    // no longer using text box search
        private void setupTextField() {
        searchBox = new TextField();
        searchBox.setMaxWidth(300);
        searchBox.setPromptText("Search");
        StackPane.setAlignment(searchBox, Pos.TOP_LEFT);
        StackPane.setMargin(searchBox, new Insets(10, 0,0, 10));
        createLocatorTask();
        searchBox.setOnAction(event -> {
            String text = searchBox.getText();
            if(!text.isEmpty()){
                //graphicsOverlay.getGraphics().clear(); // clears the overlay of any previous result
                //performGeocode(text, "address");
            }
        });
    }

    private void schoolButtonUsage() throws IOException {
        System.out.println(" in School Button usage");
        Schools schoolsInstance = new Schools("Edmonton_Public_School_Board.csv");
        String schools = schoolsInstance.getAllCoordinates();
        //String schools = "-113.434494524 53.5397222576, -113.501975651 53.519775373";
        getPointPlacement(schools);
    }

    public void createLocatorTask() {
        locatorTask = new LocatorTask("https://geocode-api.arcgis.com/arcgis/rest/services/World/GeocodeServer");
        geocodeParameters = new GeocodeParameters();
        geocodeParameters.getResultAttributeNames().add("*");
        geocodeParameters.setMaxResults(10);
        geocodeParameters.setOutputSpatialReference(mapView.getSpatialReference());
    }

    // this is not working
    private void performGeocode(String address, String type) {
        if(address.equalsIgnoreCase("Edmonton")){
            //getEdmontonBounds();
            return;
        }
        address = address + " Edmonton";
        ListenableFuture<List<GeocodeResult>> geocodeResults = locatorTask.geocodeAsync(address, geocodeParameters);
        geocodeResults.addDoneListener(() ->{
            try{
                List<GeocodeResult> geocodes = geocodeResults.get();
                if (geocodes.size() > 0) {
                    GeocodeResult result = geocodes.get(0);
                    //graphicsOverlay.getGraphics().clear(); // clears the overlay of any previous result
                    displayResult(result, type);
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "No results found.").show();
                }
            } catch (InterruptedException | ExecutionException e) {
                new Alert(Alert.AlertType.ERROR, "Error getting result.").show();
                e.printStackTrace();
            }
        });
    }

    //this is not working
    private void displayResult(GeocodeResult geocodeResult, String type) {
        Color c = null;
        if (type.equals("address")) {
            c = Color.RED;
        } else if (type.equals("park")) {
            c = Color.GREEN;
        } else if (type.equals("school")) {
            c = Color.BLUE;
        }

        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, c, 6.0f);
        Graphic markerGraphic = new Graphic(geocodeResult.getDisplayLocation(), geocodeResult.getAttributes(), markerSymbol);
        graphicsOverlay.getGraphics().add(markerGraphic);

        if(type.equals("address")){
            mapView.setViewpointCenterAsync(geocodeResult.getDisplayLocation());
        }
    }

    private void getEdmontonBounds(String multiPolygon) {
        //List<List<String>> edmontonCoord = new ArrayList<>();
//        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/City_of_Edmonton_-_Corporate_Boundary__current__20250325.csv"))) {
//            String line;
//            //skip header
//            reader.readLine();
//            line = reader.readLine();
//            if (line == null) {
//                throw new RuntimeException("CSV file does not contain Edmonton boundary");
//            }
//            String edmontonBoundary = line.split(",")[0];
//            if (!edmontonBoundary.startsWith("\"MULTIPOLYGON")){
//                throw new RuntimeException("Invalid format, CSV file does not contain Edmonton boundary");
//            }
            SimpleLineSymbol simpleLineSymbol1 = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLACK, 2);
            PointCollection pointCollection = new PointCollection(SpatialReference.create(4326));

            //pattern matcher to separate the longitude and latitude from the multipolygon string
            Pattern pattern = Pattern.compile("(-?[0-9]+\\.[0-9]+)\\s([0-9]+\\.[0-9]+)");
            Matcher matcher = pattern.matcher(multiPolygon);
            // Find and parse the coordinates
            while (matcher.find()) {
                String longitude = matcher.group(1); // x-coordinate
                String latitude = matcher.group(2); // y-coordinate
                pointCollection.add(new Point(Double.parseDouble(longitude), Double.parseDouble(latitude)));
                //System.out.println(longitude);
            }

            SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.web("#32A4A8", .8), simpleLineSymbol1);
            Polygon polygon = new Polygon(pointCollection);
            Graphic polygonGraphic = new Graphic(polygon, simpleFillSymbol);
            graphicsOverlay.getGraphics().add(polygonGraphic);


//        } catch (IOException e) {
//            throw new RuntimeException("Error reading Edmonton boundary", e);
//        }
    }

    private void getPointPlacement(String locations) {
        System.out.println("in point placement " + locations);
        SimpleMarkerSymbol markerSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 6);

        Pattern pattern = Pattern.compile("(-?[0-9]+\\.[0-9]+)\\s([0-9]+\\.[0-9]+)");
        Matcher matcher = pattern.matcher(locations);

        while (matcher.find()) {
            String longitude = matcher.group(1);
            String latitude = matcher.group(2);
            Point point = new Point(Double.parseDouble(longitude), Double.parseDouble(latitude), SpatialReference.create(4326));
            Graphic markerGraphic = new Graphic(point, markerSymbol);
            graphicsOverlay.getGraphics().add(markerGraphic);
        }

    }

//map functions end


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
            }
        };
        reset.setOnAction(event);
    }

    public static void main(String[] args) {
        Application.launch();
    }
}