package com.epam.esm.entity;

import java.security.cert.Certificate;
import java.util.Objects;
import java.util.Set;

/**
 * giftcertificates
 *
 * @author Dzmitry Platonov on 2019-09-23.
 * @version 0.0.1
 */
public class Tag implements Entity{

    private String title;
    private Set<Certificate> certificates;

    public Tag() {
    }

    public Tag(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(Set<Certificate> certificates) {
        this.certificates = certificates;
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
