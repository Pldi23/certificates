package com.epam.esm.process;

import com.epam.esm.properties.UtilityConfiguration;
import com.epam.esm.service.DataStatistic;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ThreadStarter {

    private static final int MAX_THREADS_LIMIT = 1000;

    private List<Path> paths;
    private long finish;
    private DataStatistic statistic;
    private Semaphore semaphore;
    private AtomicInteger counter;
    private UtilityConfiguration utilityConfiguration;

    public ThreadStarter(List<Path> paths, long finish, DataStatistic statistic, Semaphore semaphore, AtomicInteger counter, UtilityConfiguration utilityConfiguration) {
        this.paths = paths;
        this.finish = finish;
        this.statistic = statistic;
        this.semaphore = semaphore;
        this.counter = counter;
        this.utilityConfiguration = utilityConfiguration;
    }

    public void work() {
        while (System.currentTimeMillis() <= finish) {
            long startedAt = System.currentTimeMillis();
            startThreadBatch();
            try {
                TimeUnit.MILLISECONDS.sleep(getDelayInMillis(startedAt));
            } catch (InterruptedException e) {
                log.warn("interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private void startThreadBatch() {
        paths.forEach(this::startThread);
    }

    private void startThread(Path path) {
        while (counter.get() > MAX_THREADS_LIMIT) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.warn("interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
        counter.incrementAndGet();
        new Thread(new FilesCreator(path, statistic, semaphore, counter)).start();

    }

    private long getDelayInMillis(long startedAt) {
        long restartAt = startedAt + utilityConfiguration.getPeriodTime() - System.currentTimeMillis();
        return restartAt >= 0 ? restartAt : 0;
    }
}
