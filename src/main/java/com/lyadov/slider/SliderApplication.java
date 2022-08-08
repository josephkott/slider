package com.lyadov.slider;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Simple slideshow viewer application. It follows the MVC pattern.
 * JavaFX Scene (view) is set up using the FXML file; {@link com.lyadov.slider.model.SliderModel}
 * represents a model, it contains all the necessary properties which are bound by the
 * controller {@link com.lyadov.slider.controller.SliderController} to the view objects.
 *
 * @author Misha Lebedev (gloriouslair@gmail.com)
 */
public class SliderApplication extends Application {
    private static final String ICON = "/png/icon-app.png";

    @Override
    public void start(Stage stage) throws IOException {
        URL fxmlURL = Objects.requireNonNull(getClass().getResource("/fxml/main.fxml"));
        Parent root = FXMLLoader.load(fxmlURL);
        Image icon = new Image(Objects.requireNonNull(
                SliderApplication.class.getResourceAsStream(ICON)));

        stage.setScene(new Scene(root));
        stage.getIcons().add(icon);
        stage.setTitle("Slideshow Viewer");
        stage.setWidth(600);
        stage.setHeight(400);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
