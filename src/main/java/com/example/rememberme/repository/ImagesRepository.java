package com.example.rememberme.repository;

import com.example.rememberme.model.Image;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ImagesRepository {
    private List<Image> images = new ArrayList<>();

    public Image getImage(Long id) {
        for (Image image : images) {
            if (image.getId() == id) return image;
        }
        return null;
    }

    public void save(Image image) {
        images.add(image);
    }
}
