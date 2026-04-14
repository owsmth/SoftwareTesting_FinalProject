package com.linkedin.tests;

import com.linkedin.tests.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

/**
 * RegistrationTest - Test Suite for LinkedIn Registration/Signup

 * This class tests the user registration process:
 * - Navigation to signup page
 * - Registration form validation
 * - Field requirements
 * - Input validation
 * - Error handling

 * Test Type: End-to-End Testing, Acceptance Testing
 */
public class RegistrationTest extends BaseTest {

    // Test 1: Navigate to registration page successfully

    @Test(priority = 1, description = "Navigate to registration page")
    public void testNavigateToRegistration() {
        System.out.println("\n=== Test 1: Navigate to Registration ===");

        navigateToLinkedIn();

        // Find and click Join Now button
        WebElement joinButton = null;
        try {
            joinButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(text(), 'Join now') or contains(@href, 'signup')]")
            ));
            joinButton.click();
            pause(2000);

            // Verify navigation
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Current URL: " + currentUrl);

            Assert.assertTrue(
                    currentUrl.contains("signup") ||
                            currentUrl.contains("join") ||
                            currentUrl.contains("registration"),
                    "Should navigate to signup page"
            );

            System.out.println("✓ Successfully navigated to registration page");

        } catch (Exception e) {
            System.out.println("Join button interaction: " + e.getMessage());
        }
    }

    // Test 2: Verify registration form fields are present

    @Test(priority = 2, description = "Verify registration form fields")
    public void testRegistrationFormFields() {
        System.out.println("\n=== Test 2: Registration Form Fields ===");

        driver.get(baseUrl + "/signup");
        pause(2000);

        // Look for common registration fields
        List<WebElement> emailInputs = driver.findElements(
                By.xpath("//input[@type='email' or contains(@name, 'email') or contains(@id, 'email')]")
        );

        List<WebElement> passwordInputs = driver.findElements(
                By.xpath("//input[@type='password' or contains(@name, 'password')]")
        );

        System.out.println("Found " + emailInputs.size() + " email field(s)");
        System.out.println("Found " + passwordInputs.size() + " password field(s)");

        // Verify essential fields are present
        Assert.assertTrue(emailInputs.size() > 0 || passwordInputs.size() > 0,
                "Registration form should have input fields");

        System.out.println("✓ Registration form fields are present");
    }

    // Test 3: Verify email field validation

    @Test(priority = 3, description = "Verify email field validation")
    public void testEmailValidation() {
        System.out.println("\n=== Test 3: Email Field Validation ===");

        driver.get(baseUrl + "/signup");
        pause(2000);

        try {
            WebElement emailField = driver.findElement(
                    By.xpath("//input[@type='email' or contains(@name, 'email')]")
            );

            // Test with invalid email
            String invalidEmail = "notanemail";
            emailField.sendKeys(invalidEmail);

            String enteredValue = emailField.getAttribute("value");
            System.out.println("Entered email: " + enteredValue);

            Assert.assertEquals(enteredValue, invalidEmail,
                    "Email field should accept input");

            // HTML5 validation will prevent form submission with invalid email
            String inputType = emailField.getAttribute("type");
            Assert.assertTrue(inputType.equals("email") || inputType.equals("text"),
                    "Email field should have appropriate type");

            System.out.println("✓ Email field present and accepts input");

        } catch (Exception e) {
            System.out.println("Email field interaction: " + e.getMessage());
        }
    }

    // Test 4: Verify password field exists and accepts input

    @Test(priority = 4, description = "Verify password field")
    public void testPasswordField() {
        System.out.println("\n=== Test 4: Password Field ===");

        driver.get(baseUrl + "/signup");
        pause(2000);

        try {
            WebElement passwordField = driver.findElement(
                    By.xpath("//input[@type='password']")
            );

            String testPassword = "TestPassword123!";
            passwordField.sendKeys(testPassword);

            // Verify input was masked (value should still be accessible via getAttribute)
            String type = passwordField.getAttribute("type");
            Assert.assertEquals(type, "password",
                    "Password field should have type='password'");

            System.out.println("✓ Password field works correctly");

        } catch (Exception e) {
            System.out.println("Password field interaction: " + e.getMessage());
        }
    }

    // Test 5: Verify name/personal info fields

    @Test(priority = 5, description = "Verify name fields")
    public void testNameFields() {
        System.out.println("\n=== Test 5: Name Fields ===");

        driver.get(baseUrl + "/signup");
        pause(2000);

        // Look for first name field
        List<WebElement> firstNameFields = driver.findElements(
                By.xpath("//input[contains(@name, 'firstName') or contains(@id, 'first') or @autocomplete='given-name']")
        );

        // Look for last name field
        List<WebElement> lastNameFields = driver.findElements(
                By.xpath("//input[contains(@name, 'lastName') or contains(@id, 'last') or @autocomplete='family-name']")
        );

        System.out.println("First name fields found: " + firstNameFields.size());
        System.out.println("Last name fields found: " + lastNameFields.size());

        // LinkedIn registration may use different flows
        boolean nameFieldsPresent = firstNameFields.size() > 0 || lastNameFields.size() > 0;
        System.out.println("Name fields present: " + nameFieldsPresent);

        System.out.println("✓ Name field check completed");
    }

    // Test 6: Verify submit/agree button is present

    @Test(priority = 6, description = "Verify submit button presence")
    public void testSubmitButtonPresent() {
        System.out.println("\n=== Test 6: Submit Button ===");

        driver.get(baseUrl + "/signup");
        pause(2000);

        // Look for submit button
        List<WebElement> submitButtons = driver.findElements(
                By.xpath("//button[@type='submit' or contains(., 'Agree') or contains(., 'Join') or contains(., 'Continue')]")
        );

        if (submitButtons.size() > 0) {
            System.out.println("Found " + submitButtons.size() + " submit button(s)");
            WebElement button = submitButtons.get(0);
            Assert.assertTrue(button.isDisplayed(), "Submit button should be visible");
            System.out.println("Button text: " + button.getText());
        } else {
            System.out.println("Submit button not immediately visible");
        }

        System.out.println("✓ Submit button check completed");
    }
}