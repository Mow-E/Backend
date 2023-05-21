package se.mow_e.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import se.mow_e.models.Coordinate;
import se.mow_e.models.Mower;
import se.mow_e.repository.CoordinateRepo;
import se.mow_e.repository.MowerRepo;
import se.mow_e.utils.UtilImage;
import se.mow_e.websocket.messages.ImageMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

@Service
public class ImageService {

    private final Logger log = LoggerFactory.getLogger(ImageService.class);

    private final Map<Long, ByteBuffer[]> imageChunkList = new HashMap<>();   // TODO - Replace with CacheMap - AAAAAAA

    @Autowired
    private CoordinateRepo coordinateRepo;

    @Qualifier("imageClassificationTaskExecutor")
    @Autowired
    private Executor executor;

    @Autowired
    private ImgClassificationService imgClassificationService;

    @Autowired
    private MowerRepo mowerRepo;



    public void add(ImageMessage message) {

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

            UUID imageId = UUID.randomUUID();

            Coordinate coordinate = coordinateRepo.findCoordinateByTempImageId(String.valueOf(message.getId()));

            if (coordinate != null) {
                coordinate.setImageId(imageId.toString());

                try  {
                    UtilImage.save(imageId.toString(), buffer);
                } catch (IOException e) {
                    log.error("Error while saving image", e);
                    return;
                }

                Mower mower = mowerRepo.findMowerByMowerId(coordinate.getMowerId());
                mower.addImage(imageId.toString());

                coordinateRepo.save(coordinate);
                mowerRepo.save(mower);

                executor.execute(() -> {
                    String imgPath = UtilImage.IMAGES_DIR + imageId + UtilImage.IMAGE_FORMAT;

                    try {
//                        log.info("Started recognizing the objects from the image: " + imgPath );

                        Map<String, Float> labels = imgClassificationService.extractLabels(imgPath);

                        coordinate.setExtra(labels.toString());
                        coordinateRepo.save(coordinate);

                        log.info("Recognition successful for " + imgPath);

                    } catch (Exception e) {
                        log.error("Error during image classification process for " + imgPath, e);
                    }
                    log.info("Executor exists");
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

}
