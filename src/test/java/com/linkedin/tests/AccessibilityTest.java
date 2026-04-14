package com.linkedin.tests;

import com.linkedin.tests.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

/**
 * AccessibilityTest - Test Suite for LinkedIn Accessibility Features

 * This class tests web accessibility compliance:
 * - ARIA attributes
 * - Keyboard navigation
 * - Alt text for images
 * - Form labels
 * - Skip links
 * - Focus management

 * Test Type: Accessibility Testing, Compliance Testing, Regression Testing
 */
public class AccessibilityTest extends BaseTest {

    // Test 1: Verify page has proper lang attribute

    @Test(priority = 1, description = "Verify lang attribute on HTML element")
    public void testLangAttribute() {
        System.out.println("\n=== Test 1: Lang Attribute ===");

        navigateToLinkedIn();

        // Get html element
        WebElement htmlElement = driver.findElement(By.tagName("html"));
        String langAttr = htmlElement.getAttribute("lang");

        System.out.println("Page language: " + langAttr);

        Assert.assertNotNull(langAttr, "HTML element should have lang attribute");
        Assert.assertFalse(langAttr.isEmpty(), "Lang attribute should not be empty");

        System.out.println("✓ Page has proper lang attribute");
    }

   // Test 2: Verify images have alt text

    @Test(priority = 2, description = "Verify images have alt text")
    public void testImageAltText() {
        System.out.println("\n=== Test 2: Image Alt Text ===");

        navigateToLinkedIn();
        pause(2000);

        List<WebElement> images = driver.findElements(By.tagName("img"));
        System.out.println("Found " + images.size() + " images");

        int imagesWithAlt = 0;
        int decorativeImages = 0;

        for (WebElement img : images) {
            String alt = img.getAttribute("alt");
            String role = img.getAttribute("role");

            if (alt != null) {
                imagesWithAlt++;
                if (alt.isEmpty() && "presentation".equals(role)) {
                    decorativeImages++;
                }
            }
        }

        System.out.println("Images with alt attribute: " + imagesWithAlt);
        System.out.println("Decorative images (empty alt): " + decorativeImages);

        // Most images should have alt text
        double altPercentage = (double) imagesWithAlt / images.size() * 100;
        System.out.println("Alt text coverage: " + String.format("%.1f%%", altPercentage));

        Assert.assertTrue(altPercentage > 70,
                "At least 70% of images should have alt attributes");

        System.out.println("✓ Images have appropriate alt text");
    }

    // Test 3: Verify form inputs have labels

    @Test(priority = 3, description = "Verify form inputs have labels")
    public void testFormLabels() {
        System.out.println("\n=== Test 3: Form Input Labels ===");

        navigateToLinkedIn();
        pause(2000);

        // Find all input fields
        List<WebElement> inputs = driver.findElements(
                By.xpath("//input[@type='text' or @type='email' or @type='password']")
        );

        System.out.println("Found " + inputs.size() + " input fields");

        int labeledInputs = 0;
        for (WebElement input : inputs) {
            String ariaLabel = input.getAttribute("aria-label");
            String ariaLabelledBy = input.getAttribute("aria-labelledby");
            String id = input.getAttribute("id");

            // Check if input has associated label
            boolean hasLabel = false;

            if (ariaLabel != null && !ariaLabel.isEmpty()) {
                hasLabel = true;
                System.out.println("Input has aria-label: " + ariaLabel);
            } else if (ariaLabelledBy != null) {
                hasLabel = true;
                System.out.println("Input has aria-labelledby: " + ariaLabelledBy);
            } else if (id != null && !id.isEmpty()) {
                // Check for <label for="id">
                List<WebElement> labels = driver.findElements(
                        By.xpath("//label[@for='" + id + "']")
                );
                if (labels.size() > 0) {
                    hasLabel = true;
                    System.out.println("Input has label element");
                }
            }

            if (hasLabel) labeledInputs++;
        }

        System.out.println("Labeled inputs: " + labeledInputs + "/" + inputs.size());

        if (inputs.size() > 0) {
            double labelPercentage = (double) labeledInputs / inputs.size() * 100;
            Assert.assertTrue(labelPercentage > 60,
                    "Most form inputs should have labels");
        }

        System.out.println("✓ Form inputs have appropriate labels");
    }

    // Test 4: Verify buttons have accessible names

    @Test(priority = 4, description = "Verify buttons have accessible names")
    public void testButtonAccessibleNames() {
        System.out.println("\n=== Test 4: Button Accessible Names ===");

        navigateToLinkedIn();
        pause(2000);

        List<WebElement> buttons = driver.findElements(By.tagName("button"));
        System.out.println("Found " + buttons.size() + " buttons");

        int accessibleButtons = 0;
        for (int i = 0; i < Math.min(buttons.size(), 20); i++) {
            WebElement button = buttons.get(i);

            String text = button.getText();
            String ariaLabel = button.getAttribute("aria-label");
            String ariaLabelledBy = button.getAttribute("aria-labelledby");

            boolean hasAccessibleName =
                    (text != null && !text.trim().isEmpty()) ||
                            (ariaLabel != null && !ariaLabel.isEmpty()) ||
                            (ariaLabelledBy != null && !ariaLabelledBy.isEmpty());

            if (hasAccessibleName) {
                accessibleButtons++;
            }
        }

        System.out.println("Accessible buttons (sampled): " + accessibleButtons + "/20");

        Assert.assertTrue(accessibleButtons > 15,
                "Most buttons should have accessible names");

        System.out.println("✓ Buttons have accessible names");
    }

    // Test 5: Verify heading hierarchy

    @Test(priority = 5, description = "Verify heading hierarchy")
    public void testHeadingHierarchy() {
        System.out.println("\n=== Test 5: Heading Hierarchy ===");

        navigateToLinkedIn();
        pause(2000);

        // Count headings by level
        int h1Count = driver.findElements(By.tagName("h1")).size();
        int h2Count = driver.findElements(By.tagName("h2")).size();
        int h3Count = driver.findElements(By.tagName("h3")).size();
        int h4Count = driver.findElements(By.tagName("h4")).size();

        System.out.println("H1: " + h1Count);
        System.out.println("H2: " + h2Count);
        System.out.println("H3: " + h3Count);
        System.out.println("H4: " + h4Count);

        // Page should have at least one H1
        Assert.assertTrue(h1Count >= 1, "Page should have at least one H1");

        // Should not have too many H1s
        Assert.assertTrue(h1Count <= 3, "Page should not have too many H1 elements");

        System.out.println("✓ Heading hierarchy is reasonable");
    }
}