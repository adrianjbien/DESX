module com.example.desx {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.codec;


    opens com.example.desx to javafx.fxml;
    exports com.example.desx;
}