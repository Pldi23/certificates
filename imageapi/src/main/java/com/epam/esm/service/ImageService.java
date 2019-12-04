package com.epam.esm.service;

import com.epam.esm.response.ImageResource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String storeFile(MultipartFile file, String name);
    ImageResource loadFileAsResource(String fileName);
    ImageResource loadRandomAsResource();
}
