package com.lyadov.slider;

import com.lyadov.slider.model.SliderModel;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class SliderModelTest {
    static File directory = new File("src/test/resources/directory-with-jpeg-images");
    static File otherDirectory = new File("src/test/resources/directory-with-different-file-types");
    static File emptyDirectory = new File("src/test/resources/empty-directory");

    @Test
    void shouldHaveZeroPositionWithEmptyDirectory() {
        SliderModel model = new SliderModel(emptyDirectory);
        Assertions.assertEquals(0, model.getPositionProperty().get().getCurrent());
        Assertions.assertEquals(0, model.getPositionProperty().get().getTotal());
    }

    @Test
    void shouldNotSwitchStateOnPlayPauseToggleWithEmptyDirectory() {
        SliderModel model = new SliderModel(emptyDirectory);

        model.playPauseToggle();
        Assertions.assertEquals(Status.STOPPED, model.getStatusProperty().get());
    }

    @Test
    void shouldBeStoppedAfterCreation() {
        SliderModel model = new SliderModel(directory);
        Assertions.assertEquals(Animation.Status.STOPPED, model.getStatusProperty().get());
    }

    @Test
    void shouldSwitchStateOnPlayPauseToggle() {
        SliderModel model = new SliderModel(directory);

        model.playPauseToggle();
        Assertions.assertEquals(Status.RUNNING, model.getStatusProperty().get());

        model.playPauseToggle();
        Assertions.assertEquals(Status.PAUSED, model.getStatusProperty().get());

        model.playPauseToggle();
        Assertions.assertEquals(Status.RUNNING, model.getStatusProperty().get());
    }

    @Test
    void stopShouldSwitchStatusToStopped() {
        SliderModel model = new SliderModel(directory);

        model.playPauseToggle();
        model.stop();
        Assertions.assertEquals(Status.STOPPED, model.getStatusProperty().get());
    }

    @Test
    void hasProperImageDirectoryNameProperty() {
        SliderModel model = new SliderModel(directory);
        Assertions.assertEquals(
                "directory-with-jpeg-images",
                model.getImageDirectoryNameProperty().get()
        );
    }

    @Test
    void shouldBeStoppedWhenDirectoryIsChanged() {
        SliderModel model = new SliderModel(directory);

        model.playPauseToggle();
        model.loadNextImage();
        model.setImageDirectory(otherDirectory);
        Assertions.assertEquals(Status.STOPPED, model.getStatusProperty().get());
    }

    @Test
    void shouldLoadImagesFromNewDirectoryWhenDirectoryIsChanged() {
        SliderModel model = new SliderModel(directory);

        model.loadNextImage();
        model.setImageDirectory(otherDirectory);
        Assertions.assertEquals("bishop.jpeg", model.getCurrentImageNameProperty().get());
    }

    @Test
    void shouldChangeDirectoryNamePropertyWhenDirectoryIsChanged() {
        SliderModel model = new SliderModel(directory);
        Assertions.assertEquals(
                "directory-with-jpeg-images",
                model.getImageDirectoryNameProperty().get()
        );

        model.setImageDirectory(otherDirectory);
        Assertions.assertEquals(
                "directory-with-different-file-types",
                model.getImageDirectoryNameProperty().get()
        );
    }

    @Test
    void shouldChangeCurrentImageNamePropertyWhenLoadNext() {
        SliderModel model = new SliderModel(directory);
        Assertions.assertEquals("knight.jpg", model.getCurrentImageNameProperty().get());

        model.loadNextImage();
        Assertions.assertEquals("prize.jpg", model.getCurrentImageNameProperty().get());
    }

    @Test
    void shouldChangePositionWhenLoadNext() {
        SliderModel model = new SliderModel(directory);

        model.loadNextImage();
        Assertions.assertEquals(2, model.getPositionProperty().get().getCurrent());

        model.loadNextImage();
        Assertions.assertEquals(3, model.getPositionProperty().get().getCurrent());
    }

    @Test
    void hasProperTotalInPosition() {
        SliderModel model = new SliderModel(directory);
        Assertions.assertEquals(3, model.getPositionProperty().get().getTotal());
    }

    @Test
    void shouldBeStoppedAfterRestart() {
        SliderModel model = new SliderModel(directory);

        model.playPauseToggle();
        model.loadNextImage();
        model.restart();

        Assertions.assertEquals(Status.STOPPED, model.getStatusProperty().get());
    }

    @Test
    void shouldDropPositionAfterRestart() {
        SliderModel model = new SliderModel(directory);

        model.playPauseToggle();
        model.loadNextImage();
        model.restart();

        Assertions.assertEquals(1, model.getPositionProperty().get().getCurrent());
    }
}
