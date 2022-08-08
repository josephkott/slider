package com.lyadov.slider;

import com.lyadov.slider.model.LoadImageResult;
import com.lyadov.slider.model.SequentialImageLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SequentialImageLoaderTest {
    static List<String> formats = List.of("JPG", "JPEG");
    static File testDirectory = new File("src/test/resources/directory-with-different-file-types");
    SequentialImageLoader imageLoader;

    @BeforeEach
    void initImageLoader() {
        imageLoader = new SequentialImageLoader(formats, testDirectory);
    }

    @Test
    void testImageLoaderInitialState() {
        Assertions.assertFalse(imageLoader.isExhausted());
        Assertions.assertEquals(3, imageLoader.getPosition().getTotal());
        Assertions.assertEquals(0, imageLoader.getPosition().getCurrent());
    }

    @Test
    void testImageLoaderExhausted() {
        imageLoader.load();
        Assertions.assertFalse(imageLoader.isExhausted());

        imageLoader.load();
        Assertions.assertFalse(imageLoader.isExhausted());

        imageLoader.load();
        Assertions.assertTrue(imageLoader.isExhausted());
    }

    @Test
    void testImageLoaderAlphabeticalOrder() {
        List<String> expected = List.of(
                "bishop.jpeg",
                "knight.jpg",
                "rook.jpeg"
        );
        List<String> actual = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Optional<LoadImageResult> result = imageLoader.load();
            Assertions.assertTrue(result.isPresent());
            actual.add(result.get().getName());
        }

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testImageLoadedLoadResult() {
        Optional<LoadImageResult> result = imageLoader.load();
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("bishop.jpeg", result.get().getName());
        Assertions.assertEquals(64, result.get().getImage().getWidth());
        Assertions.assertEquals(64, result.get().getImage().getHeight());
    }

    @Test
    void testImageLoaderPosition() {
        imageLoader.load();
        Assertions.assertEquals(1, imageLoader.getPosition().getCurrent());

        imageLoader.load();
        Assertions.assertEquals(2, imageLoader.getPosition().getCurrent());

        imageLoader.load();
        Assertions.assertEquals(3, imageLoader.getPosition().getCurrent());
    }
}
