package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;

import java.util.List;

public interface AbstractCertificateRepository extends Repository<GiftCertificate> {

    List<GiftCertificate> getCertificatesByTagId(long tagId);
}
