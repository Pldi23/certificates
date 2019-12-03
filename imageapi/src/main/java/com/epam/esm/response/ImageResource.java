package com.epam.esm.response;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.net.URI;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class ImageResource extends UrlResource {

    private String filename;

    public ImageResource(URI uri, String filename) throws MalformedURLException {
        super(uri);
        this.filename = filename;
    }
}
