package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-12-11.
 * @version 0.0.1
 */
public interface JpaTagRepository extends JpaRepository<Tag, Long> {
}
