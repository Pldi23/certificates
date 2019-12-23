package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * gift-certificates
 *
 * @author Dzmitry Platonov on 2019-12-11.
 * @version 0.0.1
 */
public interface JpaTagRepository extends JpaRepository<Tag, Long> {

    Tag findTagByTitle(String title);

    default Set<Tag> saveOrMergeAll(Set<Tag> tags) {
        return tags.stream().map(tag -> {
            Tag tagByTitle = findTagByTitle(tag.getTitle());
            return Objects.requireNonNullElseGet(tagByTitle, () -> save(tag));
        }).collect(Collectors.toSet());

    }

    Set<Tag> saveAllTags(@Param(value = "tags") String tags);
}
