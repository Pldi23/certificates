package com.epam.esm.service;


import com.epam.esm.dto.TagDTO;

import java.util.List;

public interface FindTagsByCertificateService {

    List<TagDTO> getTagsByCertificate(long id);
}
