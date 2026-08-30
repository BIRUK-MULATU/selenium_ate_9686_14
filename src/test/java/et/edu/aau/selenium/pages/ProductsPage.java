package et.edu.aau.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

/** Home page / product catalogue, including search. */
public class ProductsPage {

    public static final String URL = "https://practicesoftwaretesting.com/";

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By searchInput     = By.cssSelector("[data-test='search-query']");
    private final By searchButton    = By.cssSelector("[data-test='search-submit']");
    private final By searchCompleted = By.cssSelector("[data-test='search_completed']");
    private final By noResults       = By.cssSelector("[data-test='no-results']");
    private final By productLinks    = By.cssSelector("a[data-test^='product-']");
    private final By productNameTag  = By.cssSelector("[data-test='product-name']");
    private final By cartBadge       = By.cssSelector("[data-test='nav-cart']");

    public ProductsPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public ProductsPage open() {
        driver.get(URL);
        return waitUntilLoaded();
    }

    public ProductsPage waitUntilLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        return this;
    }

    /** T5: the explicit wait. Results replace the DOM asynchronously after submit. */
    public ProductsPage searchFor(String term) {
        WebElement search = driver.findElement(searchInput);
        search.clear();
        search.sendKeys(term);
        driver.findElement(searchButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchCompleted));
        return this;
    }

    public List<String> productNames() {
        return driver.findElements(productNameTag).stream()
                     .map(WebElement::getText)
                     .collect(Collectors.toList());
    }

    public boolean isNoResultsDisplayed() {
        return !driver.findElements(noResults).isEmpty();
    }

    /** Opens the product whose visible name is an exact match, e.g. "Hammer". */
    public ProductDetailPage openProduct(String exactName) {
        WebElement link = driver.findElements(productLinks).stream()
            .filter(a -> a.findElement(productNameTag).getText().equals(exactName))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("No product named '" + exactName + "' in the current listing"));

        wait.until(ExpectedConditions.elementToBeClickable(link)).click();
        return new ProductDetailPage(driver, wait).waitUntilLoaded();
    }

    /** Returns 0 when the badge is absent, which is how an empty cart renders. */
    public int cartCount() {
        try {
            return Integer.parseInt(driver.findElement(cartBadge).getText().trim());
        } catch (NoSuchElementException | NumberFormatException e) {
            return 0;
        }
    }
}
