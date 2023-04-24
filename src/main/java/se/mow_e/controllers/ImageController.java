package se.mow_e.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;
import se.mow_e.models.Coordinate;
import se.mow_e.repository.CoordinateRepo;
import se.mow_e.services.ImgClassificationService;
import se.mow_e.websocket.messages.ImageMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.Executor;

@Controller
public class ImageController {

    private final Logger log = LoggerFactory.getLogger(ImageController.class);

    private final Map<Long, ByteBuffer[]> imageChunkList = new HashMap<Long, ByteBuffer[]>();   // TODO - Replace with CacheMap - AAAAAAA

    @Autowired
    private CoordinateRepo coordinateRepo;

    @Qualifier("imageClassificationTaskExecutor")
    @Autowired
    private Executor executor;

    @Autowired
    private ImgClassificationService imgClassificationService;

    @MessageMapping("/images/add")
    public void add(ImageMessage message) throws Exception {

        ByteBuffer[] chunks;
        Long id = message.getId();

        if (!imageChunkList.containsKey(id)) {
            chunks = new ByteBuffer[message.getChunkAmount()];
            imageChunkList.put(id, chunks);
        } else {
            chunks = imageChunkList.get(id);
        }

        chunks[message.getChunkOffset()] = message.getData();

        if (allChunksReceived(chunks)) {

            int length = 0;
            for (ByteBuffer b : chunks) {
                if (b != null) length += b.remaining();
            }

            ByteBuffer buffer = ByteBuffer.allocate(length);

            for (ByteBuffer b : chunks) {
                if (b != null) buffer.put(b);
            }

            imageChunkList.remove(id); // No memory leak in case someone stops the image transfer

            UUID uuid = UUID.randomUUID();

            Coordinate coordinate = coordinateRepo.findCoordinateByTempImageId(String.valueOf(message.getId()));

            if (coordinate != null) {
                coordinate.setImageId(uuid.toString());
                coordinateRepo.save(coordinate);

                // Save the image data to a file
                new File("data/images/").mkdirs();
                String imgPath = "data/images/" + uuid + ".jpg";
                try (FileOutputStream fos = new FileOutputStream(imgPath)) {
                    fos.write(buffer.array());
                }

                executor.execute(() -> {
                    try {
//                        log.info("Started recognizing the objects from the image: " + imgPath );

                        Map<String, Float> labels = imgClassificationService.extractLabels(imgPath);

                        coordinate.setExtra(labels.toString());
                        coordinateRepo.save(coordinate);

                        log.info("Recognition successful for " + imgPath);

                    } catch (Exception e) {
                        log.error("Error during image classification process for " + imgPath, e);
                    }
                });

            } else {
                log.error("No coordinate found for attaching the image with id: " + message.getId());
            }

        }
    }

    private boolean allChunksReceived(ByteBuffer[] chunks) {
        for (ByteBuffer b : chunks) {
            if (b == null) return false;
        }
        return true;
    }

    @GetMapping("/images/{id}")
    public EntityResponse<?> get(@RequestParam String id) {
        return EntityResponse.fromObject("").build();

    }

}
