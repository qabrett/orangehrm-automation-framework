package com.orangehrm.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    private Constants() {
        // utility class — do not instantiate
    }

    public static final int DEFAULT_WAIT_TIME = 10;
    public static final String FILE_PATH = "./src/test/resources/config/FilePath.json";

    //this keeps track of execution details
    public static Map<String, String> testExecutionDetails = new HashMap<>();


    public static Map<String, Object> filePathMap = new HashMap<>();
    public static Map<String, Object> environmentMap = new HashMap<>();

}

