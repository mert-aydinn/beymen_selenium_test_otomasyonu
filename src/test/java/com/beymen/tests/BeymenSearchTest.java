package com.beymen.tests;

import com.beymen.pages.HomePage;
import com.beymen.pages.SearchResultsPage;
import com.beymen.utils.ConfigReader;
import com.beymen.utils.DriverManager;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Beymen Search Test Class
 * Implements the test scenario for searching products on Beymen.com
 */
public class BeymenSearchTest extends BaseTest {
    
    @Test(description = "Test searching for products on Beymen.com")
    public void testProductSearch() {
        // Get parameterized search terms from properties file
        String firstSearchTerm = ConfigReader.getProperty("search.term.first");
        String secondSearchTerm = ConfigReader.getProperty("search.term.second");
        
        logger.info("Starting Beymen search test");
        
        // Step 1: Open www.beymen.com
        HomePage homePage = new HomePage(DriverManager.getDriver());
        homePage.navigateToHomePage();
        
        // Step 2: Verify that home page is opened
        Assert.assertTrue(homePage.isHomePageDisplayed(), 
            "Home page should be displayed");
        logger.info("Home page is displayed successfully");
        
        // Step 3: Enter "kazak" in search box
        homePage.enterSearchTerm(firstSearchTerm);
        logger.info("Entered first search term: " + firstSearchTerm);
        
        // Step 4: Clear the search box
        homePage.clearSearchBox();
        logger.info("Cleared search box");
        
        // Step 5: Enter "gömlek" in search box
        homePage.enterSearchTerm(secondSearchTerm);
        logger.info("Entered second search term: " + secondSearchTerm);
        
        // Step 6: Press Enter key
        SearchResultsPage searchResultsPage = homePage.pressEnterInSearchBox();
        logger.info("Pressed Enter key to search");
        
        // Verify search results are displayed
        Assert.assertTrue(searchResultsPage.areSearchResultsDisplayed(), 
            "Search results should be displayed");
        logger.info("Search results are displayed");
        
        // Step 7: Select a random product from results
        searchResultsPage.selectRandomProduct();
        logger.info("Selected a random product from search results");
        
        logger.info("Beymen search test completed successfully");
    }
} 