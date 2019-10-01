package com.epam.esm.entity;

import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
public class Tag extends Entity{

    @Size(min = 1, max = 100, message = "tag title symbols 1-100")
    private String title;

    public Tag() {
    }

    public Tag(long id, String title) {
        super(id);
        this.title = title;
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
        Tag tag = (Tag) o;
        return Objects.equals(title, tag.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }

    @Override
    public String toString() {
        return '<' + title + '>';
    }
}
