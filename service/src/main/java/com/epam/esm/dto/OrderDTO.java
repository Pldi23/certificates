package com.epam.esm.dto;

import com.epam.esm.validator.ValidCertificatesSet;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(value={ "giftCertificates" }, allowSetters=true)
public class OrderDTO {

    @Null(message = "{violation.input.id.should.be.null}")
    private Long id;

    @Null(message = "{violation.user.email}")
    private String userEmail;

    @Null(message = "{violation.creation.at.should.be.null}")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @Null
    private BigDecimal price;

    @ValidCertificatesSet(message = "{violation.certificate.set}")
    private List<GiftCertificateDTO> giftCertificates;

    @Null
    @JsonIgnore
    private Long userId;

    @Null
    @JsonIgnore
    private List<Long> certificatesIds;

}
