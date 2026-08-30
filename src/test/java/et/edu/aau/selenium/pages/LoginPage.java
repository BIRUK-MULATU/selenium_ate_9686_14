package et.edu.aau.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    public static final String URL = "https://practicesoftwaretesting.com/auth/login";

    private final WebDriver driver;
    private final WebDriverWait wait;

    // --- T2: two different locator strategies, no positional XPath ---
    private final By emailField    = By.id("email");                            // By.id
    private final By passwordField = By.id("password");                         // By.id
    private final By loginButton   = By.cssSelector("[data-test='login-submit']");  // By.cssSelector
    private final By loginError    = By.cssSelector("[data-test='login-error']");
    private final By emailError    = By.cssSelector("[data-test='email-error']");
    private final By passwordError = By.cssSelector("[data-test='password-error']");

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
    public void submitCredentials(String email, String password) {
        WebElement email_ = driver.findElement(emailField);
        email_.clear();
        email_.sendKeys(email);

        WebElement password_ = driver.findElement(passwordField);
        password_.clear();
        password_.sendKeys(password);

        driver.findElement(loginButton).click();
    }

    /** Happy path: expects to land on the account page. */
    public AccountPage loginAs(String email, String password) {
        submitCredentials(email, password);
        wait.until(ExpectedConditions.urlContains("/account"));
        return new AccountPage(driver, wait).waitUntilLoaded();
    }

    /** Sad path: expects to stay here with one of the validation alerts shown. */
    public LoginPage loginExpectingFailure(String email, String password) {
        submitCredentials(email, password);
        wait.until(ExpectedConditions.or(
            ExpectedConditions.visibilityOfElementLocated(loginError),
            ExpectedConditions.visibilityOfElementLocated(emailError),
            ExpectedConditions.visibilityOfElementLocated(passwordError)
        ));
        return this;
    }

    /**
     * Reads the text of a specific validation alert.
     * @param field one of "login" (invalid credentials), "email" or "password" (required-field / format errors)
     */
    public String errorText(String field) {
        By locator = switch (field) {
            case "email" -> emailError;
            case "password" -> passwordError;
            default -> loginError;
        };
        return driver.findElement(locator).getText();
    }

    public String title() {
        return driver.getTitle();
    }
}
