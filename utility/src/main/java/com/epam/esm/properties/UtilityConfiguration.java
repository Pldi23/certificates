package com.epam.esm.properties;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.file.Path;
import java.util.ResourceBundle;

@Data
@AllArgsConstructor
public class UtilityConfiguration {

    private static final String APP_PROPERTIES_PATH = "app";
    private static final String APP_ROOT = "app.root";
    private static final String APP_ERRORS_PATH = "app.errors";
    private static final String SUB_FOLDERS_COUNT = "app.subfolders_count";
    private static final String TEST_TIME = "app.test_time";
    private static final String FILES_COUNT = "app.files_count";
    private static final String SUB_FOLDER_NAME = "app.sub_folder_name";
    private static final String PERIOD_TIME = "app.period_time";
    private static final String GENERATOR_DIVIDER = "app.generator_divider";
    private static final String WRONG_JSON_FORMAT_INDEX = "app.wrong_json_format_index";
    private static final String BROKEN_FIELD_INDEX = "app.broken_field_names_index";
    private static final String NON_VALID_BEAN_INDEX = "app.non_valid_bean_index";
    private static final String VIOLATES_DB_CONSTRAINT_INDEX = "app.violates_db_constraints_index";
    private static final String MARKER_FILENAME = "app.marker_file_name";


    private static UtilityConfiguration instance;

    private Path root;
    private Path errors;
    private int subFoldersCount;
    private int testTime;
    private int filesCount;
    private String subFolderName;
    private int periodTime;
    private int generatorDivider;
    private int wrongJsonFormatIndex;
    private int brokenFieldIndex;
    private int nonValidBeanIndex;
    private int violatesDbConstraintIndex;
    private String markerFilename;

    public static UtilityConfiguration getInstance() {
        if (instance == null) {
            instance = init();
        }
        return instance;
    }

    private static UtilityConfiguration init() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(APP_PROPERTIES_PATH);
        Path root = Path.of(resourceBundle.getString(APP_ROOT));
        Path errors = Path.of(resourceBundle.getString(APP_ERRORS_PATH));
        int subFoldersCount = Integer.parseInt(resourceBundle.getString(SUB_FOLDERS_COUNT));
        int testTime = Integer.parseInt(resourceBundle.getString(TEST_TIME));
        int filesCount = Integer.parseInt(resourceBundle.getString(FILES_COUNT));
        String subFolderName = resourceBundle.getString(SUB_FOLDER_NAME);
        String markerFileName = resourceBundle.getString(MARKER_FILENAME);
        int periodTime = Integer.parseInt(resourceBundle.getString(PERIOD_TIME));
        int generatorDivider = Integer.parseInt(resourceBundle.getString(GENERATOR_DIVIDER));
        int wrongJsonFormatIndex = Integer.parseInt(resourceBundle.getString(WRONG_JSON_FORMAT_INDEX));
        int brokenFieldIndex = Integer.parseInt(resourceBundle.getString(BROKEN_FIELD_INDEX));
        int nonValidBeanIndex = Integer.parseInt(resourceBundle.getString(NON_VALID_BEAN_INDEX));
        int violatesDbConstraintIndex = Integer.parseInt(resourceBundle.getString(VIOLATES_DB_CONSTRAINT_INDEX));

        return new UtilityConfiguration(root, errors, subFoldersCount, testTime, filesCount, subFolderName, periodTime,
                generatorDivider, wrongJsonFormatIndex, brokenFieldIndex, nonValidBeanIndex, violatesDbConstraintIndex, markerFileName);
    }
}
