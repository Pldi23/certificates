package com.epam.esm.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "s3")
public class S3Properties {

    private String bucket;
    private String accessKey;
    private String secretKey;
}
