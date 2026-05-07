module d.demo6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires jdk.compiler;


    opens d.demo6 to javafx.fxml;
    exports d.demo6;
}