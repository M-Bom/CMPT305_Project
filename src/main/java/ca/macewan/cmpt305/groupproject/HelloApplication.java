package ca.macewan.cmpt305.groupproject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {

    TableView table;
    ObservableList<PropertyAssessment> data;

    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 1280, 960);
        // Choice box of all possible neighbourhoods with wards
        /*ChoiceBox<Neighbourhood> neighbourhoodFilter = new ChoiceBox<>();
        neighbourhoodFilter.getItems().addAll(neighbourhoods);*/
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10,10,10,10));

        Scene scene = new Scene(bp, 1800, 960);//new Scene(fxmlLoader.load(), win_width, win_height);
        stage.setTitle("Employee Example");

        VBox vb1 = new VBox();

        vb1 = setVB1(vb1);

        bp.setLeft(vb1);


        //VBox layout = new VBox(10);
        //layout.setPadding(new Insets(20,20,20,20));
        //layout.getChildren().addAll(neighbourhoodFilter);



        stage.setTitle("Map Search");
        stage.setScene(scene);
        stage.show();
    }

    VBox setVB1(VBox vb) throws IOException {
        String csvFileName = "src/main/resources/Property_Assessment_Data_2024.csv";
        PropertyAssessments propertyAssessments = new PropertyAssessments(csvFileName);
        //List<Neighbourhood> neighbourhoods = propertyAssessments.getAllNeighbourhoods();

        vb.setPrefWidth(700);

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

        vb.getChildren().add(table);

        return vb;
    }

    public static void main(String[] args) {
        launch();
    }
}