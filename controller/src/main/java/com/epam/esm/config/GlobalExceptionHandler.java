package com.epam.esm.config;

import com.epam.esm.dto.ViolationDTO;
import com.epam.esm.exception.ApplicationDataSourceException;
import com.epam.esm.exception.CriteriaSearchTypeException;
import com.epam.esm.exception.DateNotValidException;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.GenerateDataException;
import com.epam.esm.exception.PaginationException;
import com.epam.esm.exception.UserRoleException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * class to configure exception handling
 *
 * @author Dzmitry Platonov on 2019-09-27.
 * @version 0.0.1
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(new ViolationDTO(errors, 400, LocalDateTime.now()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraitViolationException(ConstraintViolationException ex, WebRequest request) {
        return ResponseEntity.badRequest().body(new ViolationDTO(List.of(ex.getMessage()), 400, LocalDateTime.now()));
    }

    @ExceptionHandler(CriteriaSearchTypeException.class)
    protected ResponseEntity<Object> handleCriteriaSearchException(CriteriaSearchTypeException ex, WebRequest webRequest) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("message", ex.getLocalizedMessage());

        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApplicationDataSourceException.class)
    protected ResponseEntity<Object> handleApplicationDataSourceException(ApplicationDataSourceException ex, WebRequest webRequest) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("message", ex.getLocalizedMessage());

        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        return ResponseEntity.badRequest()
                .body(new ViolationDTO(List.of(ex.getLocalizedMessage().substring(0, ex.getLocalizedMessage().lastIndexOf(';'))),
                        400, LocalDateTime.now()));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String message = ex.getLocalizedMessage();
        return ResponseEntity.badRequest()
                .body(new ViolationDTO(List.of(message.substring(0, message.indexOf('\n'))), 400, LocalDateTime.now()));

    }

    @ExceptionHandler(DateNotValidException.class)
    protected ResponseEntity<Object> handleDateNotValidException(DateNotValidException ex, WebRequest request) {
        return ResponseEntity.badRequest()
                .body(new ViolationDTO(List.of(ex.getLocalizedMessage()),
                        400, LocalDateTime.now()));
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    protected ResponseEntity<Object> handleTagAlreadyExistsException(EntityAlreadyExistsException ex, WebRequest request) {
        return ResponseEntity.badRequest()
                .body(new ViolationDTO(List.of(ex.getLocalizedMessage()), 400, LocalDateTime.now()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        return ResponseEntity.badRequest()
                .body(new ViolationDTO(List.of(ex.getLocalizedMessage()), 404, LocalDateTime.now()));
    }

    @ExceptionHandler(UserRoleException.class)
    protected ResponseEntity<Object> handleRoleException(UserRoleException ex, WebRequest request) {
        return ResponseEntity.badRequest()
                .body(new ViolationDTO(List.of(ex.getLocalizedMessage()), 400, LocalDateTime.now()));
    }

    @ExceptionHandler(PaginationException.class)
    protected ResponseEntity<Object> handlePaginationException(PaginationException ex, WebRequest request) {
        return ResponseEntity.badRequest()
                .body(new ViolationDTO(List.of(ex.getLocalizedMessage()), 400, LocalDateTime.now()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        return ResponseEntity.badRequest()
                .body(new ViolationDTO(List.of(ex.getLocalizedMessage()), 401, LocalDateTime.now()));
    }

    @ExceptionHandler(GenerateDataException.class)
    protected ResponseEntity<Object> handleUsernameNotFoundException(GenerateDataException ex, WebRequest request) {
        return ResponseEntity.badRequest()
                .body(new ViolationDTO(List.of(ex.getLocalizedMessage()), 401, LocalDateTime.now()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        return ResponseEntity.status(401)
                .body(new ViolationDTO(List.of(ex.getLocalizedMessage()), 401, LocalDateTime.now()));
    }

    @ExceptionHandler(DisabledException.class)
    protected ResponseEntity<Object> handleDisabledException(DisabledException ex, WebRequest request) {
        return ResponseEntity.status(401)
                .body(new ViolationDTO(List.of(ex.getLocalizedMessage()), 401, LocalDateTime.now()));
    }

}
