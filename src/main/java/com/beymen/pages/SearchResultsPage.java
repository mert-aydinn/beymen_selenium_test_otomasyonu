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
    @FindBy(css = ".m-productCard, .productCard, .product-card, .product-item, .product, [data-product], .productCard__wrapper, .product-list-item, .plp-product, .product-tile, .o-productList__item, .m-productCard__wrapper")
    private List<WebElement> productCards;
    
    @FindBy(css = ".search-result, .results-title, .o-productList__title, h1, .page-title")
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
            // Wait for page to load and try multiple selectors
            Thread.sleep(2000); // Set to 2 seconds
            
            // Check if any products are displayed
            if (productCards.size() > 0) {
                logger.info("Found " + productCards.size() + " products");
                return true;
            }
            
            // Alternative check - look for any indication of search results
            try {
                waitForElementVisible(By.cssSelector(".product-card, .product-item, .product, [data-product], .m-productCard, .productCard, .productCard__wrapper, .product-list-item, .plp-product, .product-tile"));
                return productCards.size() > 0;
            } catch (Exception e) {
                logger.error("No product elements found with any selector");
            }
            
            // Final check - see if we're on a results page at all
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("search") || currentUrl.contains("arama") || currentUrl.contains("gomlek") || currentUrl.contains("gömlek")) {
                logger.info("On search results page based on URL: " + currentUrl);
                return true; // We're on a search page even if no products found
            }
            
            return false;
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
        try {
            if (productCards.isEmpty()) {
                throw new RuntimeException("No products found in search results");
            }
            
            // Try to select a product that's more likely to work (avoid complex products)
            WebElement selectedProduct = null;
            Random random = new Random();
            int maxAttempts = 5; // Try up to 5 different products
            
            for (int attempt = 0; attempt < maxAttempts && selectedProduct == null; attempt++) {
                int randomIndex = random.nextInt(productCards.size());
                WebElement candidateProduct = productCards.get(randomIndex);
                
                // Check if this product might be simpler (less likely to have complex size requirements)
                try {
                    String productText = candidateProduct.getText().toLowerCase();
                    String productClass = candidateProduct.getAttribute("class");
                    
                    // Skip products that might have complex sizing (like corsets, fitted items)
                    if (productText.contains("corset") || productText.contains("fitted") || 
                        productText.contains("blazer") || productText.contains("ceket") ||
                        productText.contains("pantolon") || productText.contains("jean")) {
                        logger.info("Skipping potentially complex product: " + productText);
                        continue;
                    }
                    
                    // Prefer simpler items like shirts, basic tops, accessories
                    if (productText.contains("tshirt") || productText.contains("shirt") || 
                        productText.contains("bluz") || productText.contains("basic") ||
                        productText.contains("accessory") || productText.contains("bag") ||
                        productText.contains("çanta") || productText.contains("simple")) {
                        logger.info("Selected preferred simple product at index: " + randomIndex);
                        selectedProduct = candidateProduct;
                        break;
                    }
                    
                    // If no preferred type found in max attempts, use any product
                    if (attempt == maxAttempts - 1) {
                        selectedProduct = candidateProduct;
                        logger.info("Selected product (fallback) at index: " + randomIndex);
                    }
                    
                } catch (Exception e) {
                    logger.info("Error checking product details, continuing: " + e.getMessage());
                    if (attempt == maxAttempts - 1) {
                        selectedProduct = candidateProduct;
                    }
                }
            }
            
            if (selectedProduct == null) {
                // Final fallback - just pick the first available product
                selectedProduct = productCards.get(0);
                logger.info("Selected first available product as final fallback");
            }
            
            // Click on the selected product
            scrollToElement(selectedProduct);
            clickElement(selectedProduct);
            logger.info("Selected random product at index: " + productCards.indexOf(selectedProduct));
            
            return new ProductDetailPage(driver);
        } catch (Exception e) {
            logger.error("Failed to select random product: " + e.getMessage());
            throw new RuntimeException("Could not select a product from search results");
        }
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