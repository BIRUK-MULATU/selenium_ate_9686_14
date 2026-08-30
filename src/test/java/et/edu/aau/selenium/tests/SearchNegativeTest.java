package et.edu.aau.selenium.tests;

import et.edu.aau.selenium.base.BaseTest;
import et.edu.aau.selenium.pages.ProductsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("T4 - Negative path: search")
class SearchNegativeTest extends BaseTest {

    @Test
    @DisplayName("A nonsense search term returns no results")
    void nonsenseSearchShowsNoResults() {
        ProductsPage products = new ProductsPage(driver, wait)
                                     .open()
                                     .searchFor("zzzznoresultsxyz123");

        assertTrue(products.isNoResultsDisplayed(), "The 'no products found' message should be shown");
        assertTrue(products.productNames().isEmpty(), "No product tiles should render for a nonsense search");
    }
}
