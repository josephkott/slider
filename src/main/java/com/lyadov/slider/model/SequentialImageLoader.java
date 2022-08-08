package com.lyadov.slider.model;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Loads images sequentially from a given directory. Images are sorted alphabetically.
 * {@link SequentialImageLoader} uses internal buffer to load the next image asynchronously
 * when the current image is accessed.
 */
public class SequentialImageLoader {
    private final File[] imageFiles;
    private int index;

    private final Runnable loadTask;
    private final BlockingQueue<Image> buffer = new ArrayBlockingQueue<>(1);
    private final Executor taskExecutor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    });

    public SequentialImageLoader(List<String> formats, File directory) {
        imageFiles = directory.listFiles(new ExtensionFileFilter(formats));
        Arrays.sort(Objects.requireNonNull(imageFiles));

        index = 0;
        loadTask = () -> {
            try {
                Image image = new Image(new FileInputStream(imageFiles[index]));
                buffer.put(image);
            } catch (FileNotFoundException | InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        };

        if (!isExhausted()) {
            taskExecutor.execute(loadTask);
        }
    }

    /**
     * Returns currently loaded image or {@link Optional#empty()} if there are no images left.
     * If there is the next image to load, create a loading task to create an {@link Image}
     * object from it.
     *
     * @return {@link Optional} describing image loading result.
     */
    public Optional<LoadImageResult> load() {
        if (isExhausted()) {
            return Optional.empty();
        }

        try {
            String name = imageFiles[index].getName();
            LoadImageResult result = new LoadImageResult(name, buffer.take());

            index++;
            if (!isExhausted()) {
                taskExecutor.execute(loadTask);
            }

            return Optional.of(result);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }

        return Optional.empty();
    }

    public boolean isExhausted() {
        return index >= imageFiles.length;
    }

    /**
     * @return {@link Position} of the image loading progress.
     */
    public Position getPosition() {
        return new Position(index, imageFiles.length);
    }
}
