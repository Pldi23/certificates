package com.epam.esm;

import com.epam.esm.util.S3Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        S3Properties.class
})
public class ImageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageApplication.class, args);
    }
}
