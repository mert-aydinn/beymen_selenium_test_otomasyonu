package com.beymen.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Beymen Home Page
 * Following Page Object Pattern and OOP principles
 */
public class HomePage extends BasePage {
    
    // Page URL
    private static final String HOME_PAGE_URL = "https://www.beymen.com";
    
    // Web Elements using @FindBy annotation
    @FindBy(css = "input[placeholder='Ürün, Marka Arayın']")
    private WebElement searchBox;
    
    @FindBy(css = ".o-header__logo")
    private WebElement beymenLogo;
    
    @FindBy(css = "button[id='onetrust-accept-btn-handler']")
    private WebElement acceptCookiesButton;
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public HomePage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Navigate to home page
     */
    public void navigateToHomePage() {
        driver.get(HOME_PAGE_URL);
        logger.info("Navigated to Beymen home page");
        handleCookies();
    }
    
    /**
     * Check if home page is displayed
     * @return true if home page is displayed
     */
    public boolean isHomePageDisplayed() {
        try {
            return waitForElementVisible(beymenLogo).isDisplayed() && 
                   driver.getCurrentUrl().contains("beymen.com");
        } catch (Exception e) {
            logger.error("Home page is not displayed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Enter search term in search box
     * @param searchTerm term to search
     */
    public void enterSearchTerm(String searchTerm) {
        sendKeys(searchBox, searchTerm);
        logger.info("Entered search term: " + searchTerm);
    }
    
    /**
     * Clear search box
     */
    public void clearSearchBox() {
        clearElement(searchBox);
        logger.info("Cleared search box");
    }
    
    /**
     * Press Enter key in search box
     * @return SearchResultsPage
     */
    public SearchResultsPage pressEnterInSearchBox() {
        searchBox.sendKeys(Keys.ENTER);
        logger.info("Pressed Enter key in search box");
        return new SearchResultsPage(driver);
    }
    
    /**
     * Perform search with given term
     * @param searchTerm term to search
     * @return SearchResultsPage
     */
    public SearchResultsPage searchForProduct(String searchTerm) {
        enterSearchTerm(searchTerm);
        return pressEnterInSearchBox();
    }
    
    /**
     * Handle cookie consent if present
     */
    private void handleCookies() {
        try {
            if (acceptCookiesButton.isDisplayed()) {
                clickElement(acceptCookiesButton);
                logger.info("Accepted cookies");
            }
        } catch (Exception e) {
            logger.info("Cookie consent not displayed or already accepted");
        }
    }
} 