package com.lyadov.slider.model;

import javafx.scene.image.Image;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * Loads images sequentially from a given directory. Images are sorted alphabetically.
 * {@link ImageProvider} uses internal buffer to load the next image asynchronously
 * when the current image is accessed.
 */
public class ImageProvider {
    private final File[] imageFiles;
    private int index;

    private final Runnable loadTask;
    private final BlockingQueue<Image> buffer = new ArrayBlockingQueue<>(1);

    public ImageProvider(List<String> formats, File directory) {
        imageFiles = directory.listFiles(new ExtensionFileFilter(formats));
        Arrays.sort(Objects.requireNonNull(imageFiles));

        index = 0;
        loadTask = () -> {
            try (InputStream stream = new FileInputStream(imageFiles[index])) {
                buffer.put(new Image(stream));
            } catch (IOException exception) {
                throw new RuntimeException("I/O exception: " + exception.getMessage());
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        };

        if (hasNext()) {
            new Thread(loadTask).start();
        }
    }

    /**
     * Returns currently loaded image or {@link Optional#empty()} if there are no images left.
     * If there is the next image to load, it creates a new {@link Image} loading task.
     *
     * @return {@link Optional} describing image loading result.
     */
    public Optional<NamedImage> load() {
        if (!hasNext()) {
            return Optional.empty();
        }

        try {
            Image image = buffer.take();
            String name = imageFiles[index].getName();

            index++;
            if (hasNext()) {
                new Thread(loadTask).start();
            }

            return Optional.of(new NamedImage(name, image));
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }

        return Optional.empty();
    }

    public boolean hasNext() {
        return index < imageFiles.length;
    }

    /**
     * @return {@link Position} of the image loading progress.
     */
    public Position getPosition() {
        return new Position(index, imageFiles.length);
    }
}
