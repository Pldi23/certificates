package com.epam.esm.process;

import com.epam.esm.service.DataStatistic;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * utility
 *
 * @author Dzmitry Platonov on 2019-12-17.
 * @version 0.0.1
 */
@Slf4j
public class ThreadContainer {

    private static final int BARRIER_TIMEOUT = 10;

    private List<FilesCreator> filesCreators;
    private Processor processor;
    private CyclicBarrier barrier;

    public ThreadContainer(List<Path> paths, Processor processor, DataStatistic dataStatistic) {
        this.barrier = new CyclicBarrier(paths.size() + 1);
        this.filesCreators = paths.stream().map(path -> new FilesCreator(path, barrier, dataStatistic)).collect(Collectors.toList());
        this.processor = processor;
    }

    public void startContainer() {
            filesCreators.forEach(filesCreator -> new Thread(filesCreator).start());
        try {
            barrier.await(BARRIER_TIMEOUT, TimeUnit.SECONDS);
            log.info("signaling");
            processor.startNext();
        } catch (InterruptedException e) {
            log.warn("Interrupted", e);
            Thread.currentThread().interrupt();
        } catch (BrokenBarrierException e) {
            log.warn("Barrier broken", e);
        } catch (TimeoutException e) {
            log.warn("Time out", e);
        }
    }

}
