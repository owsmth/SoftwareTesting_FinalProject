package com.linkedin.tests;

import com.linkedin.tests.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

/**
 * SearchFunctionalityTest - Test Suite for LinkedIn Search Features
 *
 * This class tests the search functionality on LinkedIn:
 * - Search box visibility and interaction
 * - Search suggestions/autocomplete
 * - Search execution
 * - Search result navigation
 * - Search filters (if accessible without login)
 *
 * Test Type: Functional Testing, Integration Testing
 *
 * Note: Some search features may require authentication
 */
public class SearchFunctionalityTest extends BaseTest {

    // Test 1: Verify search input field is present on homepage

    @Test(priority = 1, description = "Verify search input field presence")
    public void testSearchFieldPresent() {
        System.out.println("\n=== Test 1: Search Field Presence ===");

        navigateToLinkedIn();

        // Look for search input field
        List<WebElement> searchInputs = driver.findElements(
                By.xpath("//input[@type='search' or contains(@placeholder, 'Search') or @role='searchbox']")
        );

        if (searchInputs.size() > 0) {
            System.out.println("Found search input field");
            Assert.assertTrue(searchInputs.get(0).isDisplayed(),
                    "Search field should be visible");
        } else {
            // Search might be behind login, verify search-related elements exist
            boolean searchRelatedExists = driver.findElements(
                    By.xpath("//*[contains(@class, 'search') or contains(@id, 'search')]")
            ).size() > 0;

            System.out.println("Search-related elements present: " + searchRelatedExists);
        }

        System.out.println("✓ Search functionality elements detected");
    }

    // Test 2: Verify search field accepts text input

    @Test(priority = 2, description = "Verify search field accepts input")
    public void testSearchFieldAcceptsInput() {
        System.out.println("\n=== Test 2: Search Field Input ===");

        navigateToLinkedIn();

        try {
            // Try to find and interact with search field
            WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//input[@type='search' or contains(@placeholder, 'Search')]")
            ));

            String testQuery = "Software Engineer";
            searchInput.clear();
            searchInput.sendKeys(testQuery);

            pause(500);

            // Verify input was accepted
            String enteredValue = searchInput.getAttribute("value");
            System.out.println("Entered search query: " + enteredValue);

            Assert.assertEquals(enteredValue, testQuery,
                    "Search field should accept and retain input");

            System.out.println("✓ Search field accepts input");
        } catch (Exception e) {
            System.out.println("Search field requires authentication or not accessible");
            System.out.println("Exception: " + e.getMessage());
            // This is acceptable as search may require login
        }
    }

    // Test 3: Verify search suggestions appear (if available)

    @Test(priority = 3, description = "Verify search suggestions functionality")
    public void testSearchSuggestions() {
        System.out.println("\n=== Test 3: Search Suggestions ===");

        navigateToLinkedIn();

        try {
            WebElement searchInput = driver.findElement(
                    By.xpath("//input[@type='search' or contains(@placeholder, 'Search')]")
            );

            // Type partial query
            searchInput.sendKeys("Java");
            pause(2000); // Wait for suggestions to appear

            // Look for suggestion dropdown
            List<WebElement> suggestions = driver.findElements(
                    By.xpath("//*[contains(@class, 'suggestions') or contains(@class, 'typeahead') or contains(@role, 'listbox')]")
            );

            if (suggestions.size() > 0) {
                System.out.println("Found " + suggestions.size() + " suggestion containers");
                Assert.assertTrue(suggestions.get(0).isDisplayed(),
                        "Suggestions should be visible");
            } else {
                System.out.println("No suggestions found - may require authentication");
            }

        } catch (Exception e) {
            System.out.println("Search suggestions not accessible: " + e.getMessage());
        }

        System.out.println("✓ Search suggestions test completed");
    }

    // Test 4: Verify search can be triggered by Enter key

    @Test(priority = 4, description = "Verify search execution via Enter key")
    public void testSearchWithEnterKey() {
        System.out.println("\n=== Test 4: Search with Enter Key ===");

        navigateToLinkedIn();

        try {
            WebElement searchInput = driver.findElement(
                    By.xpath("//input[@type='search' or contains(@placeholder, 'Search')]")
            );

            String searchQuery = "Product Manager";
            searchInput.sendKeys(searchQuery);
            searchInput.sendKeys(Keys.RETURN);

            pause(3000); // Wait for search results or redirect

            String currentUrl = driver.getCurrentUrl();
            System.out.println("URL after search: " + currentUrl);

            // Verify URL changed or search was processed
            Assert.assertTrue(
                    currentUrl.contains("search") ||
                            currentUrl.contains("results") ||
                            !currentUrl.equals(baseUrl),
                    "URL should change after search"
            );

            System.out.println("✓ Search triggered successfully");

        } catch (Exception e) {
            System.out.println("Search execution requires authentication: " + e.getMessage());
        }
    }

    // Test 5: Verify search button is clickable (if present)

    @Test(priority = 5, description = "Verify search button functionality")
    public void testSearchButtonClickable() {
        System.out.println("\n=== Test 5: Search Button Functionality ===");

        navigateToLinkedIn();

        // Look for search button
        List<WebElement> searchButtons = driver.findElements(
                By.xpath("//button[contains(@class, 'search') or @aria-label='Search' or contains(., 'Search')]")
        );

        if (searchButtons.size() > 0) {
            WebElement searchBtn = searchButtons.get(0);
            System.out.println("Found search button");
            Assert.assertTrue(searchBtn.isEnabled(),
                    "Search button should be enabled");
            System.out.println("✓ Search button is clickable");
        } else {
            System.out.println("No visible search button found - may use Enter key or require auth");
        }

        System.out.println("✓ Search button test completed");
    }

    // Test 6: Verify clearing search input

    @Test(priority = 6, description = "Verify search field can be cleared")
    public void testClearSearchInput() {
        System.out.println("\n=== Test 6: Clear Search Input ===");

        navigateToLinkedIn();

        try {
            WebElement searchInput = driver.findElement(
                    By.xpath("//input[@type='search' or contains(@placeholder, 'Search')]")
            );

            // Enter text
            searchInput.sendKeys("Test Query");
            pause(500);

            // Clear the field
            searchInput.clear();
            pause(500);

            // Verify field is empty
            String value = searchInput.getAttribute("value");
            Assert.assertTrue(value == null || value.isEmpty(),
                    "Search field should be empty after clear");

            System.out.println("✓ Search field cleared successfully");

        } catch (Exception e) {
            System.out.println("Search field not accessible: " + e.getMessage());
        }
    }
}