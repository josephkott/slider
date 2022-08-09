package com.lyadov.slider;

import com.lyadov.slider.model.NamedImage;
import com.lyadov.slider.model.ImageProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImageProviderTest {
    static List<String> formats = List.of("JPG", "JPEG");
    static File directoryWithImages = new File("src/test/resources/directory-with-different-file-types");
    static File directoryWithoutImages = new File("src/test/resources/directory-without-images");
    static File emptyDirectory = new File("src/test/resources/empty-directory");

    @Test
    void shouldHaveZeroPositionWhenCreatedWithEmptyDirectory() {
        ImageProvider imageProvider = new ImageProvider(List.of(), emptyDirectory);
        Assertions.assertEquals(0, imageProvider.getPosition().getCurrent());
        Assertions.assertEquals(0, imageProvider.getPosition().getTotal());
    }

    @Test
    void shouldNotHaveNextWhenCreatedWithEmptyDirectory() {
        ImageProvider imageProvider = new ImageProvider(List.of(), emptyDirectory);
        Assertions.assertFalse(imageProvider.hasNext());
    }

    @Test
    void shouldHaveEmptyResultWhenLoadingFromEmptyDirectory() {
        ImageProvider imageProvider = new ImageProvider(List.of(), emptyDirectory);
        Assertions.assertTrue(imageProvider.load().isEmpty());
    }

    @Test
    void shouldHaveZeroPositionWhenNoFileMatchesFormats() {
        ImageProvider imageProvider = new ImageProvider(formats, directoryWithoutImages);
        Assertions.assertEquals(0, imageProvider.getPosition().getCurrent());
        Assertions.assertEquals(0, imageProvider.getPosition().getTotal());
    }

    @Test
    void shouldNotHaveNextWhenNoFileMatchesFormats() {
        ImageProvider imageProvider = new ImageProvider(formats, directoryWithoutImages);
        Assertions.assertFalse(imageProvider.hasNext());
    }

    @Test
    void shouldHaveEmptyResultWhenNoFileMatchesFormats() {
        ImageProvider imageProvider = new ImageProvider(formats, directoryWithoutImages);
        Assertions.assertTrue(imageProvider.load().isEmpty());
    }

    @Test
    void shouldHaveNextWhenDirectoryContainsImages() {
        ImageProvider imageProvider = new ImageProvider(formats, directoryWithImages);
        Assertions.assertTrue(imageProvider.hasNext());
    }

    @Test
    void hasValidInitialPositionWhenDirectoryContainsImages() {
        ImageProvider imageProvider = new ImageProvider(formats, directoryWithImages);
        Assertions.assertEquals(3, imageProvider.getPosition().getTotal());
        Assertions.assertEquals(0, imageProvider.getPosition().getCurrent());
    }

    @Test
    void loadsCorrectImageWhenDirectoryContainsImages() {
        ImageProvider imageProvider = new ImageProvider(formats, directoryWithImages);

        Optional<NamedImage> result = imageProvider.load();
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("bishop.jpeg", result.get().getName());
        Assertions.assertEquals(64, result.get().getImage().getWidth());
        Assertions.assertEquals(64, result.get().getImage().getHeight());
    }

    @Test
    void shouldNotHaveNextWhenAllImagesAreAlreadyLoaded() {
        ImageProvider imageProvider = new ImageProvider(formats, directoryWithImages);

        for (int i = 0; i < 3; i++) {
            imageProvider.load();
        }

        Assertions.assertFalse(imageProvider.hasNext());
    }

    @Test
    void shouldLoadImagesInAlphabeticalOrder() {
        ImageProvider imageProvider = new ImageProvider(formats, directoryWithImages);
        List<String> expected = List.of("bishop.jpeg", "knight.jpg", "rook.jpeg");

        List<String> actual = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Optional<NamedImage> result = imageProvider.load();
            Assertions.assertTrue(result.isPresent());
            actual.add(result.get().getName());
        }

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldChangePositionAfterLoadingNextImage() {
        ImageProvider imageProvider = new ImageProvider(formats, directoryWithImages);

        imageProvider.load();
        Assertions.assertEquals(1, imageProvider.getPosition().getCurrent());

        imageProvider.load();
        Assertions.assertEquals(2, imageProvider.getPosition().getCurrent());

        imageProvider.load();
        Assertions.assertEquals(3, imageProvider.getPosition().getCurrent());
    }
}
