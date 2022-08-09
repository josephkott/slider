package com.lyadov.slider.model;

import javafx.animation.Animation.Status;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.util.Duration;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Slider application model. It uses {@link ImageProvider} to load next images asynchronously
 * in a background while showing the currently loaded image.
 */
public class SliderModel {
    public static final Duration DELAY = Duration.seconds(3);
    private static final List<String> FORMATS = List.of("JPG", "JPEG");
    private static final Image EMPTY_IMAGE = new WritableImage(16, 16);
    private static final String EMPTY_IMAGE_NAME = "<empty>";

    private File imageDirectory = null;
    private ImageProvider imageProvider = null;

    /* Model binding properties */
    private final StringProperty imageDirectoryNameProperty = new SimpleStringProperty("");
    private final StringProperty currentImageNameProperty = new SimpleStringProperty(EMPTY_IMAGE_NAME);
    private final ObjectProperty<Image> currentImageProperty = new SimpleObjectProperty<>(EMPTY_IMAGE);
    private final ObjectProperty<Position> positionProperty = new SimpleObjectProperty<>(Position.EMPTY);
    private final ObjectProperty<Status> statusProperty = new SimpleObjectProperty<>(Status.STOPPED);

    public SliderModel(File imageDirectory) {
        this.setImageDirectory(imageDirectory);
    }

    public void setImageDirectory(File imageDirectory) {
        this.imageDirectory = Objects.requireNonNull(imageDirectory);
        imageProvider = new ImageProvider(FORMATS, imageDirectory);

        imageDirectoryNameProperty.set(imageDirectory.getName());
        statusProperty.set(Status.STOPPED);
        positionProperty.set(imageProvider.getPosition());

        loadNextImage();
    }

    public ObjectProperty<Status> getStatusProperty() {
        return statusProperty;
    }

    public StringProperty getImageDirectoryNameProperty() {
        return imageDirectoryNameProperty;
    }

    public StringProperty getCurrentImageNameProperty() {
        return currentImageNameProperty;
    }

    public ObjectProperty<Image> getCurrentImageProperty() {
        return currentImageProperty;
    }

    public ObjectProperty<Position> getPositionProperty() {
        return positionProperty;
    }

    public void pause() {
        statusProperty.set(Status.PAUSED);
    }

    public void stop() {
        statusProperty.set(Status.STOPPED);
    }

    public void restart() {
        statusProperty.set(Status.STOPPED);
        imageProvider = new ImageProvider(FORMATS, imageDirectory);
        positionProperty.set(imageProvider.getPosition());

        loadNextImage();
    }

    public void playPauseToggle() {
        Status status = switch (statusProperty.get()) {
            case PAUSED, STOPPED -> imageProvider.hasNext() ? Status.RUNNING : Status.STOPPED;
            case RUNNING -> Status.PAUSED;
        };

        statusProperty.set(status);
    }

    public void loadNextImage() {
        imageProvider.load().ifPresentOrElse(
                namedImage -> {
                    positionProperty.set(imageProvider.getPosition());
                    currentImageNameProperty.set(namedImage.getName());
                    currentImageProperty.set(namedImage.getImage());
                },
                () -> {
                    currentImageNameProperty.set(EMPTY_IMAGE_NAME);
                    currentImageProperty.set(EMPTY_IMAGE);
                }
        );
    }
}
