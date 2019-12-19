package com.epam.esm.process;

import com.epam.esm.properties.UtilityConfiguration;
import com.epam.esm.service.DataStatistic;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
public class Processor {

    private static final int PROCESSOR_TIMEOUT = 1; //milliseconds

    private ArrayDeque<ThreadContainer> containers;
    private long finishTime;
    private UtilityConfiguration utilityConfiguration;

    public Processor(long finishTime) {
        this.containers = new ArrayDeque<>();
        this.finishTime = finishTime;
        this.utilityConfiguration = UtilityConfiguration.getInstance();
    }

    public void addContainer(List<Path> paths, DataStatistic dataStatistic) {
        ThreadContainer container = new ThreadContainer(paths, this, dataStatistic);
        containers.add(container);
    }

    public void startNext() {
        if (!containers.isEmpty()) {
            containers.removeFirst().startContainer();
        } else if (System.currentTimeMillis() < finishTime) {
            try {
                TimeUnit.MILLISECONDS.sleep(PROCESSOR_TIMEOUT);
            } catch (InterruptedException e) {
                log.warn("interrupted", e);
                Thread.currentThread().interrupt();
            }
            startNext();
        }
    }

}
