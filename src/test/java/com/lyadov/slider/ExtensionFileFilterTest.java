package com.lyadov.slider;

import com.lyadov.slider.model.ExtensionFileFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Assertions;

public class ExtensionFileFilterTest {
    static File testDirectory = new File("src/test/resources/directory-with-different-file-types");

    @Test
    void testJpgExtensionFileFilter() {
        ExtensionFileFilter filter = new ExtensionFileFilter("jpg");
        File[] files = Objects.requireNonNull(testDirectory.listFiles(filter));
        Assertions.assertEquals(1, files.length);

        List<String> fileNames = Arrays.stream(files).map(File::getName).toList();
        Assertions.assertTrue(fileNames.contains("knight.jpg"));
    }

    @Test
    void testJpgAndJpegExtensionFileFilter() {
        ExtensionFileFilter filter = new ExtensionFileFilter(List.of("JPG", "jpeg"));
        File[] files = Objects.requireNonNull(testDirectory.listFiles(filter));
        Assertions.assertEquals(3, files.length);

        List<String> fileNames = Arrays.stream(files).map(File::getName).toList();
        Assertions.assertTrue(fileNames.containsAll(List.of("knight.jpg", "bishop.jpeg", "rook.jpeg")));
    }

    @Test
    void testNoFilesFound() {
        ExtensionFileFilter filter = new ExtensionFileFilter("xxx");
        File[] files = Objects.requireNonNull(testDirectory.listFiles(filter));
        Assertions.assertEquals(0, files.length);
    }
}
