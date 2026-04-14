package com.linkedin.tests;

import com.linkedin.tests.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

/**
 * ProfilePageTest - Test Suite for LinkedIn Profile Pages

 * This class tests public profile page functionality:
 * - Profile URL structure
 * - Public profile information display
 * - Profile sections visibility
 * - Profile navigation

 * Test Type: Integration Testing, System Testing

 * Note: Tests public profiles which don't require authentication
 */

public class ProfilePageTest extends BaseTest {

    private static final String SAMPLE_PROFILE_URL = baseUrl + "/in/williamhgates";

    // Test 1: Verify public profile page is accessible

    @Test(priority = 1, description = "Verify public profile accessibility")
    public void testPublicProfileAccessible() {
        System.out.println("\n=== Test 1: Public Profile Accessibility ===");

        driver.get(SAMPLE_PROFILE_URL);
        pause(2000);

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentUrl);

        // Verify we're on a profile page
        Assert.assertTrue(currentUrl.contains("/in/"),
                "URL should contain /in/ for profile pages");

        // Check page title
        String pageTitle = driver.getTitle();
        System.out.println("Page title: " + pageTitle);
        Assert.assertFalse(pageTitle.isEmpty(), "Page should have a title");

        System.out.println("✓ Public profile is accessible");
    }

    // Test 2: Verify profile header section is present

    @Test(priority = 2, description = "Verify profile header section")
    public void testProfileHeaderPresent() {
        System.out.println("\n=== Test 2: Profile Header Section ===");

        driver.get(SAMPLE_PROFILE_URL);
        pause(2000);

        // Look for profile header elements
        List<WebElement> headings = driver.findElements(By.tagName("h1"));
        System.out.println("Found " + headings.size() + " H1 headings");

        if (headings.size() > 0) {
            String headingText = headings.get(0).getText();
            System.out.println("Main heading: " + headingText);
            Assert.assertFalse(headingText.isEmpty(),
                    "Profile should have a name/heading");
        }

        // Check for profile image
        List<WebElement> images = driver.findElements(
                By.xpath("//img[contains(@alt, 'profile') or contains(@class, 'profile')]")
        );
        System.out.println("Found " + images.size() + " profile-related images");

        System.out.println("✓ Profile header elements present");
    }

    // Test 3: Verify profile sections are visible

    @Test(priority = 3, description = "Verify profile sections visibility")
    public void testProfileSectionsVisible() {
        System.out.println("\n=== Test 3: Profile Sections ===");

        driver.get(SAMPLE_PROFILE_URL);
        pause(3000);

        // Look for section headings
        List<WebElement> h2Headings = driver.findElements(By.tagName("h2"));
        System.out.println("Found " + h2Headings.size() + " section headings");

        for (WebElement heading : h2Headings) {
            String text = heading.getText();
            if (!text.isEmpty()) {
                System.out.println("Section: " + text);
            }
        }

        Assert.assertTrue(h2Headings.size() > 0,
                "Profile should have section headings");

        System.out.println("✓ Profile sections are visible");
    }

    // Test 4: Verify profile URL follows correct format

    @Test(priority = 4, description = "Verify profile URL format")
    public void testProfileUrlFormat() {
        System.out.println("\n=== Test 4: Profile URL Format ===");

        driver.get(SAMPLE_PROFILE_URL);
        pause(2000);

        String currentUrl = driver.getCurrentUrl();
        System.out.println("Profile URL: " + currentUrl);

        // Verify URL structure
        Assert.assertTrue(currentUrl.contains("linkedin.com/in/"),
                "Profile URL should follow linkedin.com/in/ pattern");

        // Verify URL has a username/identifier
        String[] urlParts = currentUrl.split("/in/");
        if (urlParts.length > 1) {
            String identifier = urlParts[1].split("/")[0];
            System.out.println("Profile identifier: " + identifier);
            Assert.assertFalse(identifier.isEmpty(),
                    "Profile should have an identifier");
        }

        System.out.println("✓ Profile URL format is correct");
    }

    // Test 5: Verify profile has meta tags for social sharing

    @Test(priority = 5, description = "Verify profile meta tags")
    public void testProfileMetaTags() {
        System.out.println("\n=== Test 5: Profile Meta Tags ===");

        driver.get(SAMPLE_PROFILE_URL);
        pause(2000);

        // Check for Open Graph tags
        List<WebElement> ogTags = driver.findElements(
                By.xpath("//meta[starts-with(@property, 'og:')]")
        );

        System.out.println("Found " + ogTags.size() + " Open Graph meta tags");

        for (WebElement tag : ogTags) {
            String property = tag.getAttribute("property");
            String content = tag.getAttribute("content");
            System.out.println(property + ": " + content);
        }

        Assert.assertTrue(ogTags.size() > 0,
                "Profile should have Open Graph meta tags");

        System.out.println("✓ Profile meta tags present");
    }
}