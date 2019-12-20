package com.epam.esm.listener;

import com.epam.esm.service.TagService;
import com.epam.esm.process.CertificateFilesService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class AppListener implements ApplicationListener<ContextRefreshedEvent> {

    private CertificateFilesService filesService;
    private ScheduledExecutorService scheduledExecutorService;
    private TagService tagService;


    public AppListener(CertificateFilesService filesService, TagService tagService) {
        this.filesService = filesService;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.tagService = tagService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("app listener working");
        filesService.listenToCertificatesData();
        scheduledExecutorService.scheduleAtFixedRate(() -> tagService.deleteUnlinkedTags(), 1, 1, TimeUnit.DAYS);
    }

    @PreDestroy
    public void close() {
        scheduledExecutorService.shutdown();
    }



}
