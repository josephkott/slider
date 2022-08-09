package com.lyadov.slider.model;

import java.io.File;
import java.io.FileFilter;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * File filter that accepts files by their extensions ignoring case.
 */
public class ExtensionFileFilter implements FileFilter {
    private final Set<String> extensions;

    public ExtensionFileFilter(String extension) {
        this(List.of(extension));
    }

    public ExtensionFileFilter(Collection<String> extensions) {
        this.extensions = extensions.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean accept(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");

        return lastIndexOf != -1 && extensions.contains(
                name.substring(lastIndexOf + 1).toLowerCase()
        );
    }
}
