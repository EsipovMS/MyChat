package com.example.rememberme.model;

import lombok.Data;

@Data
public class Image {
    private Long id;
    private String name;
    private Long size;
    private byte[] bytes;
}
