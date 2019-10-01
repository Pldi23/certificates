package com.epam.esm.dto;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * gift certificates
 *
 * @author Dzmitry Platonov on 2019-09-25.
 * @version 0.0.1
 */
public class TagDTO {

    @Min(value = 0, message = "id should be greater than 0")
    private long id;

    @NotBlank(message = "title should not be empty")
    private String title;

    public TagDTO() {
    }

    public TagDTO(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDTO tagDTO = (TagDTO) o;
        return id == tagDTO.id &&
                Objects.equals(title, tagDTO.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "TagDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}