package et.edu.aau.selenium.tests;

import et.edu.aau.selenium.base.BaseTest;
import et.edu.aau.selenium.pages.ProductsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("T1 - Navigation smoke test")
class SmokeTest extends BaseTest {

    @Test
    @DisplayName("Home page loads with the expected title and product catalogue")
    void homePageLoads() {
        ProductsPage home = new ProductsPage(driver, wait).open();

        assertEquals("Practice Software Testing - Toolshop - v5.0", driver.getTitle(),
                     "Page title should identify the Toolshop site");
        assertFalse(home.productNames().isEmpty(),
                    "The catalogue should render at least one product on load");
    }
}
