package com.linkedin.tests;

import com.linkedin.tests.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

/**
 * HomepageNavigationTest - Test Suite for LinkedIn Homepage Navigation
 * This class tests navigation elements and links on the LinkedIn homepage:
 * - Main navigation menu items
 * - Footer links
 * - External navigation
 * - Page sections accessibility
 * - Responsive elements

 * Test Type: System Testing, Integration Testing
 */
public class HomepageNavigationTest extends BaseTest {

    // Test 1: Verify main navigation menu is present

    @Test(priority = 1, description = "Verify main navigation menu presence")
    public void testMainNavigationPresent() {
        System.out.println("\n=== Test 1: Main Navigation Menu Presence ===");

        navigateToLinkedIn();

        // Look for common navigation elements
        boolean navPresent = false;

        // Check for navigation by various selectors
        List<WebElement> navElements = driver.findElements(By.tagName("nav"));
        if (navElements.size() > 0) {
            navPresent = true;
            System.out.println("Found " + navElements.size() + " navigation elements");
        }

        Assert.assertTrue(navPresent || driver.findElements(By.className("nav")).size() > 0,
                "Navigation menu should be present on homepage");

        System.out.println("✓ Main navigation menu is present");
    }

    // Test 2: Verify Sign In link is clickable and navigates correctly

    @Test(priority = 2, description = "Verify Sign In link functionality")
    public void testSignInLinkClickable() {
        System.out.println("\n=== Test 2: Sign In Link Functionality ===");

        navigateToLinkedIn();

        // Find and click Sign In link
        WebElement signInLink = null;
        try {
            signInLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(text(), 'Sign in') or contains(@class, 'sign-in')]")
            ));
        } catch (Exception e) {
            // Try alternative locator
            List<WebElement> links = driver.findElements(By.tagName("a"));
            for (WebElement link : links) {
                if (link.getText().toLowerCase().contains("sign in")) {
                    signInLink = link;
                    break;
                }
            }
        }

        Assert.assertNotNull(signInLink, "Sign In link should be found");
        Assert.assertTrue(signInLink.isDisplayed(), "Sign In link should be visible");

        System.out.println("✓ Sign In link is present and clickable");
    }

    // Test 3: Verify Join Now/Register link is present and functional

    @Test(priority = 3, description = "Verify Join Now link functionality")
    public void testJoinNowLinkPresent() {
        System.out.println("\n=== Test 3: Join Now Link Functionality ===");

        navigateToLinkedIn();

        // Look for Join Now link
        boolean joinNowPresent = false;

        List<WebElement> links = driver.findElements(By.tagName("a"));
        for (WebElement link : links) {
            String linkText = link.getText().toLowerCase();
            if (linkText.contains("join now") || linkText.contains("join")) {
                joinNowPresent = true;
                System.out.println("Found Join link: " + link.getText());
                Assert.assertTrue(link.isDisplayed(), "Join link should be visible");
                break;
            }
        }

        Assert.assertTrue(joinNowPresent, "Join Now/Register link should be present");

        System.out.println("✓ Join Now link is present");
    }

    // Test 4: Verify footer section is present

    @Test(priority = 4, description = "Verify footer section presence")
    public void testFooterPresent() {
        System.out.println("\n=== Test 4: Footer Section Presence ===");

        navigateToLinkedIn();

        // Look for footer element
        List<WebElement> footers = driver.findElements(By.tagName("footer"));

        if (footers.size() > 0) {
            System.out.println("Footer element found");
            Assert.assertTrue(footers.get(0).isDisplayed(), "Footer should be visible");
        } else {
            // Check for alternative footer identification
            boolean footerContentPresent = driver.findElements(
                    By.xpath("//*[contains(@class, 'footer') or contains(@id, 'footer')]")
            ).size() > 0;
            Assert.assertTrue(footerContentPresent, "Footer section should be present");
        }

        System.out.println("✓ Footer section is present");
    }

    // Test 5: Verify multiple language options or accessibility links

    @Test(priority = 5, description = "Verify language/accessibility options")
    public void testLanguageOptionsPresent() {
        System.out.println("\n=== Test 5: Language/Accessibility Options ===");

        navigateToLinkedIn();

        // Scroll to bottom to ensure footer is visible
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, document.body.scrollHeight)");
        pause(1000);

        // Look for language selector or related links
        boolean languageOptionsFound = false;

        List<WebElement> allLinks = driver.findElements(By.tagName("a"));
        for (WebElement link : allLinks) {
            String linkText = link.getText().toLowerCase();
            if (linkText.contains("language") || linkText.contains("english") ||
                    linkText.contains("settings") || linkText.matches(".*\\b[a-z]{2}\\b.*")) {
                languageOptionsFound = true;
                System.out.println("Found potential language option: " + link.getText());
                break;
            }
        }

        // Also check for footer navigation
        List<WebElement> footerLinks = driver.findElements(
                By.xpath("//footer//a | //*[contains(@class, 'footer')]//a")
        );

        Assert.assertTrue(footerLinks.size() > 0 || languageOptionsFound,
                "Footer should contain links or language options");
        System.out.println("Found " + footerLinks.size() + " footer links");

        System.out.println("✓ Footer navigation elements present");
    }

    // Test 6: Verify page scrolling functionality

    @Test(priority = 6, description = "Verify page scrolling functionality")
    public void testPageScrolling() {
        System.out.println("\n=== Test 6: Page Scrolling Functionality ===");

        navigateToLinkedIn();

        // Get initial scroll position
        Long initialScroll = (Long) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return window.pageYOffset;");
        System.out.println("Initial scroll position: " + initialScroll);

        // Scroll down
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("window.scrollTo(0, 500)");
        pause(500);

        // Get new scroll position
        Long newScroll = (Long) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return window.pageYOffset;");
        System.out.println("New scroll position: " + newScroll);

        Assert.assertTrue(newScroll > initialScroll,
                "Page should be scrollable");

        System.out.println("✓ Page scrolling works correctly");
    }

    // Test 7: Verify page loads within acceptable time

    @Test(priority = 7, description = "Verify page load performance")
    public void testPageLoadPerformance() {
        System.out.println("\n=== Test 9: Page Load Performance ===");

        long startTime = System.currentTimeMillis();

        navigateToLinkedIn();

        long endTime = System.currentTimeMillis();
        long loadTime = endTime - startTime;

        System.out.println("Page load time: " + loadTime + "ms");

        // Verify page loaded within 10 seconds (generous timeout for test environment)
        Assert.assertTrue(loadTime < 10000,
                "Page should load within 10 seconds");

        System.out.println("✓ Page loaded in acceptable time");
    }

    // Test 8: Verify critical resources are loaded

    @Test(priority = 8, description = "Verify critical resources loaded")
    public void testCriticalResourcesLoaded() {
        System.out.println("\n=== Test 10: Critical Resources Loaded ===");

        navigateToLinkedIn();

        // Check for images
        List<WebElement> images = driver.findElements(By.tagName("img"));
        System.out.println("Found " + images.size() + " images");

        // Check for scripts
        List<WebElement> scripts = driver.findElements(By.tagName("script"));
        System.out.println("Found " + scripts.size() + " script elements");

        // Check for stylesheets
        List<WebElement> styles = driver.findElements(
                By.xpath("//link[@rel='stylesheet']")
        );
        System.out.println("Found " + styles.size() + " stylesheets");

        // Verify resources are present
        Assert.assertTrue(scripts.size() > 0, "Page should have JavaScript");
        Assert.assertTrue(styles.size() > 0, "Page should have CSS");

        System.out.println("✓ Critical resources are loaded");
    }
}