package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


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
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDate creationDate;
    private LocalDate modificationDate;
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
