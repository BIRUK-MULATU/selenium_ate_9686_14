package et.edu.aau.selenium.tests;

import et.edu.aau.selenium.base.BaseTest;
import et.edu.aau.selenium.pages.CartPage;
import et.edu.aau.selenium.pages.InventoryPage;
import et.edu.aau.selenium.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("T3 - Positive path: login, browse, add to cart")
class LoginPositiveTest extends BaseTest {

    private static final String USER = "standard_user";
    private static final String PASS = "secret_sauce";
    private static final String PRODUCT = "Sauce Labs Backpack";

    @Test
    @DisplayName("Valid credentials land on the inventory page")
    void validLoginShowsInventory() {
        InventoryPage inventory = new LoginPage(driver, wait)
                                      .open()
                                      .loginAs(USER, PASS);

        assertTrue(inventory.isLoaded(), "Inventory container should be present");
        assertTrue(driver.getCurrentUrl().contains("inventory.html"),
                   "Browser should be on the inventory URL after login");
        assertEquals(6, inventory.productNames().size(),
                     "Swag Labs catalogue should list six products");
    }

    @Test
    @DisplayName("Adding a product updates the cart badge and the cart contents")
    void addToCartUpdatesBadgeAndCart() {
        InventoryPage inventory = new LoginPage(driver, wait)
                                      .open()
                                      .loginAs(USER, PASS);

        assertEquals(0, inventory.cartCount(), "Cart should start empty");

        inventory.addToCart(PRODUCT);
        assertEquals(1, inventory.cartCount(), "Badge should show one item");

        CartPage cart = inventory.openCart();
        assertEquals(1, cart.itemCount(), "Cart should hold exactly one line item");
        assertTrue(cart.itemNames().contains(PRODUCT),
                   "Cart should contain the product that was added");
    }
}
