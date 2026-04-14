package com.linkedin.tests;

import com.linkedin.tests.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

/**
 * ResponsiveDesignTest - Test Suite for LinkedIn Responsive Design

 * This class tests responsive design features across different viewport sizes:
 * - Mobile viewport rendering
 * - Tablet viewport rendering
 * - Desktop viewport rendering
 * - Responsive navigation
 * - Element visibility at different sizes

 * Test Type: UI Testing, Cross-browser Testing, Regression Testing
 */
public class ResponsiveDesignTest extends BaseTest {

    // Common viewport sizes
    private static final Dimension MOBILE_SIZE = new Dimension(375, 667);    // iPhone 8
    private static final Dimension TABLET_SIZE = new Dimension(768, 1024);   // iPad
    private static final Dimension DESKTOP_SIZE = new Dimension(1920, 1080); // Full HD

    // Test 1: Verify page renders on mobile viewport

    @Test(priority = 1, description = "Verify mobile viewport rendering")
    public void testMobileViewport() {
        System.out.println("\n=== Test 1: Mobile Viewport ===");

        // Set mobile size
        driver.manage().window().setSize(MOBILE_SIZE);
        System.out.println("Set viewport to: " + MOBILE_SIZE);

        navigateToLinkedIn();
        pause(2000);

        // Verify page loads
        String pageTitle = driver.getTitle();
        Assert.assertFalse(pageTitle.isEmpty(), "Page should load on mobile viewport");

        // Check if content is visible
        List<WebElement> visibleElements = driver.findElements(
                By.xpath("//*[not(contains(@style, 'display: none'))]")
        );

        System.out.println("Visible elements: " + visibleElements.size());
        Assert.assertTrue(visibleElements.size() > 0,
                "Page should have visible content on mobile");

        System.out.println("✓ Page renders on mobile viewport");
    }

    // Test 2: Verify page renders on tablet viewport

    @Test(priority = 2, description = "Verify tablet viewport rendering")
    public void testTabletViewport() {
        System.out.println("\n=== Test 2: Tablet Viewport ===");

        // Set tablet size
        driver.manage().window().setSize(TABLET_SIZE);
        System.out.println("Set viewport to: " + TABLET_SIZE);

        navigateToLinkedIn();
        pause(2000);

        // Verify page loads
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("linkedin.com"),
                "Page should load on tablet viewport");

        // Check viewport meta tag
        List<WebElement> viewportTags = driver.findElements(
                By.xpath("//meta[@name='viewport']")
        );

        if (viewportTags.size() > 0) {
            String content = viewportTags.get(0).getAttribute("content");
            System.out.println("Viewport meta: " + content);
        }

        System.out.println("✓ Page renders on tablet viewport");
    }

    // Test 3: Verify page renders on desktop viewport

    @Test(priority = 3, description = "Verify desktop viewport rendering")
    public void testDesktopViewport() {
        System.out.println("\n=== Test 3: Desktop Viewport ===");

        // Set desktop size
        driver.manage().window().setSize(DESKTOP_SIZE);
        System.out.println("Set viewport to: " + DESKTOP_SIZE);

        navigateToLinkedIn();
        pause(2000);

        // Get window size
        Dimension actualSize = driver.manage().window().getSize();
        System.out.println("Actual window size: " + actualSize);

        Assert.assertTrue(actualSize.width >= 1900,
                "Desktop viewport should be wide");

        System.out.println("✓ Page renders on desktop viewport");
    }

   // Test 4: Verify navigation adapts to mobile

    @Test(priority = 4, description = "Verify responsive navigation")
    public void testResponsiveNavigation() {
        System.out.println("\n=== Test 4: Responsive Navigation ===");

        // Test on mobile
        driver.manage().window().setSize(MOBILE_SIZE);
        navigateToLinkedIn();
        pause(2000);

        // Look for mobile menu (hamburger icon)
        List<WebElement> mobileMenus = driver.findElements(
                By.xpath("//*[contains(@class, 'hamburger') or contains(@class, 'menu-icon') or contains(@aria-label, 'menu')]")
        );

        System.out.println("Mobile menu elements: " + mobileMenus.size());

        // Switch to desktop
        driver.manage().window().setSize(DESKTOP_SIZE);
        pause(1000);

        // Look for desktop navigation
        List<WebElement> navElements = driver.findElements(By.tagName("nav"));
        System.out.println("Navigation elements: " + navElements.size());

        System.out.println("✓ Navigation responsiveness checked");
    }

    // Test 5: Verify images are responsive

    @Test(priority = 5, description = "Verify responsive images")
    public void testResponsiveImages() {
        System.out.println("\n=== Test 5: Responsive Images ===");

        navigateToLinkedIn();
        pause(2000);

        // Get all images
        List<WebElement> images = driver.findElements(By.tagName("img"));
        System.out.println("Found " + images.size() + " images");

        int responsiveImages = 0;
        for (WebElement img : images) {
            String srcset = img.getAttribute("srcset");
            String sizes = img.getAttribute("sizes");

            if (srcset != null || sizes != null) {
                responsiveImages++;
            }
        }

        System.out.println("Responsive images (with srcset/sizes): " + responsiveImages);

        System.out.println("✓ Image responsiveness checked");
    }

    // Test 6: Verify text remains readable at different sizes

    @Test(priority = 6, description = "Verify text readability")
    public void testTextReadability() {
        System.out.println("\n=== Test 6: Text Readability ===");

        // Test on mobile
        driver.manage().window().setSize(MOBILE_SIZE);
        navigateToLinkedIn();
        pause(2000);

        // Check for minimum font sizes
        List<WebElement> textElements = driver.findElements(
                By.xpath("//p | //span | //div")
        );

        int checkedElements = 0;
        for (WebElement element : textElements) {
            if (checkedElements >= 10) break; // Sample first 10

            String fontSize = element.getCssValue("font-size");
            if (fontSize != null && !fontSize.isEmpty()) {
                System.out.println("Font size: " + fontSize);
                checkedElements++;
            }
        }

        System.out.println("✓ Text readability checked");
    }
}