package com.epam.esm.validator;

import com.epam.esm.exception.IllegalImageNameException;
import com.epam.esm.exception.InvalidFileException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Log4j2
public class UploadValidator {

    private static final int MAXIMUM_FILENAME_LENGTH = 255;
    private static final int MINIMUM_FILENAME_LENGTH = 3;
    private static final String FILENAME_WITH_EXTENSION_REGEX_PATTERN = "^[\\w\\s-]{3,255}(.\\w+)$";
    private static final String FILENAME_WITHOUT_EXTENSION_REGEX_PATTERN = "^[\\w\\s-]{3,255}$";

    public void validateFiles(MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new InvalidFileException("Please provide image");
        }
        if (files.length > 1) {
            throw new InvalidFileException("Please provide single image");
        }
    }

    public void validateSingleFile(MultipartFile file) {
        if (file == null) {
            throw new InvalidFileException("Please provide image");
        }
        if (file.getContentType() == null || !file.getContentType().contains("image") || file.getOriginalFilename() == null) {
            throw new InvalidFileException("Invalid file format. Please provide image");
        }
    }

    public void validateFileName(String  filename, boolean withExtension) {
        String regex = withExtension ? FILENAME_WITH_EXTENSION_REGEX_PATTERN : FILENAME_WITHOUT_EXTENSION_REGEX_PATTERN;
        if (filename.length() >= MAXIMUM_FILENAME_LENGTH
                || filename.length() < MINIMUM_FILENAME_LENGTH
                || !filename.matches(regex)) {
            throw new IllegalImageNameException("Invalid name. Image name restrictions: - should be less than 255 symbols. " +
                    " More than 3 symbols. " +
                    " Not to start from non-word symbol.");
        }
    }



    public String replaceInvalidSymbols(String filename) {
        return filename.replaceAll("[\\\\/]", ":");
    }
}
