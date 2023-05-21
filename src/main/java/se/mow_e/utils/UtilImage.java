package se.mow_e.utils;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class UtilImage {

    public static final String IMAGES_DIR = "data/images/";
    public static final String IMAGE_FORMAT = ".jpg";

    public static void save(String imageId, ByteBuffer buffer) throws IOException {

        // Save the image data to a file
        new File(IMAGES_DIR).mkdirs();
        String imgPath = IMAGES_DIR + imageId + IMAGE_FORMAT;

        FileOutputStream fos = new FileOutputStream(imgPath);
        fos.write(buffer.array());
        fos.flush();
        fos.close();

    }

    public static void remove(String imageId) {
        Path path = Paths.get(IMAGES_DIR + imageId + IMAGE_FORMAT);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            // ignore
        }
    }

    public static Long getDirSize() throws IOException {
        Path path = Paths.get(IMAGES_DIR);

        if (!Files.exists(path)) {
            return 0L;
        }
        Stream<Path> walk = Files.walk(path);
        long sum = walk
                .filter(p -> p.toFile().isFile())
                .mapToLong(p -> p.toFile().length())
                .sum();

        walk.close();

        return sum;
    }
}
