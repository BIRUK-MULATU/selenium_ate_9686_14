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

    private final By cartList  = By.cssSelector(".cart_list");
    private final By cartItems = By.cssSelector(".cart_item");
    private final By itemNames = By.cssSelector(".inventory_item_name");

    public CartPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public CartPage waitUntilLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(cartList));
        return this;
    }

    public int itemCount() {
        return driver.findElements(cartItems).size();
    }

    public List<String> itemNames() {
        return driver.findElements(itemNames).stream()
                     .map(WebElement::getText)
                     .collect(Collectors.toList());
    }
}
