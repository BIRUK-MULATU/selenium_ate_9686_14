package et.edu.aau.selenium.tests;

import et.edu.aau.selenium.base.BaseTest;
import et.edu.aau.selenium.pages.AccountPage;
import et.edu.aau.selenium.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * T6 - Data-driven test over the login form, using Equivalence Partitioning.
 *
 * Partitions on the (email, password) input pair:
 *   P1 - well-formed, registered email + correct password  -> success, lands on /account
 *   P2 - well-formed, registered email + wrong password    -> server-side "Invalid email or password"
 *   P3 - syntactically invalid email (missing '@')          -> client-side "Email format is invalid"
 *   P4 - both required fields empty                         -> client-side "Email/Password is required"
 */
@DisplayName("T6 - Data-driven login validation (Equivalence Partitioning)")
class LoginDataDrivenTest extends BaseTest {

    @ParameterizedTest(name = "[{index}] {5}")
    @CsvSource({
        // email,                                password,        outcome, errorField, expectedMessage,             partition label
        "customer@practicesoftwaretesting.com,    welcome01,       SUCCESS, NA,         '',                          P1 - valid email and correct password",
        "customer@practicesoftwaretesting.com,    wrong_password,  ERROR,   login,      Invalid email or password,   P2 - valid email format, wrong password",
        "not-an-email,                            welcome01,       ERROR,   email,      Email format is invalid,     P3 - malformed email (format boundary)",
        "'',                                      '',              ERROR,   email,      Email is required,           P4 - both required fields empty"
    })
    @DisplayName("Login credential partitions each produce the right outcome")
    void loginCredentialPartitions(String email,
                                    String password,
                                    String outcome,
                                    String errorField,
                                    String expectedMessage,
                                    String partition) {

        LoginPage login = new LoginPage(driver, wait).open();

        if (outcome.equals("SUCCESS")) {
            AccountPage account = login.loginAs(email, password);
            assertTrue(account.isLoaded(), partition + " should land on the account page");
        } else {
            login.loginExpectingFailure(email, password);
            assertEquals(expectedMessage, login.errorText(errorField),
                         partition + " should produce the expected validation message");
        }
    }
}
