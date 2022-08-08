package com.lyadov.slider.model;

import javafx.animation.Animation.Status;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.util.Duration;

import java.io.File;
import java.util.List;

/**
 * Slider application model. It uses {@link SequentialImageLoader} to load next images asynchronously
 * in a background while showing the currently loaded image.
 */
public class SliderModel {
    public static final Duration DELAY = Duration.seconds(3);
    private static final List<String> FORMATS = List.of("JPG", "JPEG");
    private static final Image EMPTY_IMAGE = new WritableImage(16, 16);
    private static final String EMPTY_IMAGE_NAME = "<empty>";

    private File imageDirectory = null;
    private SequentialImageLoader imageLoader = null;

    /* Model binding properties */
    private final StringProperty imageDirectoryNameProperty = new SimpleStringProperty("");
    private final StringProperty currentImageNameProperty = new SimpleStringProperty(EMPTY_IMAGE_NAME);
    private final ObjectProperty<Image> currentImageProperty = new SimpleObjectProperty<>(EMPTY_IMAGE);
    private final ObjectProperty<Position> positionProperty = new SimpleObjectProperty<>(Position.EMPTY);
    private final ObjectProperty<Status> statusProperty = new SimpleObjectProperty<>(Status.STOPPED);

    /**
     * Creates an empty model without an image loader
     */
    public SliderModel() {}

    public ObjectProperty<Status> getStatusProperty() {
        return statusProperty;
    }

    public static SliderModel fromImageDirectory(File imageDirectory) {
        SliderModel model = new SliderModel();
        model.setImageDirectory(imageDirectory);
        return model;
    }

    public void setImageDirectory(File imageDirectory) {
        this.imageDirectory = imageDirectory;
        imageLoader = new SequentialImageLoader(FORMATS, imageDirectory);

        imageDirectoryNameProperty.set(imageDirectory.getName());
        statusProperty.set(Status.STOPPED);
        positionProperty.set(imageLoader.getPosition());

        loadNextImage();
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
        validateImageLoader();

        statusProperty.set(Status.STOPPED);
        imageLoader = new SequentialImageLoader(FORMATS, imageDirectory);
        positionProperty.set(imageLoader.getPosition());

        loadNextImage();
    }

    public void playPauseToggle() {
        validateImageLoader();

        Status status = switch (statusProperty.get()) {
            case PAUSED, STOPPED -> !imageLoader.isExhausted() ? Status.RUNNING : Status.STOPPED;
            case RUNNING -> Status.PAUSED;
        };

        statusProperty.set(status);
    }

    public void loadNextImage() {
        validateImageLoader();

        imageLoader.load().ifPresentOrElse(
                result -> {
                    positionProperty.set(imageLoader.getPosition());
                    currentImageNameProperty.set(result.getName());
                    currentImageProperty.set(result.getImage());
                },
                () -> {
                    currentImageNameProperty.set(EMPTY_IMAGE_NAME);
                    currentImageProperty.set(EMPTY_IMAGE);
                }
        );
    }

    private void validateImageLoader() throws RuntimeException {
        if (imageLoader == null) {
            throw new RuntimeException("Image directory is not set");
        }
    }
}
