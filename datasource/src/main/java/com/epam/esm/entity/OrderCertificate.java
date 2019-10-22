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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-19.
 * @version 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString(exclude = {"order", "certificate"})
@Entity
@Table(name = "order_certificate")
public class OrderCertificate implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Order order;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn
    private GiftCertificate certificate;

    @Column(name = "fixed_price")
    private BigDecimal fixedPrice;

    private LocalDate expirationDate;
}
