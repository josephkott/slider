module com.lyadov.slider {
    requires javafx.fxml;
    requires javafx.controls;

    opens com.lyadov.slider.controller to javafx.fxml;
    exports com.lyadov.slider;
    exports com.lyadov.slider.model;
}