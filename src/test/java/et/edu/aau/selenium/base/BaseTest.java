package et.edu.aau.selenium.base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BaseTest {

    static {
        java.util.logging.Logger
            .getLogger("org.openqa.selenium")
            .setLevel(java.util.logging.Level.SEVERE);
    }

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void startBrowser() {
        ChromeOptions options = new ChromeOptions();

        // headless by default; run "mvn test -Dheadless=false" to watch it work
        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440,900", "--disable-gpu", "--no-sandbox");

        driver = new ChromeDriver(options);

        // one shared wait object, 10s ceiling, used by every Page Object
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void quitBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
