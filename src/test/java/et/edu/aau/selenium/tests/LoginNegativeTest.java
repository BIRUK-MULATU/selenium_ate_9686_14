package et.edu.aau.selenium.tests;

import et.edu.aau.selenium.base.BaseTest;
import et.edu.aau.selenium.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("T4 - Negative path: login")
class LoginNegativeTest extends BaseTest {

    @Test
    @DisplayName("Wrong password is rejected with an error and no navigation")
    void wrongPasswordShowsError() {
        LoginPage login = new LoginPage(driver, wait)
                               .open()
                               .loginExpectingFailure("customer@practicesoftwaretesting.com", "definitely_wrong");

        assertEquals("Invalid email or password", login.errorText("login"));
        assertFalse(driver.getCurrentUrl().contains("/account"),
                    "A failed login must not navigate to the account page");
    }

    @Test
    @DisplayName("Submitting the form empty is blocked by required-field validation")
    void emptyFieldsBlockSubmission() {
        LoginPage login = new LoginPage(driver, wait)
                               .open()
                               .loginExpectingFailure("", "");

        assertEquals("Email is required", login.errorText("email"));
        assertEquals("Password is required", login.errorText("password"));
        assertFalse(driver.getCurrentUrl().contains("/account"));
    }
}
