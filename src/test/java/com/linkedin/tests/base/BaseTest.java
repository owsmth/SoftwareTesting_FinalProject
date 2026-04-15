package com.linkedin.tests.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.time.Duration;
/**
 * BaseTest - Foundation class for all LinkedIn test classes
 * This class provides:
 * - WebDriver initialization and cleanup
 * - Common configuration and utilities
 * - Implicit and explicit wait management
 * - Browser options and settings
 *
 * All test classes extend this base class to inherit common functionality.
 */
public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static String baseUrl = "https://www.linkedin.com";

    // Test data - In production, these would be in property files or encrypted
    protected static final String TEST_EMAIL = "your_test_email@example.com";
    protected static final String TEST_PASSWORD = "your_test_password";

    /**
     * Suite-level setup - Runs once before all tests
     * Configures WebDriverManager for automatic driver management
     */
    @BeforeSuite(alwaysRun = true)
    public void suiteSetup() {
        System.out.println("=== LinkedIn Test Suite Starting ===");
        System.out.println("Setting up ChromeDriver using WebDriverManager...");
        WebDriverManager.chromedriver().setup();
        System.out.println("ChromeDriver configured successfully\n");
    }

    /**
     * Class-level setup - Runs once before each test class
     */
    @BeforeClass(alwaysRun = true)
    public void classSetup() {
        System.out.println("Initializing test class: " + this.getClass().getSimpleName());
    }

    /**
     * Method-level setup - Runs before each test method
     * Initializes WebDriver with optimal settings for LinkedIn automation
     */
    @BeforeMethod(alwaysRun = true)

    public void methodSetup() {
        System.out.println("\n--- Starting new test method ---");

        // Configure Chrome options for stable automation
        ChromeOptions options = new ChromeOptions();

        // Add arguments to avoid bot detection and improve stability
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--start-maximized");


        // Remove automation flags that LinkedIn might detect
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);

        // Set user agent to appear as regular browser
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/147.0.0.0 Safari/537.36");
        // Uncomment for headless mode (faster but can't see browser)
        // options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // Initialize explicit wait with 15 second timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        System.out.println("Browser initialized and ready");

    }


    /**
     * Method-level teardown - Runs after each test method
     */
    @AfterMethod(alwaysRun = true)
    public void methodTeardown() {
        System.out.println("Cleaning up after test method");
        if (driver != null) {
            driver.quit();
            System.out.println("Browser closed successfully");
        }
    }

    /**
     * Class-level teardown - Runs once after all tests in the class
     */
    @AfterClass(alwaysRun = true)
    public void classTeardown() {
        System.out.println("Completed all tests in class: " + this.getClass().getSimpleName());
    }

    /**
     * Suite-level teardown - Runs once after all tests
     */
    @AfterSuite(alwaysRun = true)
    public void suiteTeardown() {
        System.out.println("\n=== LinkedIn Test Suite Completed ===");
    }

    /**
     * Utility method to add delays for demonstration or to avoid rate limiting
     */
    protected void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Utility method to navigate to LinkedIn homepage
     */
    protected void navigateToLinkedIn() {
        driver.get(baseUrl);
        System.out.println("Navigated to: " + baseUrl);
    }

    public void handleSignInPopup() {
        System.out.println("Scanning for guest popups...");
        // 1. Give LinkedIn's JavaScript a moment to trigger the modal
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        // 2. Define multiple possible XPaths for the 'X' button
        String[] xpaths = {
                "//*[local-name()='svg']/*[contains(@d, 'M20,5.32L13.32')]//ancestor::button", // Your SVG path
                "//button[@aria-label='Dismiss']",
                "//button[contains(@class, 'modal__dismiss')]",
                "//button[contains(@class, 'contextual-sign-in-modal__modal-dismiss')]"
        };

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        for (int attempt = 1; attempt <= 3; attempt++) {
            for (String xpath : xpaths) {
                try {
                    // Find and ensure it's clickable
                    WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));

                    // Force the click via JavaScript (ignores overlays)
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", closeButton);

                    // 3. VERIFICATION: Wait to see if it actually disappeared
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));

                    System.out.println("✓ Popup closed successfully on attempt " + attempt + " using XPath: " + xpath);
                    return; // Exit function immediately upon success
                } catch (Exception e) {
                    // If this XPath fails, the loop continues to the next one
                }
            }
            // Small pause between retry attempts if the first batch of XPaths didn't work
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        }
        System.out.println("No active popup found after 3 attempts.");
    }
}