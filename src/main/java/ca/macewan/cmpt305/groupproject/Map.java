package ca.macewan.cmpt305.groupproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.esri.arcgisruntime.geometry.*;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Map extends Application {
    private static MapView mapView;
    private static GeocodeParameters geocodeParameters;
    private static GraphicsOverlay graphicsOverlay;
    private static LocatorTask locatorTask;
    private static TextField searchBox;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        //set size
        stage.setTitle("Map of Edmonton");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();

        StackPane stackPane = createMap();
        Scene scene = new Scene(stackPane, 800, 600);
        //Scene scene = createMap(); //new Scene(stackPane, 800, 600);
        stage.setScene(scene);

        //arcgis key
//        ArcGISRuntimeEnvironment.setApiKey("AAPTxy8BH1VEsoebNVZXo8HurLGkiLZYE4xEJO1UZcClXcJCvJFDBTEnVkdydmje5VEOivd1rG98ST-5NOgSDb4ULR80I2k4qyh-Ju3VXLPL1alR0PgRQxs_GcQL1XOv7mxv92GLE6LdX9MSqZpo60rrroLJfjqlxqWVrBoWspVzYHHwbQBkzKUfPniuInAGUuoCMWHB4fmQwz8BwJuJbNjmw3ivpFzpb_MqSQcnC4S5Vpg.AT1_s86nj1rq");
//
//        mapView = new MapView();
//        stackPane.getChildren().add(mapView);
//        //set to topographic map for basemap to be able to see plain map
//        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_TOPOGRAPHIC);
//        mapView.setMap(map);
//        //set viewpoint to edmonton coordinates
//        mapView.setViewpoint(new Viewpoint(53.5461, -113.4937, 350000));
//
//        graphicsOverlay = new GraphicsOverlay();
//
//        mapView.getGraphicsOverlays().add(graphicsOverlay);
//        graphicsOverlay.getGraphics().clear(); // clears the overlay of any previous result

//        setupTextField();
//
//        stackPane.getChildren().add(searchBox);
//        StackPane.setAlignment(searchBox, Pos.TOP_LEFT);
//        StackPane.setMargin(searchBox, new Insets(10, 0,0, 10));
//        createLocatorTask();
//
//        searchBox.setOnAction(event -> {
//            String text = searchBox.getText();
//            if(!text.isEmpty()){
//                //graphicsOverlay.getGraphics().clear(); // clears the overlay of any previous result
//                performGeocode(text, "address");
//            }
//        });
//
//        //show schools button
//        Button schoolButton = new Button("School");
//        schoolButton.setStyle("-fx-background-color: #3f83e8; -fx-background-size: 20px 40px");
//        stackPane.getChildren().add(schoolButton);
//        StackPane.setAlignment(schoolButton, Pos.TOP_RIGHT);
//        createLocatorTask();
//        schoolButton.setOnAction(event -> {
//            try {
//                displayGroup("data/Edmonton_Public_School_Board__EPSB__School_Locations_20250326.csv", 5, "school");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        //show parks button
//        Button parkButton = new Button("Park");
//        parkButton.setStyle("-fx-background-color: #49df1a; -fx-background-size: 20px 40px");
//        stackPane.getChildren().add(parkButton);
//        stackPane.setAlignment(parkButton,Pos.CENTER_RIGHT);
//        createLocatorTask();
//        parkButton.setOnAction(event -> {
//            try {
//                displayGroup("data/Playgrounds_20250326.csv", 2, "park");
//            } catch (IOException e) {
//                throw new RuntimeException("in button", e);
//            }
//        });
//
//        //clear button
//        Button clearButton = new Button("Clear");
//        clearButton.setStyle("-fx-background-color: #ffffff; -fx-background-size: 20px 40px");
//        stackPane.getChildren().add(clearButton);
//        stackPane.setAlignment(clearButton, Pos.TOP_CENTER);
//        createLocatorTask();
//        clearButton.setOnAction(event -> {
//            graphicsOverlay.getGraphics().clear(); // clears the overlay of any previous result
//        });
    }

    @Override
    public void stop() {
        if (mapView != null) {
            mapView.setViewpoint(null);
        }
    }

    public static StackPane createMap(){
        StackPane stackPane = new StackPane();
        //arcgis key
        ArcGISRuntimeEnvironment.setApiKey("AAPTxy8BH1VEsoebNVZXo8HurLGkiLZYE4xEJO1UZcClXcJCvJFDBTEnVkdydmje5VEOivd1rG98ST-5NOgSDb4ULR80I2k4qyh-Ju3VXLPL1alR0PgRQxs_GcQL1XOv7mxv92GLE6LdX9MSqZpo60rrroLJfjqlxqWVrBoWspVzYHHwbQBkzKUfPniuInAGUuoCMWHB4fmQwz8BwJuJbNjmw3ivpFzpb_MqSQcnC4S5Vpg.AT1_s86nj1rq");

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

//        //address search
//        setupTextField();
//
//        stackPane.getChildren().add(searchBox);
//        StackPane.setAlignment(searchBox, Pos.TOP_LEFT);
//        StackPane.setMargin(searchBox, new Insets(10, 0,0, 10));
//        createLocatorTask();
//
//        searchBox.setOnAction(event -> {
//            String text = searchBox.getText();
//            if(!text.isEmpty()){
//                //graphicsOverlay.getGraphics().clear(); // clears the overlay of any previous result
//                performGeocode(text, "address");
//            }
//        });
//
//                //show schools button
//        Button schoolButton = new Button("School");
//        schoolButton.setStyle("-fx-background-color: #3f83e8; -fx-background-size: 20px 40px");
//        stackPane.getChildren().add(schoolButton);
//        StackPane.setAlignment(schoolButton, Pos.TOP_RIGHT);
//        createLocatorTask();
//        schoolButton.setOnAction(event -> {
//            try {
//                performGeocode(School.getAddress(), "Address");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }

        return stackPane;
    }

    private static void setupTextField() {
        searchBox = new TextField();
        searchBox.setMaxWidth(300);
        searchBox.setPromptText("Search");
    }

    private static void createLocatorTask() {
        locatorTask = new LocatorTask("https://geocode-api.arcgis.com/arcgis/rest/services/World/GeocodeServer");
        geocodeParameters = new GeocodeParameters();
        geocodeParameters.getResultAttributeNames().add("*");
        geocodeParameters.setMaxResults(10);
        geocodeParameters.setOutputSpatialReference(mapView.getSpatialReference());
    }

    private static void performGeocode(String address, String type) {
        if(address.equalsIgnoreCase("Edmonton")){
            getEdmontonBounds();
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

    private static void displayResult(GeocodeResult geocodeResult, String type) {
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

    private void displayGroup(String filename, int pos, String type) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            reader.readLine();

            for(line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] group = line.split(",");
                performGeocode(group[pos], type);
            }
        }catch (IOException e){
            throw new RuntimeException("in display group", e);
        }
    }

    private static void getEdmontonBounds() {
        List<List<String>> edmontonCoord = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/City_of_Edmonton_-_Corporate_Boundary__current__20250325.csv"))) {
            String line;
            //skip header
            reader.readLine();
            line = reader.readLine();
            if (line == null) {
                throw new RuntimeException("CSV file does not contain Edmonton boundary");
            }
            String edmontonBoundary = line.split(",")[0];
            if (!edmontonBoundary.startsWith("\"MULTIPOLYGON")){
                throw new RuntimeException("Invalid format, CSV file does not contain Edmonton boundary");
            }

            SimpleLineSymbol simpleLineSymbol1 = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLACK, 2);
            PointCollection pointCollection = new PointCollection(SpatialReference.create(4326));

            //pattern matcher to separate the longitude and latitude from the multipolygon string
            Pattern pattern = Pattern.compile("(-?[0-9]+\\.[0-9]+)\\s([0-9]+\\.[0-9]+)");
            Matcher matcher = pattern.matcher(line);
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


        } catch (IOException e) {
            throw new RuntimeException("Error reading Edmonton boundary", e);
        }
    }

}
