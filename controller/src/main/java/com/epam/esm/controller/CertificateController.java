package com.epam.esm.controller;

import com.epam.esm.dto.GiftCertificateDTO;
import com.epam.esm.dto.MessageDTO;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.Map;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-26.
 * @version 0.0.1
 */
@RestController
@EnableWebMvc
@RequestMapping("certificates")
public class CertificateController {

    private CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GiftCertificateDTO> findAll() {
        return certificateService.findAll();
    }

    @GetMapping(value = "/{id}")
    public List<GiftCertificateDTO> findOneById(@PathVariable("id") long id) {
        return certificateService.findOneById(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageDTO add(@RequestBody GiftCertificateDTO giftCertificateDTO) {
        return certificateService.save(giftCertificateDTO);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageDTO update(@RequestBody GiftCertificateDTO giftCertificateDTO) {
        return certificateService.update(giftCertificateDTO);
    }

    @DeleteMapping(value = "/{id}")
    public MessageDTO delete(@PathVariable("id") long id) {
        return certificateService.delete(id);
    }

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String findByCriteria(@RequestParam Map<String, String> criteriaMap) {
//        CriteriaDTO criteriaDTO = new CriteriaDTO(criteriaMap);
        return criteriaMap.toString();
    }
//    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
//    public String findByCriteria(
//            @RequestParam("command") List<String> commands,
//            @RequestParam("then") List<String> thenCommands,
//            @RequestParam("name") List<String> names
//    ) {
//        return "params: " + " com:" + commands + " , then: " + thenCommands + " , nams" + names;
//    }
}
