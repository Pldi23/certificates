package com.epam.esm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SqlResultSetMapping(
        name = "certificatesCustomMapping",
        classes = {@ConstructorResult(
                targetClass = GiftCertificate.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                @ColumnResult(name = "price", type = BigDecimal.class),
                @ColumnResult(name = "creationDate", type = LocalDate.class),
                @ColumnResult(name = "modificationDate", type = LocalDate.class),
                @ColumnResult(name = "expirationDate", type = LocalDate.class),
                @ColumnResult(name = "activeStatus", type = Boolean.class)})})

@NamedNativeQuery(
        name = "GiftCertificate.saveCertificate",
        query = "select * from insert_certificates_list(:name_in, :description_in, :price_in, :creation_date_in, :modification_date_in, :expiration_date_in, :active_status_in, :tags)",
        resultSetMapping = "certificatesCustomMapping")
@Entity
@Table(name = "certificate")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @NotBlank(message = "{violation.name.not.blank}")
    @Size(min = 1, max = 50, message = "{violation.name.size}")
    @Pattern(regexp = "([\\w-]+(?: [\\w-]+)+)|([\\w-]+)", message = "{violation.certificate.name.pattern}")
    private String name;
    @Size(min = 1, max = 1000, message = "{violation.description}")
    private String description;
    @DecimalMin(value = "0", message = "{violation.price}")
    private BigDecimal price;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate modificationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate expirationDate;
    private Boolean activeStatus;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "certificate_tag",
            joinColumns = @JoinColumn(name = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;


    @OneToMany(mappedBy = "certificate", cascade = CascadeType.ALL)
    private List<OrderCertificate> orderCertificates;

    @PrePersist
    public void onPrePersist() {
        setActiveStatus(true);
        setCreationDate(LocalDate.now());
    }

}
