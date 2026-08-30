package et.edu.aau.selenium.tests;

import et.edu.aau.selenium.base.BaseTest;
import et.edu.aau.selenium.pages.AccountPage;
import et.edu.aau.selenium.pages.CartPage;
import et.edu.aau.selenium.pages.LoginPage;
import et.edu.aau.selenium.pages.ProductDetailPage;
import et.edu.aau.selenium.pages.ProductsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("T3 - Positive path: login, search, add to cart")
class LoginPositiveTest extends BaseTest {

    private static final String EMAIL = "customer@practicesoftwaretesting.com";
    private static final String PASSWORD = "welcome01";
    private static final String PRODUCT = "Hammer";

    @Test
    @DisplayName("Valid credentials land on the My account page")
    void validLoginShowsAccountPage() {
        AccountPage account = new LoginPage(driver, wait)
                                   .open()
                                   .loginAs(EMAIL, PASSWORD);

        assertTrue(account.isLoaded(), "Account page title should read 'My account'");
        assertTrue(driver.getCurrentUrl().contains("/account"),
                   "Browser should be on the account URL after login");
        assertTrue(account.loggedInUserLabel().contains("Jane"),
                   "Nav menu should greet the logged-in customer by name");
    }

    @Test
    @DisplayName("Searching, opening a product and adding it to cart updates the cart total")
    void searchAndAddToCartUpdatesCart() {
        ProductsPage products = new LoginPage(driver, wait)
                                     .open()
                                     .loginAs(EMAIL, PASSWORD)
                                     .goToShop();

        products.searchFor(PRODUCT);
        assertFalse(products.isNoResultsDisplayed(), "A real search term should return results");
        assertTrue(products.productNames().contains(PRODUCT),
                   "Search results should include the exact product");

        ProductDetailPage detail = products.openProduct(PRODUCT);
        assertEquals(PRODUCT, detail.productName());
        assertEquals(0, detail.cartCount(), "Cart should start empty for a fresh session");

        detail.addToCart();
        assertEquals(1, detail.cartCount(), "Cart badge should show one item after adding");

        CartPage cart = detail.goToCart();
        assertEquals(1, cart.itemCount(), "Cart should hold exactly one line item");
        assertTrue(cart.productTitles().contains(PRODUCT),
                   "Cart should contain the product that was added");
        assertTrue(cart.totalAmount() > 0, "Cart total should reflect the added item's price");
    }
}
