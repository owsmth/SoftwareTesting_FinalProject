package com.linkedin.tests.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

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
}