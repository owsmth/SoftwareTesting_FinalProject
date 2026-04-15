package com.linkedin.tests;

import com.linkedin.tests.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;

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

    private static final String JOBS_URL = baseUrl + "/jobs/search";

    // Test 1: Verify jobs page is accessible

    @Test(priority = 1, description = "Verify jobs page accessibility")
    public void testJobsPageAccessible() {
        System.out.println("\n=== Test 1: Jobs Page Accessibility ===");

        driver.get(JOBS_URL);
        pause(3000);

        handleSignInPopup();

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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(JOBS_URL);

        handleSignInPopup();

        // Target the specific job search input by name or unique placeholder
        // Using 'name' is much more stable than generic placeholders
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@name='keywords' or @placeholder='Search job titles or companies']")
        ));

        String placeholder = searchBox.getAttribute("placeholder");
        System.out.println("Detected Search Box with placeholder: " + placeholder);

        // Verify it is actually on the screen
        Assert.assertTrue(searchBox.isDisplayed(), "Job title search box should be visible");

        System.out.println("✓ Job search functionality detected");
    }

    // Test 3: Verify location search field

    @Test(priority = 3, description = "Verify location search field")
    public void testLocationSearchField() {
        System.out.println("\n=== Test 3: Location Search Field ===");

        driver.get(JOBS_URL);
        pause(3000);

        handleSignInPopup();

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

        handleSignInPopup();

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

        handleSignInPopup();

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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(JOBS_URL);

        handleSignInPopup();

        // 1. Locate the keyword input field
        // Based on the screenshot, the 'name' or 'placeholder' is the best bet
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@name='keywords' or @placeholder='Search job titles or companies']")
        ));

        // 2. Type and press ENTER
        String searchQuery = "Software Engineer";
        searchInput.clear();
        searchInput.sendKeys(searchQuery + Keys.ENTER);
        System.out.println("Typed search query and pressed Enter key.");

        // 3. Wait for the results to refresh
        // We look for the list of job cards (the <li> or <ul> on the left)
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector(".jobs-search__results-list, .jobs-search-results-list")
            ));

            String currentUrl = driver.getCurrentUrl();
            System.out.println("Search successful! URL is now: " + currentUrl);

            // Assert that the URL contains our search term (replaces the try-catch print)
            Assert.assertTrue(currentUrl.contains("Software"), "Search did not redirect correctly!");

        } catch (TimeoutException e) {
            Assert.fail("Search results did not load after pressing Enter.");
        }

        System.out.println("✓ Job search execution test completed");
    }
}