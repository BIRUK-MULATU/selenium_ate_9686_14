package et.edu.aau.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By productTitles = By.cssSelector("[data-test='product-title']");
    private final By cartTotal     = By.cssSelector("[data-test='cart-total']");

    public CartPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public CartPage waitUntilLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(productTitles));
        return this;
    }

    public int itemCount() {
        return driver.findElements(productTitles).size();
    }

    /**
     * The site leaves a trailing whitespace text node before an Angular comment marker
     * inside this element, which ChromeDriver's getText() doesn't always trim here
     * (unlike the equivalent element on the catalogue page) - so trim defensively.
     */
    public List<String> productTitles() {
        return driver.findElements(productTitles).stream()
                     .map(e -> e.getText().trim())
                     .collect(Collectors.toList());
    }

    public double totalAmount() {
        String text = driver.findElement(cartTotal).getText().replace("$", "").trim();
        return Double.parseDouble(text);
    }
}
