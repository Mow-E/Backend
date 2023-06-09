package se.mow_e.models;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import se.mow_e.utils.UtilImage;

import javax.persistence.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
@Entity
@Table(name = "mowers")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Mower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // By default, enabled, but I don't trust them xD
    private Long id;

    @Column(name = "mower_id", unique = true)
    private String mowerId;

    @Column(name = "username")
    private String username;

    @Column(name = "status")
    private boolean status;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> images;

    public Mower(String mowerId) {
        this.mowerId = mowerId;
    }

    public Mower() {
        // Dummy constructor for spring voodoo mindgames
    }

    public String getMowerId() {
        return mowerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getImages() {
        return images;
    }

    public void addImage(String image) {
        if (this.images == null) {
            this.images = new ArrayList<String>();
        }
        this.images.add(image);
    }

    public void removeOldImages() {
        for (String imageId : images) {
            UtilImage.remove(imageId);

        }
        this.images.clear();
    }

}
