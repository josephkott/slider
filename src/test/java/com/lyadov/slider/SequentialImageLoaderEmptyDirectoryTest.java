package com.lyadov.slider;

import com.lyadov.slider.model.SequentialImageLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class SequentialImageLoaderEmptyDirectoryTest {
    @Test
    public void testImageLoaderWithEmptyDirectory() {
        String testDirectoryPath = "src/test/resources/empty-directory";
        File emptyDirectory = new File(testDirectoryPath);

        SequentialImageLoader imageLoader = new SequentialImageLoader(List.of(), emptyDirectory);
        Assertions.assertEquals(0, imageLoader.getPosition().getCurrent());
        Assertions.assertEquals(0, imageLoader.getPosition().getTotal());
        Assertions.assertTrue(imageLoader.load().isEmpty());
        Assertions.assertTrue(imageLoader.isExhausted());
    }

    @Test
    public void testImageLoaderDirectoryWithoutImages() {
        String testDirectoryPath = "src/test/resources/directory-without-images";
        File emptyDirectory = new File(testDirectoryPath);

        SequentialImageLoader imageLoader = new SequentialImageLoader(List.of("JPG", "JPEG"), emptyDirectory);
        Assertions.assertEquals(0, imageLoader.getPosition().getCurrent());
        Assertions.assertEquals(0, imageLoader.getPosition().getTotal());
        Assertions.assertTrue(imageLoader.load().isEmpty());
        Assertions.assertTrue(imageLoader.isExhausted());
    }
}
