package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Builder
@AllArgsConstructor
@Getter
public class BrokenGiftCertificateDTO {

    private Long id;
    private String brokenName;
    private String brokenDescription;
    private BigDecimal brokenPrice;
    private LocalDate brokenCreationDate;
    private LocalDate brokenModificationDate;
    private LocalDate brokenExpirationDate;
    private Set brokenTags;

    public BrokenGiftCertificateDTO() {
    }


}
