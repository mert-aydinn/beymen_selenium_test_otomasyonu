package com.beymen.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

/**
 * WebDriver Manager class following Singleton pattern
 * Manages WebDriver lifecycle and configuration
 */
public class DriverManager {
    
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final int IMPLICIT_WAIT_TIME = 10;
    private static final int PAGE_LOAD_TIMEOUT = 30;
    
    /**
     * Private constructor to prevent instantiation
     */
    private DriverManager() {
    }
    
    /**
     * Get WebDriver instance
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }
    
    /**
     * Initialize WebDriver based on browser type
     * @param browserName browser name (chrome, firefox, edge)
     */
    public static void initializeDriver(String browserName) {
        if (driverThreadLocal.get() == null) {
            WebDriver driver;
            
            switch (browserName.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                    chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
                    driver = new ChromeDriver(chromeOptions);
                    break;
                    
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driver = new FirefoxDriver();
                    break;
                    
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    driver = new EdgeDriver();
                    break;
                    
                default:
                    throw new IllegalArgumentException("Browser not supported: " + browserName);
            }
            
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_WAIT_TIME));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
            
            driverThreadLocal.set(driver);
            logger.info("WebDriver initialized for browser: " + browserName);
        }
    }
    
    /**
     * Initialize Chrome driver with default settings
     */
    public static void initializeDriver() {
        initializeDriver("chrome");
    }
    
    /**
     * Quit WebDriver and remove from ThreadLocal
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            logger.info("WebDriver quit successfully");
        }
    }
} 