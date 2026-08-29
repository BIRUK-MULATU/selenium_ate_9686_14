package et.edu.aau.selenium.tests;

import et.edu.aau.selenium.base.BaseTest;
import et.edu.aau.selenium.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("T1 - Navigation smoke test")
class SmokeTest extends BaseTest {

    @Test
    @DisplayName("Login page loads with the expected title and branding")
    void loginPageLoads() {
        LoginPage login = new LoginPage(driver, wait).open();

        assertEquals("Swag Labs", login.title(),
                     "Page title should identify the Swag Labs site");
        assertTrue(login.isLogoDisplayed(),
                   "The login logo should be visible on the landing page");
    }
}
