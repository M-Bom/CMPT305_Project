package ca.macewan.cmpt305.groupproject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;


public class PropertyAssessmentsApplication extends Application {

    TableView table;
    ObservableList<PropertyAssessment> data;

    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 1280, 960);

        String csvFileName = "src/main/resources/Property_Assessment_Data_2024.csv";
        PropertyAssessments propertyAssessments = new PropertyAssessments(csvFileName);
        PropertyAssessments residentialFilteredPropertyAssessments = propertyAssessments.getFilteredData("Residential");

        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10,10,10,10));

        Scene scene = new Scene(bp, 1800, 960);//new Scene(fxmlLoader.load(), win_width, win_height);
        stage.setTitle("Map search");

        VBox vb1 = new VBox();

        vb1 = setVB1(vb1, residentialFilteredPropertyAssessments);

        bp.setLeft(vb1);

        StackPane stackPane = Map.createMap();
        bp.setCenter(stackPane);

        onSearch(vb1, residentialFilteredPropertyAssessments);

        onReset(vb1, residentialFilteredPropertyAssessments);

        stage.setTitle("Map Search");
        stage.setScene(scene);
        stage.show();
    }

    VBox setVB1(VBox vb, PropertyAssessments propertyAssessments) throws IOException {
        List<Neighbourhood> neighbourhoods = propertyAssessments.getAllNeighbourhoods();
        List<Address> addresses = propertyAssessments.getAllAddresses();

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

    void onSearch(VBox vb, PropertyAssessments propertyAssessments) {
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
                        table.getItems().clear();
                        table.getItems().addAll(filteredPropertyAssessments.getData());
                    } else if (filter1 != null) {
                        PropertyAssessments filteredPropertyAssessments1 = propertyAssessments.getFilteredData(filter1);
                        PropertyAssessments filteredPropertyAssessments2 = filteredPropertyAssessments1.getFilteredData(filter2);
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