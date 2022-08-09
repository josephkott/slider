package com.lyadov.slider.view;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
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
        URL url = Objects.requireNonNull(IconLoader.class.getResource(name));
        Image icon = new Image(url.toExternalForm());
        ImageView view = new ImageView(icon);
        view.setFitWidth(button.getPrefWidth() * 0.6);
        view.setFitHeight(button.getPrefHeight() * 0.6);
        return view;
    }
}
