package com.example.rememberme.service;

import com.example.rememberme.model.Image;
import com.example.rememberme.repository.ImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class ImagesService {

    private final ImagesRepository imagesRepository;
    public InputStreamResource getImage(Long id) {
        Image image = imagesRepository.getImage(id);
        return new InputStreamResource(new ByteArrayInputStream(image.getBytes()));
    }
}
