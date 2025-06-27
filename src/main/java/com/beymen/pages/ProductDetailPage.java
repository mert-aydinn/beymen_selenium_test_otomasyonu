package com.beymen.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for Product Detail Page
 * Following Page Object Pattern and OOP principles
 */
public class ProductDetailPage extends BasePage {
    
    // Web Elements
    @FindBy(css = ".o-productDetail__title")
    private WebElement productTitle;
    
    @FindBy(css = ".m-price__new")
    private WebElement productPrice;
    
    @FindBy(css = ".m-addBasketFavorite__basket")
    private WebElement addToCartButton;
    
    /**
     * Constructor
     * @param driver WebDriver instance
     */
    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if product detail page is displayed
     * @return true if product detail page is displayed
     */
    public boolean isProductDetailPageDisplayed() {
        try {
            return waitForElementVisible(productTitle).isDisplayed();
        } catch (Exception e) {
            logger.error("Product detail page is not displayed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get product title
     * @return product title text
     */
    public String getProductTitle() {
        return waitForElementVisible(productTitle).getText();
    }
    
    /**
     * Get product price
     * @return product price text
     */
    public String getProductPrice() {
        return waitForElementVisible(productPrice).getText();
    }
    
    /**
     * Add product to cart
     */
    public void addToCart() {
        clickElement(addToCartButton);
        logger.info("Added product to cart");
    }
} 