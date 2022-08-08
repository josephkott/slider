package com.lyadov.slider;

import com.lyadov.slider.model.SliderModel;
import javafx.animation.Animation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SliderEmptyModelTest {
    @Test
    void testEmptyModel() {
        SliderModel model = new SliderModel();

        Assertions.assertEquals(Animation.Status.STOPPED, model.getStatusProperty().get());
        Assertions.assertEquals(0, model.getPositionProperty().get().getCurrent());
        Assertions.assertEquals(0, model.getPositionProperty().get().getTotal());

        Exception playPauseToggleException = Assertions.assertThrows(
                RuntimeException.class,
                model::playPauseToggle
        );

        Assertions.assertEquals(
                "Image directory is not set",
                playPauseToggleException.getMessage()
        );

        Exception restartException = Assertions.assertThrows(
                RuntimeException.class,
                model::restart
        );

        Assertions.assertEquals(
                "Image directory is not set",
                restartException.getMessage()
        );

        Exception loadNextImageException = Assertions.assertThrows(
                RuntimeException.class,
                model::loadNextImage
        );

        Assertions.assertEquals(
                "Image directory is not set",
                loadNextImageException.getMessage()
        );
    }
}
