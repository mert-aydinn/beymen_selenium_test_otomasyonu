package com.beymen.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Reader utility class
 * Reads properties from configuration file
 */
public class ConfigReader {
    
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/test-data.properties";
    
    /**
     * Static block to load properties file once
     */
    static {
        try {
            properties = new Properties();
            InputStream inputStream = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(inputStream);
            inputStream.close();
            logger.info("Configuration file loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load configuration file: " + e.getMessage());
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }
    
    /**
     * Get property value by key
     * @param key property key
     * @return property value
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            logger.warn("Property not found: " + key);
        }
        return value;
    }
    
    /**
     * Get property value with default
     * @param key property key
     * @param defaultValue default value if property not found
     * @return property value or default
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Get integer property value
     * @param key property key
     * @return integer value
     */
    public static int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key));
    }
    
    /**
     * Get boolean property value
     * @param key property key
     * @return boolean value
     */
    public static boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
} 