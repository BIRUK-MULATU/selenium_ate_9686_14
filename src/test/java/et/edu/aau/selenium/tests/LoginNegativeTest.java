package et.edu.aau.selenium.tests;

import et.edu.aau.selenium.base.BaseTest;
import et.edu.aau.selenium.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("T4 - Negative path")
class LoginNegativeTest extends BaseTest {

    @Test
    @DisplayName("Wrong password is rejected with an error and no navigation")
    void wrongPasswordShowsError() {
        LoginPage login = new LoginPage(driver, wait)
                              .open()
                              .loginExpectingFailure("standard_user", "definitely_wrong");

        assertTrue(login.errorText().contains("do not match"),
                   "Error should say the credentials do not match. Actual: " + login.errorText());
        assertFalse(driver.getCurrentUrl().contains("inventory.html"),
                    "A failed login must not navigate to the inventory page");
    }

    @Test
    @DisplayName("A locked-out account is blocked with its own message")
    void lockedOutUserIsBlocked() {
        LoginPage login = new LoginPage(driver, wait)
                              .open()
                              .loginExpectingFailure("locked_out_user", "secret_sauce");

        assertTrue(login.errorText().toLowerCase().contains("locked out"),
                   "Error should mention the account is locked out. Actual: " + login.errorText());
    }
}
