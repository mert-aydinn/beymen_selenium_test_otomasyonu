package com.beymen.tests;

import com.beymen.utils.ConfigReader;
import com.beymen.utils.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Base Test class containing common test setup and teardown
 * Following OOP principles - all test classes will extend this
 */
public abstract class BaseTest {
    
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    
    /**
     * Setup method - runs before each test method
     */
    @BeforeMethod
    public void setUp() {
        logger.info("Starting test setup");
        String browserName = ConfigReader.getProperty("browser.name", "chrome");
        DriverManager.initializeDriver(browserName);
        logger.info("Test setup completed");
    }
    
    /**
     * Teardown method - runs after each test method
     */
    @AfterMethod
    public void tearDown() {
        logger.info("Starting test teardown");
        DriverManager.quitDriver();
        logger.info("Test teardown completed");
    }
} 