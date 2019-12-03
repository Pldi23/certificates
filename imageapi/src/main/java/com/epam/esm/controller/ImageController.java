package com.epam.esm.controller;


import com.epam.esm.exception.FileStorageException;
import com.epam.esm.exception.ImageAlreadyExistException;
import com.epam.esm.exception.InvalidFileException;
import com.epam.esm.response.ImageResource;
import com.epam.esm.response.MultipleFilesUploadResponse;
import com.epam.esm.response.UploadFileResponse;
import com.epam.esm.response.ViolationResponse;
import com.epam.esm.service.ImageService;
import com.epam.esm.validator.UploadValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
public class ImageController {

    private final ImageService imageService;
    private final UploadValidator uploadValidator;

    public ImageController(ImageService imageService, UploadValidator uploadValidator) {
        this.imageService = imageService;
        this.uploadValidator = uploadValidator;
    }

    @PostMapping("/")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile[] files, @RequestParam(value = "name", required = false) String name) {
        uploadValidator.validateFiles(files);
        MultipartFile file = files[0];
        uploadValidator.validateSingleFile(file);
        String fileName;
        if (name == null || name.equals("")) {
            uploadValidator.validateFileName(file.getOriginalFilename());
            fileName = imageService.storeFile(file, null);
        } else {
            name = uploadValidator.replaceInvalidSymbols(name);
            uploadValidator.validateFileName(name);
            fileName = imageService.storeFile(file, name);
        }

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/")
                .path(fileName)
                .toUriString();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.LOCATION, fileDownloadUri);
        return ResponseEntity.status(201).headers(httpHeaders).body(new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize()));
    }

    @PostMapping("/uploadMultipleFiles")
    public ResponseEntity uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        HttpHeaders httpHeaders = new HttpHeaders();
        List<UploadFileResponse> entities = new ArrayList<>();
        List<ViolationResponse> violations = new ArrayList<>();
        HttpStatus status = HttpStatus.CREATED;
        for (MultipartFile file : files) {
            try {
                uploadValidator.validateSingleFile(file);
                String fileName = imageService.storeFile(file, null);
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/")
                        .path(fileName)
                        .toUriString();
                httpHeaders.add(HttpHeaders.LOCATION, fileDownloadUri);
                entities.add(new UploadFileResponse(fileName, fileDownloadUri,
                        file.getContentType(), file.getSize()));
            } catch (ImageAlreadyExistException | FileStorageException | InvalidFileException ex) {
                log.info("Image could not be added. Reason: " + ex.getLocalizedMessage());
                status = HttpStatus.BAD_REQUEST;
                violations.add(new ViolationResponse(LocalDateTime.now(), status.value(), "Bad Request", ex.getLocalizedMessage()));
            }
        }

        return ResponseEntity.status(status).headers(httpHeaders).body(new MultipleFilesUploadResponse(entities, violations));
    }

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        ImageResource resource = imageService.loadFileAsResource(fileName);
        return buildResponseEntity(resource, request);
    }

    @GetMapping("/random")
    public ResponseEntity<Resource> downloadRandomFile(HttpServletRequest request) {
        ImageResource resource = imageService.loadRandomAsResource();
        return buildResponseEntity(resource, request);
    }

    private ResponseEntity<Resource> buildResponseEntity(ImageResource resource, HttpServletRequest request) {
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/")
                .path(resource.getFilename())
                .toUriString();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_LOCATION, fileDownloadUri)
                .body(resource);
    }
}
