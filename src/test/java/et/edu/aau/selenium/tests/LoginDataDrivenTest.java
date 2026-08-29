package et.edu.aau.selenium.tests;

import et.edu.aau.selenium.base.BaseTest;
import et.edu.aau.selenium.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("T6 - Data-driven login validation (Equivalence Partitioning)")
class LoginDataDrivenTest extends BaseTest {

    @ParameterizedTest(name = "[{index}] {3}")
    @CsvSource({
        // username,        password,        expected fragment,  partition label
        "'',                secret_sauce,    Username is required, P2 - empty username",
        "standard_user,     '',              Password is required, P3 - empty password",
        "standard_user,     wrong_password,  do not match,         P4 - wrong password",
        "locked_out_user,   secret_sauce,    locked out,           P5 - locked account"
    })
    @DisplayName("Invalid credential partitions are each rejected with the right message")
    void invalidCredentialPartitionsAreRejected(String username,
                                                String password,
                                                String expectedFragment,
                                                String partition) {

        LoginPage login = new LoginPage(driver, wait)
                              .open()
                              .loginExpectingFailure(username, password);

        assertTrue(login.errorText().contains(expectedFragment),
                   partition + " should produce \"" + expectedFragment
                   + "\" but the page said: " + login.errorText());
    }
}
