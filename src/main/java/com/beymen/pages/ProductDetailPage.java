package com.beymen.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;
import java.util.Random;

/**
 * Page Object for Product Detail Page
 * Following Page Object Pattern and OOP principles
 */
public class ProductDetailPage extends BasePage {
    
    // Web Elements
    @FindBy(css = ".o-productDetail__title, .product-title, h1, .pdp-product-name")
    private WebElement productTitle;
    
    @FindBy(css = ".m-price__new, .price, .product-price, .pdp-price")
    private WebElement productPrice;
    
    @FindBy(id = "addBasket")
    private WebElement addToCartButton;
    
    @FindBy(xpath = "//*[@id='sizes']/div[1]")
    private WebElement firstSizeOption;
    
    @FindBy(xpath = "//*[@id='sizes']")
    private WebElement sizesContainer;
    
    @FindBy(css = ".size-option, .size-selector, .variant-size, .size-button, .m-variantSize, .variant-option, .size-item, .product-size")
    private List<WebElement> sizeOptions;
    
    @FindBy(css = ".cart-icon, .sepet, .basket-icon, .header-cart")
    private WebElement cartIcon;
    
    // Pop-up elements after adding to cart
    @FindBy(css = ".popup, .modal, .overlay, .cart-popup, .sepet-popup")
    private WebElement addToCartPopup;
    
