module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires com.google.zxing;
    requires com.google.zxing.javase;
//    requires mysql.connector.j;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}