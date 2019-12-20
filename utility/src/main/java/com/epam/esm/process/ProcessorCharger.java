package com.epam.esm.process;

import com.epam.esm.properties.UtilityConfiguration;
import com.epam.esm.service.DataStatistic;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
public class ProcessorCharger implements Runnable {

    private long finish;
    private Processor processor;
    private List<Path> paths;
    private DataStatistic dataStatistic;
    private UtilityConfiguration utilityConfiguration;


    public ProcessorCharger(long finish, Processor processor, List<Path> paths, DataStatistic dataStatistic) {
        this.finish = finish;
        this.processor = processor;
        this.paths = paths;
        this.dataStatistic = dataStatistic;
        this.utilityConfiguration = UtilityConfiguration.getInstance();
    }

    @Override
    public void run() {
        while (System.currentTimeMillis() < finish) {
            processor.addContainer(paths, dataStatistic);
            try {
                TimeUnit.MILLISECONDS.sleep(utilityConfiguration.getPeriodTime());
            } catch (InterruptedException e) {
                log.warn("interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
