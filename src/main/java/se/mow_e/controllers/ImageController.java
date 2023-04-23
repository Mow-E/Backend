package se.mow_e.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;
import se.mow_e.repository.CoordinateRepo;
import se.mow_e.websocket.messages.ImageMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.*;

@Controller
//@RequestMapping("/images")
public class ImageController {

    private final Map<Long, List<ByteBuffer>> imageChunkList = new HashMap<>();

    @Autowired
    private CoordinateRepo coordinateRepo;

    @MessageMapping("/images/add")
    public void add(ImageMessage message) throws Exception {

        List<ByteBuffer> chunks;

        Long id = message.getId();
        if (!imageChunkList.containsKey(id)) {
            chunks = new ArrayList<>();
            imageChunkList.put(id, chunks);
        } else {
            chunks = imageChunkList.get(id);
        }

        chunks.add(message.getData());

        if (Objects.equals(message.getChunkAmount()-1, message.getChunkOffset())) {

            int length = 0;
            for (ByteBuffer b : chunks) {
                length += b.remaining();
            }

            ByteBuffer buffer = ByteBuffer.allocate(length);

            for (ByteBuffer b : chunks) {
                buffer.put(b);
            }

            imageChunkList.remove(id); // No memory leak in case someone stops the image transfer

            UUID uuid = UUID.randomUUID();

            // Save the image data to a file
            new File("data/images/").mkdirs();
            try (FileOutputStream fos = new FileOutputStream("data/images/"+uuid+".jpg")) {
                fos.write(buffer.array());
            }
        }
    }

    @GetMapping("/images/{id}")
    public EntityResponse<?> get(@RequestParam String id) {
        return EntityResponse.fromObject("").build();

    }

}
