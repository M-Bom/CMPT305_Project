module ca.macewan.cmpt305.groupproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens ca.macewan.cmpt305.groupproject to javafx.fxml;
    exports ca.macewan.cmpt305.groupproject;
}