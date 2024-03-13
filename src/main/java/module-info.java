module uk.ac.soton.comp2211.runwayredeclaration {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires java.desktop;
    requires java.sql;


    opens uk.ac.soton.comp2211.runwayredeclaration to javafx.fxml;
    exports uk.ac.soton.comp2211.runwayredeclaration;
}