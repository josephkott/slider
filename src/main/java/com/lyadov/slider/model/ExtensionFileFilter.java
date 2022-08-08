package com.lyadov.slider.model;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 * File filter that accepts files by their extensions ignoring case.
 */
public class ExtensionFileFilter implements FileFilter {
    private final List<String> extensions;

    public ExtensionFileFilter(String extension) {
        this.extensions = List.of(extension);
    }

    public ExtensionFileFilter(List<String> extension) {
        this.extensions = List.copyOf(extension);
    }

    @Override
    public boolean accept(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        String extension = name.substring(lastIndexOf + 1);
        return lastIndexOf != -1 && extensions.stream().anyMatch(extension::equalsIgnoreCase);
    }
}
