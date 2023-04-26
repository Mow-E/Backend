package se.mow_e.services;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ImgClassificationService {

    private final Logger log = LoggerFactory.getLogger(ImgClassificationService.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private CloudVisionTemplate cloudVisionTemplate;


    public Map<String, Float> extractLabels(String imageUrl) throws Exception {

        try {

            AnnotateImageResponse response =
                    this.cloudVisionTemplate.analyzeImage(
                            this.resourceLoader.getResource("File:" + imageUrl), Type.LABEL_DETECTION);

            return response.getLabelAnnotationsList().stream()
                    .collect(
                            Collectors.toMap(
                                    EntityAnnotation::getDescription,
                                    EntityAnnotation::getScore));

        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new Exception("Unable to extract labels from image.");
        }
    }
}
