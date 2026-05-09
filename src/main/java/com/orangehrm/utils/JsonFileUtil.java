package com.orangehrm.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class for reading, writing, and querying JSON data files.
 */
public class JsonFileUtil {

    private static final Logger log = LogManager.getLogger(JsonFileUtil.class);

    private Map<String, Map<String, Object>> fileDataMaps;
    private Map<String, Object> filteredObjMap;

    /**
     * Loads JSON file data into a Map and optionally filters by a key.
     *
     * @param filePath the path to the JSON file
     * @param filterBy the key to filter by, or null/"NONE" to load all
     * @return a Map containing the loaded JSON data
     * @throws RuntimeException if the file cannot be found or parsed
     */
    public Map<String, Object> loadJsonFileDataToMap(String filePath, String filterBy) {
        try {
            JsonObject jsonObject = JsonParser.parseReader(new FileReader(filePath)).getAsJsonObject();
            Gson gson = new Gson();
            fileDataMaps = gson.fromJson(jsonObject, Map.class);

            if (filterBy != null && !filterBy.equals("NONE")) {
                log.debug("Filtering JSON data by key: {}", filterBy);
                filteredObjMap = fileDataMaps.get(filterBy);
            } else {
                log.debug("Loading all JSON data — no filter applied");
                filteredObjMap = new LinkedHashMap<>();
                filteredObjMap.putAll(fileDataMaps);

            }

            log.debug("Loaded filtered map: {}", filteredObjMap);
            return filteredObjMap;

        } catch (FileNotFoundException e) {
            String message = "JSON file not found: " + filePath;
            log.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    /**
     * Writes test data to a JSON file.
     *
     * @param filePath the path to the JSON file to write
     * @param testData the data to write
     * @throws RuntimeException if the file cannot be written
     */
    public void updateJsonFileData(String filePath, Map<String, Object> testData) {
        try (Writer writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(testData, writer);
            log.debug("JSON file updated: {}", filePath);
        } catch (IOException e) {
            String message = "Error writing JSON file: " + filePath;
            log.error(message, e);
            throw new RuntimeException(message, e);
        }
    }

    /**
     * Filters the loaded map by the specified key.
     *
     * @param filterBy the key to filter by
     * @return the filtered Map
     * @throws IllegalStateException if no data has been loaded yet
     * @throws IllegalArgumentException if the filter key is not found
     */
    public Map<String, Object> applyFilter(String filterBy) {
        if (fileDataMaps == null) {
            throw new IllegalStateException("No JSON data loaded. Call loadJsonFileDataToMap first.");
        }
        filteredObjMap = fileDataMaps.get(filterBy);
        if (filteredObjMap == null) {
            String message = "Filter key not found in JSON data: " + filterBy;
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        log.debug("Applied filter '{}': {}", filterBy, filteredObjMap);
        return filteredObjMap;
    }

    /**
     * Returns the string value for the given key in the filtered map.
     *
     * @param key the key to look up
     * @return the string value
     * @throws IllegalStateException if no filtered data is available
     * @throws IllegalArgumentException if the key is not found
     */
    public String getFilteredValues(Object key) {
        if (filteredObjMap == null) {
            throw new IllegalStateException("No filtered data available. Call loadJsonFileDataToMap or applyFilter first.");
        }
        Object value = filteredObjMap.get(key);
        if (value == null) {
            String message = "Key not found in filtered map: " + key;
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return value.toString();
    }

    /**
     * Returns the string value for the given key in the provided map.
     *
     * @param map the map to look up the key in
     * @param key the key to look up
     * @return the string value
     * @throws IllegalArgumentException if the key is not found
     */
    public String getKey(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            String message = "Key not found in map: " + key;
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return value.toString();
    }

    /**
     * Reads a JSON file and returns its contents as a String.
     *
     * @param filePath the path to the JSON file
     * @return the file contents as a UTF-8 string
     * @throws RuntimeException if the file cannot be read
     */
    public String jsonToStringConversion(String filePath) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(filePath));
            return new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            String message = "Error reading JSON file: " + filePath;
            log.error(message, e);
            throw new RuntimeException(message, e);
        }
    }
}
