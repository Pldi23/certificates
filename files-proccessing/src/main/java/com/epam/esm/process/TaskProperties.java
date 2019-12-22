package com.epam.esm.process;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@PropertySource("task.properties")
public class TaskProperties {

    @Value("${task.test-time}")
    private int testTime;
    @Value("${task.files-count}")
    private int filesCount;
    @Value("${task.period-time}")
    private int periodTime;
    @Value("${task.subfolders-count}")
    private int subfoldersCount;
    @Value("${task.scan-delay}")
    private int scanDelay;
    @Value("${task.initial-delay}")
    private int initialDelay;
    @Value("${task.folder}")
    private String folder;
    @Value("${task.thread-count}")
    private int threadCount;
    @Value("${task.error-folder}")
    private String errorFolder;
    @Value("${task.error-data-integrity-folder}")
    private String errorDataIntegrityFolder;
    @Value("${task.error-json-mapping-folder}")
    private String errorJsonMappingFolder;
    @Value("${task.error-validator-violations-folder}")
    private String errorValidatorViolationsFolder;
    @Value("${task.error-mismatch-input-folder}")
    private String errorMismatchInputFolder;
    @Value("${task.marker-file-name}")
    private String markerFileName;
}
