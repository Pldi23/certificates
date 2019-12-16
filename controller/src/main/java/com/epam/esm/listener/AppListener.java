package com.epam.esm.listener;

import com.epam.esm.service.processing.CertificateFilesService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Log4j2
@Component
public class AppListener implements ApplicationListener<ContextRefreshedEvent> {

    private CertificateFilesService filesService;
    private ScheduledExecutorService scheduledExecutorService;


    public AppListener(CertificateFilesService filesService) {
        this.filesService = filesService;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        scheduledExecutorService.scheduleAtFixedRate(dataListener, 0, 1, TimeUnit.SECONDS);
//        scheduledExecutorService.schedule(dataListener, 1, TimeUnit.SECONDS);
        log.info("app listener working");
        filesService.listenToCertificatesData();
    }

    private Runnable dataListener = () -> {

        filesService.listenToCertificatesData();
    };

    @PreDestroy
    public void close() {
        scheduledExecutorService.shutdown();
    }



}
