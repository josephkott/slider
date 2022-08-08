package com.lyadov.slider;

import com.lyadov.slider.model.SliderModel;
import javafx.animation.Animation.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class SliderModelTest {
    static File testDirectory = new File("src/test/resources/directory-with-jpeg-images");
    static File otherTestDirectory = new File("src/test/resources/directory-with-different-file-types");

    SliderModel model;

    @BeforeEach
    void initModel() {
        model = SliderModel.fromImageDirectory(testDirectory);
    }

    @Test
    void testStatusProperty() {
        Assertions.assertEquals(Status.STOPPED, model.getStatusProperty().get());

        model.playPauseToggle();
        Assertions.assertEquals(Status.RUNNING, model.getStatusProperty().get());

        model.playPauseToggle();
        Assertions.assertEquals(Status.PAUSED, model.getStatusProperty().get());

        model.stop();
        Assertions.assertEquals(Status.STOPPED, model.getStatusProperty().get());
    }

    @Test
    void testSetImageDirectory() {
        model.playPauseToggle();
        model.loadNextImage();

        model.setImageDirectory(otherTestDirectory);
        Assertions.assertEquals(
                "directory-with-different-file-types",
                model.getImageDirectoryNameProperty().get()
        );
        Assertions.assertEquals(Status.STOPPED, model.getStatusProperty().get());
        Assertions.assertEquals(3, model.getPositionProperty().get().getTotal());
        Assertions.assertEquals(1, model.getPositionProperty().get().getCurrent());
    }

    @Test
    void testImageDirectoryNameProperty() {
        Assertions.assertEquals(
                "directory-with-jpeg-images",
                model.getImageDirectoryNameProperty().get()
        );
    }

    @Test
    void testCurrentImageNameProperty() {
        Assertions.assertEquals("bishop.jpeg", model.getCurrentImageNameProperty().get());

        model.loadNextImage();
        Assertions.assertEquals("knight.jpg", model.getCurrentImageNameProperty().get());
    }

    @Test
    void testPositionProperty() {
        Assertions.assertEquals(4, model.getPositionProperty().get().getTotal());
        Assertions.assertEquals(1, model.getPositionProperty().get().getCurrent());

        model.loadNextImage();
        Assertions.assertEquals(2, model.getPositionProperty().get().getCurrent());
    }

    @Test
    void testRestart() {
        model.playPauseToggle();
        model.loadNextImage();
        model.restart();

        Assertions.assertEquals(Status.STOPPED, model.getStatusProperty().get());
        Assertions.assertEquals(1, model.getPositionProperty().get().getCurrent());
    }

    @Test
    void testPlayPauseToggle() {
        model.playPauseToggle();
        Assertions.assertEquals(Status.RUNNING, model.getStatusProperty().get());

        model.playPauseToggle();
        Assertions.assertEquals(Status.PAUSED, model.getStatusProperty().get());
    }

    @Test
    void testLoadNextImage() {
        model.loadNextImage();
        Assertions.assertEquals("knight.jpg", model.getCurrentImageNameProperty().get());
        Assertions.assertEquals(2, model.getPositionProperty().get().getCurrent());

        model.loadNextImage();
        Assertions.assertEquals("prize.jpg", model.getCurrentImageNameProperty().get());
        Assertions.assertEquals(3, model.getPositionProperty().get().getCurrent());
    }
}
