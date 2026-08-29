package et.edu.aau.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    public static final String URL = "https://www.saucedemo.com/";

    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- T2: three different locator strategies, no positional XPath ---
    private final By usernameField = By.id("user-name");                    // By.id
    private final By passwordField = By.id("password");                     // By.id
    private final By loginButton   = By.id("login-button");                 // By.id
    private final By errorMessage  = By.cssSelector("[data-test='error']"); // By.cssSelector
    private final By loginLogo     = By.className("login_logo");            // By.className

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public LoginPage open() {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton));
        return this;
    }

    /** Fills the form and submits. Does not assert anything. */
    private void submitCredentials(String username, String password) {
        driver.findElement(usernameField).clear();
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    /** Happy path: expects to land on the inventory page. */
    public InventoryPage loginAs(String username, String password) {
        submitCredentials(username, password);
        return new InventoryPage(driver, wait).waitUntilLoaded();
    }

    /** Sad path: expects to stay here with an error shown. */
    public LoginPage loginExpectingFailure(String username, String password) {
        submitCredentials(username, password);
        wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        return this;
    }

    public String errorText() {
        return driver.findElement(errorMessage).getText();
    }

    public boolean isLogoDisplayed() {
        return driver.findElement(loginLogo).isDisplayed();
    }

    public String title() {
        return driver.getTitle();
    }
}
