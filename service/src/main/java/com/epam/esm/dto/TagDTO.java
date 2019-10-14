package com.epam.esm.dto;


import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

@Builder
public class TagDTO {

    @Null(message = "{violation.input.tag.id}")
    private Long id;

    @NotBlank(message = "{violation.title}")
    @Size(min = 1, max = 200, message = "{violation.tag.title.length}")
    @Pattern(regexp = "([\\w-]+(?: [\\w-]+)+)|([\\w-]+)", message = "{violation.tag.title.pattern}")
    private String title;

    public TagDTO() {
    }

    public TagDTO(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        return Objects.equals(id, tagDTO.id) &&
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
