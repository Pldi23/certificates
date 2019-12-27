package com.epam.esm.process;


import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SystemMonitor {

    private AtomicBoolean isFilesProducing = new AtomicBoolean(false);
    private AtomicInteger activeConsumersCounter = new AtomicInteger(0);

    void activateProducer() {
        isFilesProducing.set(true);
    }

    void deActivateProducer() {
        isFilesProducing.set(false);
    }

    boolean getStatus() {
        return isFilesProducing.get();
    }

    void incrementActiveConsumersCounter() {
        activeConsumersCounter.incrementAndGet();
    }

    void decrementActiveConsumersCounter() {
        activeConsumersCounter.decrementAndGet();
    }

    void resetConsumersCount() {
        activeConsumersCounter.set(0);
    }

    int getActiveConsumersCount() {
        return activeConsumersCounter.get();
    }
}
