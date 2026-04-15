package com.linkedin.tests.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * LoginPage - Page Object Model for LinkedIn Login functionality

 * This class encapsulates all elements and actions related to LinkedIn's login page.
 * Following Page Object Model (POM) design pattern for better maintainability.
 */
public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // Locators for login page elements
    private By emailInput = By.id("session_key");
    private By passwordInput = By.id("session_password");
    private By signInButton = By.xpath("//button[@type='submit' and contains(., 'Sign in')]");
    private By rememberMeCheckbox = By.id("remember-me-checkbox__input");
    private By forgotPasswordLink = By.linkText("Forgot password?");
    private By joinNowLink = By.linkText("Join now");
    private By errorMessage = By.xpath("//*[@role='alert']");
    private By emailError = By.xpath("//input[@id='session_key']/following-sibling::div[contains(@class, 'error')]");
    private By passwordError = By.xpath("//input[@id='session_password']/following-sibling::div[contains(@class, 'error')]");

    // Alternative locators for homepage login
    private By homeEmailInput = By.id("session_key");
    private By homePasswordInput = By.id("session_password");

    // Constructor
    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // Enter email address in the login form

    public void enterEmail(String email) {
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        emailField.clear();
        emailField.sendKeys(email);
        System.out.println("Entered email: " + email);
    }

    // Enter password in the login form

    public void enterPassword(String password) {
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        passwordField.clear();
        passwordField.sendKeys(password);
        System.out.println("Entered password");
    }

    // Click the Sign In button

    public void clickSignIn() {
        WebElement signIn = wait.until(ExpectedConditions.elementToBeClickable(signInButton));
        signIn.click();
        System.out.println("Clicked Sign In button");
    }

    // Complete login process

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickSignIn();
        System.out.println("Login attempt completed");
    }

    // Toggle Remember Me checkbox

    public void toggleRememberMe() {
        WebElement checkbox = driver.findElement(rememberMeCheckbox);
        checkbox.click();
        System.out.println("Toggled Remember Me checkbox");
    }

    // Check if Remember Me is selected

    public boolean isRememberMeSelected() {
        WebElement checkbox = driver.findElement(rememberMeCheckbox);
        return checkbox.isSelected();
    }

    // Click Forgot Password link

    public void clickForgotPassword() {
        WebElement forgotPwd = wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordLink));
        forgotPwd.click();
        System.out.println("Clicked Forgot Password link");
    }

    // Click Join Now link

    public void clickJoinNow() {
        WebElement joinNow = wait.until(ExpectedConditions.elementToBeClickable(joinNowLink));
        joinNow.click();
        System.out.println("Clicked Join Now link");
    }

    // Get error message text

    public String getErrorMessage() {
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return error.getText();
        } catch (Exception e) {
            return "";
        }
    }

    // Check if error message is displayed

    public boolean isErrorMessageDisplayed() {
        try {
            return driver.findElement(errorMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Get email field error message

    public String getEmailError() {
        try {
            WebElement error = driver.findElement(emailError);
            return error.getText();
        } catch (Exception e) {
            return "";
        }
    }

    // Get password field error message

    public String getPasswordError() {
        try {
            WebElement error = driver.findElement(passwordError);
            return error.getText();
        } catch (Exception e) {
            return "";
        }
    }

    // Check if Sign In button is enabled

    public boolean isSignInButtonEnabled() {
        WebElement signIn = driver.findElement(signInButton);
        return signIn.isEnabled();
    }

    // Get page title

    public String getPageTitle() {
        return driver.getTitle();
    }
}