    @FindBy(xpath = "//div/button[2]")
    private WebElement popupGoToCartButton;
    
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
        try {
            return waitForElementVisible(productPrice).getText();
        } catch (Exception e) {
            logger.info("Price not visible or available");
            return "Price not available";
        }
    }
    
    /**
     * Close any modal or overlay that might be blocking elements
     */
    private void closeModalIfPresent() {
        try {
            // Try to find and close any modal/overlay
            String[] modalCloseSelectors = {
                ".modal .close", ".modal .btn-close", ".overlay .close",
                ".popup .close", ".o-modal .close", ".modal-close",
                "button[aria-label='Close']", "button[aria-label='Kapat']",
                ".fa-times", ".fa-close", ".icon-close"
            };
            
            for (String selector : modalCloseSelectors) {
                try {
                    java.util.List<WebElement> closeButtons = driver.findElements(org.openqa.selenium.By.cssSelector(selector));
                    for (WebElement closeButton : closeButtons) {
                        if (closeButton.isDisplayed()) {
                            clickElement(closeButton);
                            logger.info("Closed modal/overlay");
                            Thread.sleep(2000); // Set to 2 seconds
                            return;
                        }
                    }
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
            
            // Try pressing Escape key to close modal
            try {
                driver.findElement(org.openqa.selenium.By.tagName("body")).sendKeys(org.openqa.selenium.Keys.ESCAPE);
                Thread.sleep(2000); // Set to 2 seconds
                logger.info("Pressed ESC to close modal");
            } catch (Exception e) {
                // Continue
            }
            
        } catch (Exception e) {
            logger.info("No modal to close or modal closing failed");
        }
    }
    
    /**
     * Handle add to cart popup and click go to cart button
     * @return true if popup was handled successfully
     */
    private boolean handleAddToCartPopup() {
        try {
            // Try multiple selectors for the "Sepete Git" button in popup
            String[] goToCartSelectors = {
                ".go-to-cart", ".sepete-git", ".cart-button", 
                "button[class*='cart']", ".btn-cart", ".sepet-git-btn",
                ".popup .btn", ".modal .btn", ".popup button", ".modal button",
                "[href*='cart']", "[href*='sepet']", 
                "button:contains('Sepete')", "button:contains('Sepet')",
                "a:contains('Sepete')", "a:contains('Sepet')"
            };
            
            for (String selector : goToCartSelectors) {
                try {
                    java.util.List<WebElement> buttons = driver.findElements(org.openqa.selenium.By.cssSelector(selector));
                    for (WebElement button : buttons) {
                        if (button.isDisplayed() && button.isEnabled()) {
                            String buttonText = button.getText().toLowerCase();
                            if (buttonText.contains("sepet") || buttonText.contains("cart") || 
                                buttonText.contains("git") || buttonText.contains("go")) {
                                
                                scrollToElement(button);
                                clickElement(button);
                                logger.info("Clicked 'Sepete Git' button in popup: " + button.getText());
                                Thread.sleep(2000); // Set to 2 seconds
                                
                                // Verify we're on cart page
                                String currentUrl = driver.getCurrentUrl();
                                if (currentUrl.contains("cart") || currentUrl.contains("sepet")) {
                                    logger.info("Successfully navigated to cart page: " + currentUrl);
                                    return true;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    // Continue to next selector
                }
            }
            
            logger.info("No 'Sepete Git' button found in popup");
            return false;
        } catch (Exception e) {
            logger.error("Failed to handle add to cart popup: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Add product to cart
     * @return CartPage
     */
    public CartPage addToCart() {
        try {
            // Close any modal that might be blocking the button first
            closeModalIfPresent();
            
            // Check if sizes are available and select if they exist
            boolean sizeSelected = checkAndSelectSizeIfAvailable();
            
            // Try to use the specific addBasket button ID provided by user
            boolean addToCartSuccess = false;
            try {
                if (addToCartButton.isDisplayed() && addToCartButton.isEnabled()) {
                    scrollToElement(addToCartButton);
                    
                    // Try multiple click methods
                    try {
                        clickElement(addToCartButton);
                        logger.info("Added product to cart using normal click on addBasket button" + 
                                  (sizeSelected ? " (with size selected)" : " (no size required)"));
                        addToCartSuccess = true;
                    } catch (Exception e) {
                        logger.info("Normal click failed, trying JavaScript click on addBasket button");
                        try {
                            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", addToCartButton);
                            logger.info("Added product to cart using JavaScript click on addBasket button" + 
                                      (sizeSelected ? " (with size selected)" : " (no size required)"));
                            addToCartSuccess = true;
                        } catch (Exception ex) {
                            logger.info("JavaScript click also failed on addBasket button: " + ex.getMessage());
                        }
                    }
                } else {
                    logger.warn("addBasket button not available or not enabled");
                }
            } catch (Exception e) {
                logger.info("addBasket button not found: " + e.getMessage());
            }

            if (!addToCartSuccess) {
                logger.info("Specific addBasket button failed, trying comprehensive search");
                
                // Fallback to comprehensive search if specific ID doesn't work
                WebElement addButton = findAddToCartButton();
                
                if (addButton == null) {
                    logger.error("Add to cart button not found");
                    throw new RuntimeException("Could not find add to cart button");
                }
                
                scrollToElement(addButton);
                
                // Try multiple click methods on fallback button
                try {
                    clickElement(addButton);
                    logger.info("Added product to cart using normal click on fallback button" + 
                              (sizeSelected ? " (with size selected)" : " (no size required)"));
                } catch (Exception e) {
                    logger.info("Normal click failed on fallback button, trying JavaScript click");
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", addButton);
                    logger.info("Added product to cart using JavaScript click on fallback button" + 
                              (sizeSelected ? " (with size selected)" : " (no size required)"));
                }
            }
            
            // Wait for popup to appear
            Thread.sleep(2000);
            
            // Handle the popup using the specific XPath provided
            boolean popupHandled = handleAddToCartPopupWithSpecificXPath();
            
            if (!popupHandled) {
                // If specific popup handling failed, try alternative navigation
                logger.info("Specific popup not found, trying alternative navigation to cart");
                try {
                    clickElement(cartIcon);
                    logger.info("Clicked cart icon to go to cart");
                } catch (Exception e) {
                    // If cart icon click fails, navigate directly to cart URL
                    logger.info("Cart icon not found, navigating directly to cart URL");
                    driver.get("https://www.beymen.com/tr/cart");
                }
            }
            
            Thread.sleep(2000);
            return new CartPage(driver);
        } catch (Exception e) {
            logger.error("Failed to add product to cart: " + e.getMessage());
            throw new RuntimeException("Could not add product to cart");
        }
    }
    
    /**
     * Check if sizes are available and select one if they exist
     * @return true if size was selected, false if no sizes available
     */
    private boolean checkAndSelectSizeIfAvailable() {
        try {
            Thread.sleep(2000); // Set to 2 seconds
            
            logger.info("Checking if product has sizes that need to be selected");
            
            // First check if the sizes container exists using the specific XPath
            try {
                if (sizesContainer.isDisplayed()) {
                    logger.info("Sizes container found - product REQUIRES size selection");
                    
                    // Try to click the first size option using the provided XPath
                    try {
                        if (firstSizeOption.isDisplayed() && firstSizeOption.isEnabled()) {
                            scrollToElement(firstSizeOption);
                            clickElement(firstSizeOption);
                            
                            String sizeText = firstSizeOption.getText();
                            logger.info("SUCCESS: Selected first size using provided XPath: '" + sizeText + "'");
                            Thread.sleep(1000); // Brief wait to ensure size is selected
                            return true;
                        }
                    } catch (Exception e) {
                        logger.info("First size option not clickable: " + e.getMessage());
                    }
                    
                    // If first size not clickable, try to find other clickable sizes in the container
                    try {
                        java.util.List<WebElement> sizesInContainer = sizesContainer.findElements(org.openqa.selenium.By.xpath(".//div"));
                        logger.info("Found " + sizesInContainer.size() + " div elements in sizes container");
                        
                        for (int i = 0; i < sizesInContainer.size(); i++) {
                            WebElement sizeOption = sizesInContainer.get(i);
                            if (sizeOption.isDisplayed() && sizeOption.isEnabled()) {
                                try {
                                    String sizeText = sizeOption.getText().trim();
                                    
                                    // Skip if it's a size chart link or contains unwanted text
                                    if (sizeText.toLowerCase().contains("tablosu") || 
                                        sizeText.toLowerCase().contains("chart") || 
                                        sizeText.toLowerCase().contains("guide") ||
                                        sizeText.isEmpty()) {
                                        logger.info("Skipping size option " + (i+1) + " (chart/guide): '" + sizeText + "'");
                                        continue;
                                    }
                                    
                                    logger.info("Attempting to select size option " + (i+1) + ": '" + sizeText + "'");
                                    
                                    scrollToElement(sizeOption);
                                    clickElement(sizeOption);
                                    
                                    logger.info("SUCCESS: Selected size '" + sizeText + "' at position " + (i+1));
                                    Thread.sleep(1000); // Brief wait to ensure size is selected
                                    return true;
                                } catch (Exception e) {
                                    logger.info("Could not click size option " + (i+1) + ": " + e.getMessage());
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.info("Error finding sizes in container: " + e.getMessage());
                    }
                    
                    // Try alternative approach - look for clickable elements within sizes container
                    try {
                        java.util.List<WebElement> clickableElements = sizesContainer.findElements(
                            org.openqa.selenium.By.xpath(".//*[self::div or self::button or self::span][not(contains(@class, 'disabled'))]"));
                        logger.info("Found " + clickableElements.size() + " potentially clickable elements in sizes container");
                        
                        for (int i = 0; i < clickableElements.size(); i++) {
                            WebElement element = clickableElements.get(i);
                            if (element.isDisplayed() && element.isEnabled()) {
                                try {
                                    String elementText = element.getText().trim();
                                    String elementClass = element.getAttribute("class");
                                    
                                    // Skip unwanted elements
                                    if (elementText.toLowerCase().contains("tablosu") || 
                                        elementText.toLowerCase().contains("chart") || 
                                        elementText.toLowerCase().contains("guide") ||
                                        (elementClass != null && elementClass.toLowerCase().contains("disabled"))) {
                                        continue;
                                    }
                                    
                                    logger.info("Trying clickable element " + (i+1) + ": '" + elementText + "', class: '" + elementClass + "'");
                                    
                                    scrollToElement(element);
                                    clickElement(element);
                                    
                                    logger.info("SUCCESS: Selected size via clickable element: '" + elementText + "'");
                                    Thread.sleep(1000);
                                    return true;
                                } catch (Exception e) {
                                    logger.info("Could not click element " + (i+1) + ": " + e.getMessage());
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.info("Error finding clickable elements in sizes container: " + e.getMessage());
                    }
                    
                    logger.error("CRITICAL: Sizes container found but NO SELECTABLE size options - this will cause add to cart to fail!");
                    return false;
                }
            } catch (Exception e) {
                logger.info("Sizes container not found using specific XPath: " + e.getMessage());
            }
            
            // Check if there are any other size selectors on the page using broader search
            try {
                String[] sizeSelectors = {
                    "#sizes", "#sizes div", "#sizes button", "#sizes span",
                    ".size-option", ".size-selector", ".variant-size", ".size-button", 
                    ".m-variantSize", ".variant-option", ".size-item", ".product-size",
                    ".beden", ".sizes", "[data-size]", ".size-list div", ".size-list button"
                };
                
                for (String selector : sizeSelectors) {
                    try {
                        java.util.List<WebElement> sizeElements = driver.findElements(org.openqa.selenium.By.cssSelector(selector));
                        
                        if (!sizeElements.isEmpty()) {
                            logger.info("Found " + sizeElements.size() + " size elements with selector: " + selector);
                            
                            for (WebElement sizeElement : sizeElements) {
                                if (sizeElement.isDisplayed() && sizeElement.isEnabled()) {
                                    try {
                                        String sizeText = sizeElement.getText().trim();
                                        String sizeClass = sizeElement.getAttribute("class");
                                        
                                        // Skip size chart links and disabled elements
                                        if (sizeText.toLowerCase().contains("tablosu") || 
                                            sizeText.toLowerCase().contains("chart") || 
                                            sizeText.toLowerCase().contains("guide") ||
                                            (sizeClass != null && sizeClass.toLowerCase().contains("disabled")) ||
                                            sizeText.isEmpty()) {
                                            continue;
                                        }
                                        
                                        logger.info("Attempting to select alternative size: '" + sizeText + "' with selector: " + selector);
                                        
                                        scrollToElement(sizeElement);
                                        clickElement(sizeElement);
                                        
                                        logger.info("SUCCESS: Selected alternative size: '" + sizeText + "'");
                                        Thread.sleep(1000);
                                        return true;
                                    } catch (Exception e) {
                                        logger.info("Could not click alternative size element: " + e.getMessage());
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        // Continue to next selector
                    }
                }
            } catch (Exception e) {
                logger.info("Error in alternative size search: " + e.getMessage());
            }
            
            logger.info("No sizes found - product can be added to cart directly without size selection");
            return false;
            
        } catch (Exception e) {
            logger.error("Error checking for sizes: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Handle add to cart popup using the specific XPath provided: //div/button[2]
     * @return true if popup was handled successfully
     */
    private boolean handleAddToCartPopupWithSpecificXPath() {
        try {
            // First try the specific popup button XPath provided by user
            try {
                if (popupGoToCartButton.isDisplayed() && popupGoToCartButton.isEnabled()) {
                    scrollToElement(popupGoToCartButton);
                    clickElement(popupGoToCartButton);
                    logger.info("Clicked popup 'Sepete Git' button using specific XPath: //div/button[2]");
                    Thread.sleep(2000);
                    
                    // Verify we're on cart page
                    String currentUrl = driver.getCurrentUrl();
                    if (currentUrl.contains("cart") || currentUrl.contains("sepet")) {
                        logger.info("Successfully navigated to cart page: " + currentUrl);
                        return true;
                    }
                }
            } catch (Exception e) {
                logger.info("Specific popup button XPath failed: " + e.getMessage());
            }
            
            // Fallback to original popup handling if specific XPath doesn't work
            logger.info("Falling back to comprehensive popup button search");
            return handleAddToCartPopup();
            
        } catch (Exception e) {
            logger.error("Failed to handle add to cart popup with specific XPath: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Find add to cart button with comprehensive search
     * @return WebElement of add to cart button or null if not found
     */
    private WebElement findAddToCartButton() {
        // First, let's debug the specific addBasket button
        try {
            WebElement addBasketBtn = driver.findElement(org.openqa.selenium.By.id("addBasket"));
            if (addBasketBtn != null) {
                logger.info("DEBUG: Found addBasket button - displayed: " + addBasketBtn.isDisplayed() + 
                           ", enabled: " + addBasketBtn.isEnabled() + 
                           ", text: '" + addBasketBtn.getText() + "'");
                if (addBasketBtn.isDisplayed() && addBasketBtn.isEnabled()) {
                    return addBasketBtn;
                }
            }
        } catch (Exception e) {
            logger.info("DEBUG: addBasket button not found by ID: " + e.getMessage());
        }
        
        // Try multiple selectors for add to cart button
        String[] addToCartSelectors = {
            // Start with the specific ID
            "#addBasket",
            
            // Common patterns
            ".m-addBasketFavorite__basket", ".add-to-cart", ".sepete-ekle", 
            "button[class*='basket']", "button[class*='cart']", ".btn-add-bag",
            ".add-to-basket", "button[class*='sepet']", ".sepet-ekle-btn", 
            ".basket-add", ".cart-add",
            
            // More specific patterns
            ".o-productDetail__addBasket", ".m-productDetail__addToCart",
            ".product-add-cart", ".product-add-basket", ".pdp-add-cart",
            ".btn-add-to-cart", ".btn-add-to-basket", ".btn-sepet-ekle",
            
            // Generic button patterns that might be add to cart
            "button[id*='cart']", "button[id*='basket']", "button[id*='sepet']",
            "button[data-action*='cart']", "button[data-action*='basket']",
            "button[onclick*='cart']", "button[onclick*='basket']"
        };
        
        for (String selector : addToCartSelectors) {
            try {
                java.util.List<WebElement> buttons = driver.findElements(org.openqa.selenium.By.cssSelector(selector));
                logger.info("DEBUG: Selector '" + selector + "' found " + buttons.size() + " buttons");
                
                for (WebElement button : buttons) {
                    if (button.isDisplayed() && button.isEnabled()) {
                        String buttonText = button.getText().toLowerCase();
                        String buttonClass = button.getAttribute("class");
                        String buttonId = button.getAttribute("id");
                        
                        logger.info("Found potential add to cart button: text='" + button.getText() + 
                                   "', class='" + buttonClass + "', id='" + buttonId + 
                                   "', displayed=" + button.isDisplayed() + ", enabled=" + button.isEnabled());
                        
                        // Check if it's likely an add to cart button
                        if (buttonText.contains("sepete") || buttonText.contains("cart") || 
                            buttonText.contains("basket") || buttonText.contains("ekle") ||
                            (buttonClass != null && (buttonClass.contains("cart") || buttonClass.contains("basket") || buttonClass.contains("sepet"))) ||
                            (buttonId != null && (buttonId.contains("cart") || buttonId.contains("basket") || buttonId.contains("sepet") || buttonId.equals("addBasket")))) {
                            
                            logger.info("Selected add to cart button: " + button.getText());
                            return button;
                        }
                    }
                }
            } catch (Exception e) {
                logger.info("Error with selector " + selector + ": " + e.getMessage());
                // Continue to next selector
            }
        }
        
        // If no specific add to cart button found, let's list all buttons for debugging
        try {
            java.util.List<WebElement> allButtons = driver.findElements(org.openqa.selenium.By.tagName("button"));
            logger.info("DEBUG: Found " + allButtons.size() + " total buttons on page");
            
            int debugCount = 0;
            for (WebElement button : allButtons) {
                if (button.isDisplayed() && button.isEnabled() && debugCount < 10) {
                    String buttonText = button.getText().trim();
                    String buttonId = button.getAttribute("id");
                    String buttonClass = button.getAttribute("class");
                    
                    if (!buttonText.isEmpty() || (buttonId != null && !buttonId.isEmpty())) {
                        logger.info("DEBUG Button " + debugCount + ": text='" + buttonText + 
                                   "', id='" + buttonId + "', class='" + buttonClass + "'");
                        debugCount++;
                    }
                }
            }
        } catch (Exception e) {
            logger.info("Error listing all buttons: " + e.getMessage());
        }
        
        logger.warn("No add to cart button found with any selector");
        return null;
    }
    
    /**
     * Go to cart by clicking cart icon
     * @return CartPage
     */
    public CartPage goToCart() {
        try {
            clickElement(cartIcon);
            logger.info("Clicked cart icon to go to cart");
            return new CartPage(driver);
        } catch (Exception e) {
            logger.error("Failed to go to cart: " + e.getMessage());
            throw new RuntimeException("Could not navigate to cart");
        }
    }
} 