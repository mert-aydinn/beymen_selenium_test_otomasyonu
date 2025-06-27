package com.beymen.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.List;

/**
 * Base Page class that contains common functionality for all page objects
 * Following OOP principles - Encapsulation and Inheritance
 */
public abstract class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor jsExecutor;
    protected static final Logger logger = LogManager.getLogger(BasePage.class);
    private static final int DEFAULT_TIMEOUT = 10;
    
    /**
     * Constructor for BasePage
     * @param driver WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        this.jsExecutor = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Wait for element to be visible
     * @param element WebElement to wait for
     * @return WebElement after it becomes visible
     */
    protected WebElement waitForElementVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    /**
     * Wait for element to be visible by locator
     * @param locator By locator
     * @return WebElement after it becomes visible
     */
    protected WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be clickable
     * @param element WebElement to wait for
     * @return WebElement after it becomes clickable
     */
    protected WebElement waitForElementClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    /**
     * Click element with wait
     * @param element WebElement to click
     */
    protected void clickElement(WebElement element) {
        waitForElementClickable(element).click();
        logger.info("Clicked on element");
    }
    
    /**
     * Send keys to element with clear
     * @param element WebElement to send keys to
     * @param text Text to send
     */
    protected void sendKeys(WebElement element, String text) {
        WebElement visibleElement = waitForElementVisible(element);
        visibleElement.clear();
        visibleElement.sendKeys(text);
        logger.info("Entered text: " + text);
    }
    
    /**
     * Clear text from element
     * @param element WebElement to clear
     */
    protected void clearElement(WebElement element) {
        waitForElementVisible(element).clear();
        logger.info("Cleared element");
    }
    
    /**
     * Get all elements matching locator
     * @param locator By locator
     * @return List of WebElements
     */
    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }
    
    /**
     * Scroll to element using JavaScript
     * @param element WebElement to scroll to
     */
    protected void scrollToElement(WebElement element) {
        jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
    }
    
    /**
     * Check if page is loaded
     * @return true if page is loaded
     */
    public boolean isPageLoaded() {
        return jsExecutor.executeScript("return document.readyState").equals("complete");
    }
} 