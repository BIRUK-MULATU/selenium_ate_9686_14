package et.edu.aau.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By container   = By.id("inventory_container");
    private final By itemNames   = By.cssSelector(".inventory_item_name");
    private final By cartBadge   = By.cssSelector(".shopping_cart_badge");
    private final By cartLink    = By.cssSelector(".shopping_cart_link");

    public InventoryPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /** T5: the explicit wait. The inventory only exists after the login round-trip. */
    public InventoryPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(container));
        return this;
    }

    public boolean isLoaded() {
        // saucedemo.com nests two elements sharing id="inventory_container" in its
        // real markup, so this can't assert an exact count of 1.
        return !driver.findElements(container).isEmpty();
    }

    public List<String> productNames() {
        return driver.findElements(itemNames).stream()
                     .map(WebElement::getText)
                     .collect(Collectors.toList());
    }

    /** Builds the button locator from the product name, e.g.
     *  "Sauce Labs Backpack" -> [data-test='add-to-cart-sauce-labs-backpack'] */
    public InventoryPage addToCart(String productName) {
        String slug = productName.toLowerCase().replaceAll("[^a-z0-9]+", "-");
        By addButton = By.cssSelector("[data-test='add-to-cart-" + slug + "']");
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
        return this;
    }

    /** Returns 0 when the badge is absent, which is how an empty cart renders. */
    public int cartCount() {
        try {
            return Integer.parseInt(driver.findElement(cartBadge).getText().trim());
        } catch (NoSuchElementException | NumberFormatException e) {
            return 0;
        }
    }

    public CartPage openCart() {
        driver.findElement(cartLink).click();
        return new CartPage(driver, wait).waitUntilLoaded();
    }
}
