package com.epam.esm.controller;

import com.epam.esm.response.ViolationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    protected ResponseEntity<Object> handleMultipartSizeException(MaxUploadSizeExceededException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(
                new ViolationResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getLocalizedMessage().substring(0, ex.getLocalizedMessage().indexOf(';'))));
    }

    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<Object> handleMultipartException(MultipartException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(
                new ViolationResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getLocalizedMessage()));
    }
}
