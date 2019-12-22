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

    private List<Path> paths;
    private long finish;
    private DataStatistic statistic;
    private Semaphore semaphore;
    private AtomicInteger counter;

    public ThreadStarter(List<Path> paths, long finish, DataStatistic statistic, Semaphore semaphore, AtomicInteger counter) {
        this.paths = paths;
        this.finish = finish;
        this.statistic = statistic;
        this.semaphore = semaphore;
        this.counter = counter;
    }

    public void work() {
        while (System.currentTimeMillis() <= finish) {
            paths.forEach(path -> {
                counter.incrementAndGet();
                new Thread(new FilesCreator(path, statistic, semaphore, counter)).start();
            });
            try {
                TimeUnit.MILLISECONDS.sleep(UtilityConfiguration.getInstance().getPeriodTime());
            } catch (InterruptedException e) {
                log.warn("interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
