package com.epam.esm.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.epam.esm.exception.FileStorageException;
import com.epam.esm.exception.ImageAlreadyExistException;
import com.epam.esm.exception.ImageNotFoundException;
import com.epam.esm.model.Image;
import com.epam.esm.repository.ImageRepository;
import com.epam.esm.response.ImageResource;
import com.epam.esm.util.S3Properties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final S3Properties s3Properties;

    public ImageServiceImpl(S3Properties s3Properties, ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
        this.s3Properties = s3Properties;
    }

    @Transactional
    public String storeFile(MultipartFile file, String name) {
        String fileName = name == null ? StringUtils.cleanPath(file.getOriginalFilename()) :
                name + getFileExtension(StringUtils.cleanPath(file.getOriginalFilename()));

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Image image = imageRepository.save(Image.builder()
                    .name(fileName)
                    .size(file.getSize())
                    .key(UUID.randomUUID().toString())
                    .build());

            File tempFile = File.createTempFile(getFileNameWithoutExtension(fileName), getFileExtension(fileName));
            Files.copy(file.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            tempFile.deleteOnExit();

            getClient().putObject(
                    s3Properties.getBucket(),
                    image.getKey(),
                    new File(tempFile.toPath().toUri())
            );
            return fileName;
        } catch (DataIntegrityViolationException ex) {
            throw new ImageAlreadyExistException("Image " + fileName + " already exists!", ex);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public ImageResource loadFileAsResource(String fileName) {
        try {
            Image image = imageRepository.findImageByName(fileName).orElseThrow(() -> new ImageNotFoundException("File not found " + fileName));
            return convertImageToResource(image);
        } catch (MalformedURLException ex) {
            throw new ImageNotFoundException("File not found " + fileName, ex);
        } catch (IOException ex) {
            throw new FileStorageException("Could not load file " + fileName + ". Please try again!", ex);
        }
    }

    public ImageResource loadRandomAsResource() {
        try {
            Image image = imageRepository.findImageRandom().orElseThrow(() -> new ImageNotFoundException("No random file found "));
            return convertImageToResource(image);
        } catch (MalformedURLException ex) {
            throw new ImageNotFoundException("No random file found ", ex);
        } catch (IOException ex) {
            throw new FileStorageException("Could not load file. Please try again!", ex);
        }
    }

    private ImageResource convertImageToResource(Image image) throws IOException {
        S3Object s3object = getClient().getObject(s3Properties.getBucket(), image.getKey());
        S3ObjectInputStream inputStream = s3object.getObjectContent();
        File tempFile = File.createTempFile(getFileNameWithoutExtension(image.getName()), getFileExtension(image.getName()));
        Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        tempFile.deleteOnExit();
        return new ImageResource(tempFile.toPath().toUri(), image.getName());

    }

    private AmazonS3 getClient() {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(getCredentials()))
                .withRegion(Regions.EU_NORTH_1)
                .build();
    }

    private AWSCredentials getCredentials() {
        return new BasicAWSCredentials(
                s3Properties.getAccessKey(),
                s3Properties.getSecretKey()
        );
    }

    private String getFileExtension(String filename) {
        int lastIndexOf = filename.lastIndexOf('.');
        if (lastIndexOf == -1) {
            return "";
        }
        return filename.substring(lastIndexOf);
    }

    private String getFileNameWithoutExtension(String filename) {
        int lastIndexOf = filename.lastIndexOf('.');
        if (lastIndexOf == -1) {
            return "";
        }
        String filenameWithoutExtension = filename.substring(0, lastIndexOf);
        if (filenameWithoutExtension.length() < 3) {
            return filenameWithoutExtension + "prefix";
        }
        return filenameWithoutExtension;
    }
}
