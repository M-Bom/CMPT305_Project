module ca.macewan.cmpt305.groupproject {
    requires java.desktop;
    requires com.esri.arcgisruntime;
    requires javafx.fxml;


    opens ca.macewan.cmpt305.groupproject to javafx.fxml;
    exports ca.macewan.cmpt305.groupproject;
}