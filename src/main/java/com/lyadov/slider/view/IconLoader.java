package com.lyadov.slider.view;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Helper class to create {@link ImageView} objects fot the application buttons.
 */
public class IconLoader {
    public static final String PLAY_ICON = "/png/icon-play.png";
    public static final String PAUSE_ICON = "/png/icon-pause.png";
    public static final String RESTART_ICON = "/png/icon-restart.png";
    public static final String FOLDER_ICON = "/png/icon-folder.png";

    private IconLoader() {};

    public static ImageView getIconView(String name, Button button) {
        Image playIcon = new Image(Objects.requireNonNull(IconLoader.class.getResourceAsStream(name)));
        ImageView view = new ImageView(playIcon);
        view.setFitWidth(button.getPrefWidth() * 0.6);
        view.setFitHeight(button.getPrefHeight() * 0.6);
        return view;
    }
}
