package com.lyadov.slider.model;

import javafx.scene.image.Image;

/**
 * Class represents an {@link Image} with the corresponding file name.
 */
public class NamedImage {
    private final String name;
    private final Image image;

    public NamedImage(String name, Image image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }
}
