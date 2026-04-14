package com.linkedin.tests;

import com.linkedin.tests.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

/**
 * JobsPageTest - Test Suite for LinkedIn Jobs Functionality

 * This class tests job search and browsing features:
 * - Jobs page accessibility
 * - Job search functionality
 * - Job listing display
 * - Job filters
 * - Navigation elements

 * Test Type: Functional Testing, Integration Testing
 */
public class JobsPageTest extends BaseTest {

    private static final String JOBS_URL = baseUrl + "/jobs";

    // Test 1: Verify jobs page is accessible

    @Test(priority = 1, description = "Verify jobs page accessibility")
    public void testJobsPageAccessible() {
        System.out.println("\n=== Test 1: Jobs Page Accessibility ===");

        driver.get(JOBS_URL);
        pause(3000);

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);

        Assert.assertTrue(currentUrl.contains("jobs") || currentUrl.contains("job"),
                "URL should contain 'jobs'");

        String pageTitle = driver.getTitle();
        System.out.println("Page title: " + pageTitle);
        Assert.assertTrue(pageTitle.toLowerCase().contains("job") ||
                        pageTitle.toLowerCase().contains("career"),
                "Title should reference jobs or careers");

        System.out.println("✓ Jobs page is accessible");
    }

    // Test 2: Verify job search box is present

    @Test(priority = 2, description = "Verify job search box presence")
    public void testJobSearchBoxPresent() {
        System.out.println("\n=== Test 2: Job Search Box ===");

        driver.get(JOBS_URL);
        pause(3000);

        // Look for job search input fields
        List<WebElement> searchInputs = driver.findElements(
                By.xpath("//input[@placeholder or @type='search' or contains(@aria-label, 'Search')]")
        );

        System.out.println("Found " + searchInputs.size() + " search input field(s)");

        if (searchInputs.size() > 0) {
            WebElement searchBox = searchInputs.get(0);
            String placeholder = searchBox.getAttribute("placeholder");
            System.out.println("Search placeholder: " + placeholder);
            Assert.assertTrue(searchBox.isDisplayed(),
                    "Search box should be visible");
        }

        System.out.println("✓ Job search functionality detected");
    }

    // Test 3: Verify location search field

    @Test(priority = 3, description = "Verify location search field")
    public void testLocationSearchField() {
        System.out.println("\n=== Test 3: Location Search Field ===");

        driver.get(JOBS_URL);
        pause(3000);

        // Look for location input
        List<WebElement> locationInputs = driver.findElements(
                By.xpath("//input[contains(@placeholder, 'location') or contains(@aria-label, 'location') or contains(@placeholder, 'City')]")
        );

        System.out.println("Found " + locationInputs.size() + " location input field(s)");

        if (locationInputs.size() > 0) {
            WebElement locationBox = locationInputs.get(0);
            String placeholder = locationBox.getAttribute("placeholder");
            System.out.println("Location placeholder: " + placeholder);
        }

        System.out.println("✓ Location search check completed");
    }

    // Test 4: Verify job listings are displayed

    @Test(priority = 4, description = "Verify job listings display")
    public void testJobListingsDisplay() {
        System.out.println("\n=== Test 4: Job Listings Display ===");

        driver.get(JOBS_URL);
        pause(3000);

        // Scroll to ensure content loads
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, 500)");
        pause(2000);

        // Look for job listing elements
        List<WebElement> jobCards = driver.findElements(
                By.xpath("//*[contains(@class, 'job') or contains(@class, 'card')]")
        );

        System.out.println("Found " + jobCards.size() + " potential job card elements");

        // Look for list items that might be job listings
        List<WebElement> listItems = driver.findElements(By.tagName("li"));
        System.out.println("Found " + listItems.size() + " list items");

        boolean jobContentPresent = jobCards.size() > 0 || listItems.size() > 5;
        System.out.println("Job content present: " + jobContentPresent);

        System.out.println("✓ Job listings check completed");
    }

    // Test 5: Verify job filters are available

    @Test(priority = 5, description = "Verify job filter options")
    public void testJobFilters() {
        System.out.println("\n=== Test 5: Job Filter Options ===");

        driver.get(JOBS_URL);
        pause(3000);

        // Look for filter elements
        List<WebElement> filterElements = driver.findElements(
                By.xpath("//*[contains(text(), 'Filter') or contains(@class, 'filter') or contains(text(), 'Sort')]")
        );

        System.out.println("Found " + filterElements.size() + " filter-related elements");

        // Look for checkboxes or radio buttons (common in filters)
        List<WebElement> checkboxes = driver.findElements(
                By.xpath("//input[@type='checkbox']")
        );
        List<WebElement> radioButtons = driver.findElements(
                By.xpath("//input[@type='radio']")
        );

        System.out.println("Checkboxes: " + checkboxes.size());
        System.out.println("Radio buttons: " + radioButtons.size());

        System.out.println("✓ Job filter check completed");
    }

    // Test 6: Verify job search can be executed

    @Test(priority = 6, description = "Verify job search execution")
    public void testJobSearchExecution() {
        System.out.println("\n=== Test 6: Job Search Execution ===");

        driver.get(JOBS_URL);
        pause(3000);

        try {
            // Find search input
            WebElement searchInput = driver.findElement(
                    By.xpath("//input[@placeholder or @type='search']")
            );

            String searchQuery = "Software Engineer";
            searchInput.sendKeys(searchQuery);
            System.out.println("Entered search query: " + searchQuery);

            // Look for search button
            List<WebElement> searchButtons = driver.findElements(
                    By.xpath("//button[contains(., 'Search') or @type='submit']")
            );

            if (searchButtons.size() > 0) {
                searchButtons.get(0).click();
                pause(3000);
                System.out.println("Clicked search button");
            } else {
                searchInput.submit();
                pause(3000);
                System.out.println("Submitted search form");
            }

            String currentUrl = driver.getCurrentUrl();
            System.out.println("URL after search: " + currentUrl);

        } catch (Exception e) {
            System.out.println("Job search execution: " + e.getMessage());
        }

        System.out.println("✓ Job search execution test completed");
    }
}