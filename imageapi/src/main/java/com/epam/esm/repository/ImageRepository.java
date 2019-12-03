package com.epam.esm.repository;

import com.epam.esm.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findImageByName(String name);

    @Query(nativeQuery=true, value="SELECT *  FROM image ORDER BY random() LIMIT 1")
    Optional<Image> findImageRandom();
}
