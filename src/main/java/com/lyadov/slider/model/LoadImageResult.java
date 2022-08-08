package com.lyadov.slider.model;

import javafx.scene.image.Image;

public class LoadImageResult {
    String name;
    Image image;

    public LoadImageResult(String name, Image image) {
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
