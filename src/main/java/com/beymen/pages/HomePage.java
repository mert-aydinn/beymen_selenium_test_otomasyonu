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
    
    // Web Elements using @FindBy annotation with multiple fallback options
    @FindBy(xpath = "//*[@id='o-searchSuggestion__input']")
    private WebElement searchBox;
    
    @FindBy(css = "#o-searchSuggestion__input")
    private WebElement searchBoxCSS;
    
    @FindBy(css = "input[placeholder*='Ürün, Marka Arayın'], input[placeholder*='ürün'], input[placeholder*='arayın']")
    private WebElement searchBoxPlaceholder;
    
    @FindBy(css = ".autocomplete-input, .search-input")
    private WebElement searchBoxClass;
    
    @FindBy(css = "button[type='submit'], .search-button, button.search-submit, .header-search button, .search-form button")
    private WebElement searchButton;
    
    @FindBy(css = ".o-header__logo, .logo, .header-logo, [alt*='Beymen'], [alt*='beymen']")
    private WebElement beymenLogo;
    
    @FindBy(css = "button[id='onetrust-accept-btn-handler'], button[id*='accept'], button[class*='accept']")
    private WebElement acceptCookiesButton;
    
    @FindBy(css = ".gender-popup, .popup-close, .close-button, [aria-label*='close'], [aria-label*='kapat'], .modal-close, button[class*='close']")
    private WebElement genderPopupCloseButton;
    
    @FindBy(css = ".gender-popup, .gender-modal, .popup-overlay, .modal-overlay, [class*='gender'], [class*='popup']")
    private WebElement genderPopup;
    
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
     * Find search box using multiple locator strategies
     * @return WebElement of search box
     */
    private WebElement findSearchBox() {
        WebElement[] searchElements = {searchBox, searchBoxCSS, searchBoxPlaceholder, searchBoxClass};
        String[] descriptions = {"XPath", "CSS ID", "Placeholder", "Class"};
        
        for (int i = 0; i < searchElements.length; i++) {
            try {
                if (searchElements[i].isDisplayed()) {
                    logger.info("Found search box using " + descriptions[i] + " locator");
                    return searchElements[i];
                }
            } catch (Exception e) {
                logger.warn("Search box not found with " + descriptions[i] + " locator: " + e.getMessage());
            }
        }
        
        // If none of the @FindBy elements work, try direct driver search
        try {
            java.util.List<org.openqa.selenium.WebElement> inputs = driver.findElements(org.openqa.selenium.By.tagName("input"));
            for (org.openqa.selenium.WebElement input : inputs) {
                if (input.isDisplayed()) {
                    String placeholder = input.getAttribute("placeholder");
                    String id = input.getAttribute("id");
                    String className = input.getAttribute("class");
                    
                    if ((placeholder != null && (placeholder.toLowerCase().contains("arayın") || placeholder.toLowerCase().contains("ürün"))) ||
                        (id != null && id.contains("search")) ||
                        (className != null && (className.contains("search") || className.contains("autocomplete")))) {
                        logger.info("Found search box using dynamic search");
                        return input;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Dynamic search box finding failed: " + e.getMessage());
        }
        
        throw new RuntimeException("Could not find search box with any locator strategy");
    }
    
    /**
     * Click on search box to activate it
     */
    public void clickSearchBox() {
        try {
            WebElement searchElement = findSearchBox();
            clickElement(searchElement);
            logger.info("Clicked on search box");
        } catch (Exception e) {
            logger.error("Failed to click search box: " + e.getMessage());
            throw new RuntimeException("Could not click search box");
        }
    }
    
    /**
     * Enter search term in search box
     * @param searchTerm term to search
     */
    public void enterSearchTerm(String searchTerm) {
        try {
            WebElement searchElement = findSearchBox();
            searchElement.clear();
            searchElement.sendKeys(searchTerm);
            logger.info("Entered search term: " + searchTerm);
            
            // Verify the text was entered correctly
            String actualValue = searchElement.getAttribute("value");
            logger.info("Search box value after input: '" + actualValue + "'");
        } catch (Exception e) {
            logger.error("Failed to enter search term: " + e.getMessage());
            throw new RuntimeException("Could not enter search term: " + searchTerm);
        }
    }
    
    /**
     * Clear search box
     */
    public void clearSearchBox() {
        try {
            WebElement searchElement = findSearchBox();
            searchElement.clear();
            searchElement.sendKeys(Keys.CONTROL + "a");
            searchElement.sendKeys(Keys.DELETE);
            logger.info("Cleared search box");
            
            // Verify it's actually cleared
            String actualValue = searchElement.getAttribute("value");
            logger.info("Search box value after clearing: '" + actualValue + "'");
        } catch (Exception e) {
            logger.error("Failed to clear search box: " + e.getMessage());
            throw new RuntimeException("Could not clear search box");
        }
    }
    
    /**
     * Complete search workflow: click, enter first term, clear, enter second term, press enter
     * @param firstTerm first search term to enter and clear
     * @param secondTerm second search term to search for
     * @return SearchResultsPage
     */
    public SearchResultsPage performCompleteSearch(String firstTerm, String secondTerm) {
        try {
            // Step 1: Click on search box
            clickSearchBox();
            Thread.sleep(2000); // Set to 2 seconds
            
            // Step 2: Enter first search term
            enterSearchTerm(firstTerm);
            Thread.sleep(2000); // Set to 2 seconds
            
            // Step 3: Clear the search box
            clearSearchBox();
            Thread.sleep(2000); // Set to 2 seconds
            
            // Step 4: Enter second search term
            enterSearchTerm(secondTerm);
            Thread.sleep(2000); // Set to 2 seconds
            
            // Step 5: Press Enter to search
            WebElement searchElement = findSearchBox();
            searchElement.sendKeys(Keys.ENTER);
            logger.info("Pressed Enter key to search for: " + secondTerm);
            
            return new SearchResultsPage(driver);
        } catch (Exception e) {
            logger.error("Failed to perform complete search: " + e.getMessage());
            throw new RuntimeException("Could not perform search workflow");
        }
    }
    
    /**
     * Click search button directly
     * @return SearchResultsPage
     */
    public SearchResultsPage clickSearchButton() {
        clickElement(searchButton);
        logger.info("Clicked search button");
        return new SearchResultsPage(driver);
    }
    
    /**
     * Handle cookie consent if present
     */
    private void handleCookies() {
        try {
            if (acceptCookiesButton.isDisplayed()) {
                clickElement(acceptCookiesButton);
                logger.info("Accepted cookies");
                handleGenderPopup();
            }
        } catch (Exception e) {
            logger.info("Cookie consent not displayed or already accepted");
            handleGenderPopup();
        }
    }
    
    /**
     * Handle gender selection popup if present
     */
    private void handleGenderPopup() {
        try {
            Thread.sleep(2000); // Set to 2 seconds
            
            if (genderPopup.isDisplayed()) {
                clickElement(genderPopupCloseButton);
                logger.info("Closed gender selection popup");
            }
        } catch (Exception e) {
            logger.info("Gender popup not displayed or already closed");
        }
    }
} 