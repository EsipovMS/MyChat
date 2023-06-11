package com.example.rememberme.controller;

import com.example.rememberme.service.ImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImagesController {

    private final ImagesService imagesService;

    @GetMapping("/images/{id}")
    public InputStreamResource getImage(@PathVariable Long id) {
        return imagesService.getImage(id);
    }

}
