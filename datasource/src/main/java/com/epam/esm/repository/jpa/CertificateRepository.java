package com.epam.esm.repository.jpa;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-10-11.
 * @version 0.0.1
 */
//@Repository
public interface CertificateRepository extends JpaRepository<GiftCertificate, Long> {

    List<GiftCertificate> findByTagsId(long id);

    @Query(nativeQuery = true, value = "delete from certificate_tag where certificate_id = (:integer)")
    void deleteLink(@Param("integer") long id);

    Optional<GiftCertificate> findByName(String name);
}
