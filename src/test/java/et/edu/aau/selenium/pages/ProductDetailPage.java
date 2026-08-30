package et.edu.aau.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductDetailPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By productName   = By.cssSelector("[data-test='product-name']");
    private final By addToCartBtn  = By.cssSelector("[data-test='add-to-cart']");
    private final By cartBadge     = By.cssSelector("[data-test='nav-cart']");

    public ProductDetailPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public ProductDetailPage waitUntilLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productName));
        return this;
    }

    public String productName() {
        return driver.findElement(productName).getText();
    }

    public int cartCount() {
        try {
            return Integer.parseInt(driver.findElement(cartBadge).getText().trim());
        } catch (NoSuchElementException | NumberFormatException e) {
            return 0;
        }
    }

    /** T5: the explicit wait. Waits for the cart badge to actually change before returning. */
    public ProductDetailPage addToCart() {
        int before = cartCount();
        // wait for the button to be clickable rather than just present - Angular can render
        // the markup slightly before its click handler is attached (hydration lag)
        wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn)).click();
        wait.until(d -> cartCount() > before);
        return this;
    }

    public CartPage goToCart() {
        driver.findElement(cartBadge).click();
        return new CartPage(driver, wait).waitUntilLoaded();
    }
}
