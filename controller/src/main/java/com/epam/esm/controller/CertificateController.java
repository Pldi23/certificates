package com.epam.esm.controller;

import com.epam.esm.controller.parser.DtoParser;
import com.epam.esm.controller.validator.RequestParametersValidator;
import com.epam.esm.dto.*;
import com.epam.esm.service.CertificateService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
@RestController
@EnableWebMvc
@RequestMapping("certificates")
@Validated
public class CertificateController {

    private CertificateService certificateService;
    private DtoParser dtoParser;
    private RequestParametersValidator validator;


    public CertificateController(CertificateService certificateService, DtoParser dtoParser, RequestParametersValidator validator) {
        this.certificateService = certificateService;
        this.dtoParser = dtoParser;
        this.validator = validator;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findAll() {
        return ResponseEntity.ok().body(certificateService.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity findOneById(@PathVariable("id") @Min(value = 0, message = "certificate id should be greater than 0") long id) {
        return ResponseEntity.ok().body(certificateService.findOneById(id));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add(@Valid @RequestBody GiftCertificateDTO giftCertificateDTO) {
        MessageDTO messageDTO = certificateService.save(giftCertificateDTO);
        return ResponseEntity.status(messageDTO.getStatus()).body(messageDTO.getMessage());
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@RequestBody GiftCertificateDTO giftCertificateDTO) {
        MessageDTO messageDTO = certificateService.update(giftCertificateDTO);
        return ResponseEntity.status(messageDTO.getStatus()).body(messageDTO.getMessage());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@PathVariable("id") long id) {
        MessageDTO messageDTO = certificateService.delete(id);
        return ResponseEntity.status(messageDTO.getStatus()).body(messageDTO.getMessage());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findByCriteria(@RequestParam Map<String, String> criteriaMap) {
        ResponseEntity responseEntity;
        final DataBinder dataBinder = new DataBinder(criteriaMap);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        if (dataBinder.getBindingResult().hasErrors()) {
            List<String> errors = dataBinder.getBindingResult().getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getCode)
                    .collect(Collectors.toList());
            responseEntity = ResponseEntity.badRequest().body(errors);
        } else {
            SortCriteriaRequestDTO sortCriteriaRequestDTO = dtoParser.parseSortCriteria(criteriaMap);
            SearchCriteriaRequestDTO searchCriteriaRequestDTO = dtoParser.parseSearchCriteria(criteriaMap);
            LimitOffsetCriteriaRequestDTO limitOffsetCriteriaRequestDTO = dtoParser.parseLimitOffsetCriteria(criteriaMap);
            responseEntity = ResponseEntity.ok(certificateService
                    .findByCriteria(searchCriteriaRequestDTO, sortCriteriaRequestDTO, limitOffsetCriteriaRequestDTO));
        }
        return responseEntity;
    }

    @GetMapping(value = "/func", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getByFunction(@RequestParam("desc") String desc, @RequestParam("name") String name) {
        return ResponseEntity.ok(certificateService.getByFunction(desc, name));
    }
}
