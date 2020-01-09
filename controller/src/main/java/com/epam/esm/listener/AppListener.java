package com.epam.esm.listener;

import com.epam.esm.process.TaskProperties;
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
    private ScheduledExecutorService unUsedTagsScheduledExecutorService;
    private ScheduledExecutorService dataProcessingScheduledExecutorService;
    private TaskProperties taskProperties;
    private TagService tagService;


    public AppListener(CertificateFilesService filesService, TagService tagService, TaskProperties taskProperties) {
        this.filesService = filesService;
        this.unUsedTagsScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.dataProcessingScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.taskProperties = taskProperties;
        this.tagService = tagService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        dataProcessingScheduledExecutorService.scheduleWithFixedDelay(() -> filesService.scanAndProduceIfNeeded(), taskProperties.getInitialDelay(), taskProperties.getScanDelay(), TimeUnit.MILLISECONDS);
        unUsedTagsScheduledExecutorService.scheduleAtFixedRate(() -> tagService.deleteUnlinkedTags(), 1, 1, TimeUnit.DAYS);
    }


    @PreDestroy
    public void close() {
        unUsedTagsScheduledExecutorService.shutdown();
        dataProcessingScheduledExecutorService.shutdown();
    }



}
