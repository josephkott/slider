package com.lyadov.slider.controller;

import com.lyadov.slider.model.Position;
import com.lyadov.slider.model.SliderModel;
import com.lyadov.slider.view.IconLoader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ResourceBundle;

public class SliderController implements Initializable {
    private SliderModel model;

    @FXML private AnchorPane pane;
    @FXML private AnchorPane imagePane;
    @FXML private ImageView imageView;
    @FXML private Label imageNameLabel;
    @FXML private Button restartButton;
    @FXML private Button playPauseButton;
    @FXML private Button selectDirectoryButton;
    @FXML private TextField selectedDirectoryText;
    @FXML private Label positionLabel;

    @FXML
    public void paneKeyPressedEventHandler(KeyEvent event) {
        switch (event.getCode()) {
            case ESCAPE -> ((Stage) pane.getScene().getWindow()).close();
            case SHIFT -> {
                model.pause();
                Position position = model.getPositionProperty().get();
                if (position.getCurrent() < position.getTotal()) {
                    model.loadNextImage();
                }
            }
        }
    }

    @FXML
    public void playPauseButtonActionEventHandler() {
        model.playPauseToggle();
    }

    @FXML
    public void restartButtonActionEventHandler() {
        model.restart();
    }

    @FXML
    public void setFolderButtonActionEventHandler() {
        model.stop();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(Paths.get(System.getProperty("user.dir")).toFile());

        File directory = directoryChooser.showDialog(pane.getScene().getWindow());
        if (directory != null) {
             model.setImageDirectory(directory);
        }
    }

    /**
     * Provide all necessary bindings between the view objects and the model properties.
     */
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        /* Start up with default image directory for the purpose of POC */
        URL defaultDirectoryUrl = Objects.requireNonNull(getClass().getResource("/jpeg/Bruegel"));
        model = new SliderModel(new File(defaultDirectoryUrl.getFile()));

        initializeButtons();
        initializeImageView();
        initializeTextFields();
        initializeSlideshowTimeline();
    }

    private void initializeButtons() {
        ImageView playIconView = IconLoader.getIconView(IconLoader.PLAY_ICON, playPauseButton);
        ImageView pauseIconView = IconLoader.getIconView(IconLoader.PAUSE_ICON, playPauseButton);
        ImageView restartIconView = IconLoader.getIconView(IconLoader.RESTART_ICON, restartButton);
        ImageView folderIconView = IconLoader.getIconView(IconLoader.FOLDER_ICON, selectDirectoryButton);

        ObjectBinding<ImageView> playPauseImageBinding = Bindings.createObjectBinding(
                () -> switch (model.getStatusProperty().get()) {
                    case RUNNING -> pauseIconView;
                    case PAUSED, STOPPED -> playIconView;
                },
                model.getStatusProperty()
        );

        BooleanBinding disableControlsBinding = Bindings.createBooleanBinding(
                () -> model.getPositionProperty().get().getTotal() == 0,
                model.getPositionProperty()
        );

        playPauseButton.graphicProperty().bind(playPauseImageBinding);
        playPauseButton.disableProperty().bind(disableControlsBinding);
        restartButton.setGraphic(restartIconView);
        restartButton.disableProperty().bind(disableControlsBinding);
        selectDirectoryButton.setGraphic(folderIconView);
    }

    /**
     * Adjusts the image view to display the image in the center of the screen.
     */
    private void initializeImageView() {
        imageView.imageProperty().bind(model.getCurrentImageProperty());
        imageView.fitWidthProperty().bind(imagePane.widthProperty());
        imageView.fitHeightProperty().bind(imagePane.heightProperty());

        DoubleBinding xPropertyBinding = Bindings.createDoubleBinding(
                () -> getImageViewXPosition(imageView),
                imageView.fitWidthProperty(),
                imageView.fitHeightProperty(),
                model.getCurrentImageProperty()
        );

        DoubleBinding yPropertyBinding = Bindings.createDoubleBinding(
                () -> getImageViewYPosition(imageView),
                imageView.fitWidthProperty(),
                imageView.fitHeightProperty(),
                model.getCurrentImageProperty()
        );

        imageView.xProperty().bind(xPropertyBinding);
        imageView.yProperty().bind(yPropertyBinding);
    }

    private double getImageViewScaleFactor(ImageView imageView) {
        return Math.min(
                imageView.getFitWidth() / imageView.getImage().getWidth(),
                imageView.getFitHeight() / imageView.getImage().getHeight()
        );
    }

    private double getImageViewXPosition(ImageView imageView) {
        double scaleFactor = getImageViewScaleFactor(imageView);
        return (imageView.getFitWidth() - imageView.getImage().getWidth() * scaleFactor) / 2;
    }

    private double getImageViewYPosition(ImageView imageView) {
        double scaleFactor = getImageViewScaleFactor(imageView);
        return (imageView.getFitHeight() - imageView.getImage().getHeight() * scaleFactor) / 2;
    }

    private void initializeTextFields() {
        selectedDirectoryText.textProperty().bind(model.getImageDirectoryNameProperty());
        imageNameLabel.textProperty().bind(model.getCurrentImageNameProperty());

        StringBinding positionTextPropertyBinding = Bindings.createStringBinding(
                () -> model.getPositionProperty().get().toString(),
                model.getPositionProperty()
        );

        positionLabel.textProperty().bind(positionTextPropertyBinding);
    }

    private void initializeSlideshowTimeline() {
        KeyFrame switchImage = new KeyFrame(SliderModel.DELAY, event -> model.loadNextImage());
        Timeline timeline = new Timeline(switchImage);

        IntegerBinding cycleCountPropertyBinding = Bindings.createIntegerBinding(
                () -> model.getPositionProperty().get().getTotal() - 1,
                model.getPositionProperty()
        );

        timeline.cycleCountProperty().bind(cycleCountPropertyBinding);
        timeline.setOnFinished(event -> model.stop());

        model.getStatusProperty().addListener((observable, oldStatus, newStatus) -> {
            switch (newStatus) {
                case STOPPED -> timeline.stop();
                case RUNNING -> timeline.play();
                case PAUSED -> timeline.pause();
            }
        });
    }
}
