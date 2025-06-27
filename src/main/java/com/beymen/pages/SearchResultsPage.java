package com.beymen.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Random;

/**
 * Page Object for Search Results Page
 * Following Page Object Pattern and OOP principles
 */
public class SearchResultsPage extends BasePage {
    
    // Web Elements
    @FindBy(css = ".m-productCard__photo")
    private List<WebElement> productCards;
    
    @FindBy(css = ".o-productList__title")
    private WebElement searchResultsTitle;
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if search results are displayed
     * @return true if search results are displayed
     */
    public boolean areSearchResultsDisplayed() {
        try {
            waitForElementVisible(By.cssSelector(".m-productCard__photo"));
            return productCards.size() > 0;
        } catch (Exception e) {
            logger.error("Search results are not displayed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get number of search results
     * @return number of products found
     */
    public int getSearchResultsCount() {
        return productCards.size();
    }
    
    /**
     * Select a random product from search results
     * @return ProductDetailPage
     */
    public ProductDetailPage selectRandomProduct() {
        if (productCards.isEmpty()) {
            throw new RuntimeException("No products found in search results");
        }
        
        Random random = new Random();
        int randomIndex = random.nextInt(productCards.size());
        WebElement randomProduct = productCards.get(randomIndex);
        
        scrollToElement(randomProduct);
        clickElement(randomProduct);
        logger.info("Selected random product at index: " + randomIndex);
        
        return new ProductDetailPage(driver);
    }
    
    /**
     * Select product by index
     * @param index product index (0-based)
     * @return ProductDetailPage
     */
    public ProductDetailPage selectProductByIndex(int index) {
        if (index >= productCards.size()) {
            throw new IndexOutOfBoundsException("Product index out of range");
        }
        
        WebElement product = productCards.get(index);
        scrollToElement(product);
        clickElement(product);
        logger.info("Selected product at index: " + index);
        
        return new ProductDetailPage(driver);
    }
} 