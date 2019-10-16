package com.epam.esm.repository.jpa;

import com.epam.esm.entity.Tag;
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
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query(nativeQuery = true, value = "select * from get_tags_by_certificate(:integer)")
    List<Tag> findTagsByCertificate(@Param("integer") long id);

    boolean existsByTitle(String title);
    Optional<Tag> findByTitle(String title);

}
