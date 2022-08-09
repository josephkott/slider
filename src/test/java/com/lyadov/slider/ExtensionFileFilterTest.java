package com.lyadov.slider;

import com.lyadov.slider.model.ExtensionFileFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

public class ExtensionFileFilterTest {
    @Test
    void shouldAcceptFileWhenItsExtensionExactlyMatchesFilterConfiguration() {
        ExtensionFileFilter filter = new ExtensionFileFilter("JPG");
        File file = new File("/path/to/file.JPG");
        Assertions.assertTrue(filter.accept(file));
    }

    @Test
    void shouldCheckFileExtensionIgnoringCase() {
        ExtensionFileFilter filter = new ExtensionFileFilter("JPG");
        File file = new File("/path/to/file.jpg");
        Assertions.assertTrue(filter.accept(file));
    }

    @Test
    void shouldAcceptFileIfItMatchesAnyConfiguredExtension() {
        ExtensionFileFilter filter = new ExtensionFileFilter(List.of("JPG", "JPEG"));
        File file = new File("/path/to/file.jpeg");
        Assertions.assertTrue(filter.accept(file));
    }

    @Test
    void shouldNotAcceptFileWhenItDoesNotMatchConfiguredExtensions() {
        ExtensionFileFilter filter = new ExtensionFileFilter("JPG");
        File file = new File("/path/to/file.xxx");
        Assertions.assertFalse(filter.accept(file));
    }
}
