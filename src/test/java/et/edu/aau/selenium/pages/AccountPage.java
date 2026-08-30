package et.edu.aau.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** The "My account" landing page shown right after a successful login. */
public class AccountPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By pageTitle = By.cssSelector("[data-test='page-title']");
    private final By userMenu  = By.cssSelector("[data-test='nav-menu']");
    private final By homeLink  = By.cssSelector("[data-test='nav-home']");

    public AccountPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /**
     * T5: the explicit wait. The account page only exists after the login round-trip, and the
     * nav menu's user name is filled in slightly after the page title (separate async call).
     */
    public AccountPage waitUntilLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
        wait.until(ExpectedConditions.visibilityOfElementLocated(userMenu));
        return this;
    }

    public boolean isLoaded() {
        return driver.findElement(pageTitle).getText().equalsIgnoreCase("My account");
    }

    public String loggedInUserLabel() {
        return driver.findElement(userMenu).getText();
    }

    public ProductsPage goToShop() {
        driver.findElement(homeLink).click();
        return new ProductsPage(driver, wait).waitUntilLoaded();
    }
}
