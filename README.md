# Selenium E2E Test Suite — SauceDemo

Automated end-to-end tests for [saucedemo.com](https://www.saucedemo.com), built with Selenium 4, JUnit 5, and the Page Object pattern.

## Stack

- Java 17
- Maven
- Selenium 4.25.0
- JUnit 5.11.0 (Jupiter + Params)
- Chrome (headless by default, via Selenium Manager — no manual driver setup needed)

## Project structure

```
src/test/java/et/edu/aau/selenium/
├── base/
│   └── BaseTest.java          # @BeforeEach/@AfterEach browser lifecycle
├── pages/
│   ├── LoginPage.java         # login form
│   ├── InventoryPage.java     # product catalogue + add-to-cart
│   └── CartPage.java          # cart contents
└── tests/
    ├── SmokeTest.java         # page loads, title/logo visible
    ├── LoginPositiveTest.java # login → browse → add to cart
    ├── LoginNegativeTest.java # wrong password / locked-out account
    └── LoginDataDrivenTest.java # parameterized login validation (EP)
```

## Running the tests

```bash
mvn test
```

Runs headless by default. To watch it in a real browser window:

```bash
mvn test -Dheadless=false
```

## What's covered

| # | Coverage |
|---|---|
| Smoke test | Site loads with the correct title and branding |
| Locators | `By.id`, `By.cssSelector`, `By.className` — no positional XPath |
| Positive path | Valid login → inventory page → add to cart → cart contents |
| Negative path | Wrong password and locked-out account both surface the right error |
| Explicit waits | `WebDriverWait` + `ExpectedConditions` throughout — no `Thread.sleep` |
| Data-driven test | `@ParameterizedTest` over equivalence partitions of the login form |
| Page Objects | Tests only call page object methods, never raw locators |
| Lifecycle | Fresh browser per test via `@BeforeEach` / `@AfterEach` |

## Known site defects found during manual exploration

- `inventory.html` renders two nested elements sharing `id="inventory_container"` (duplicate DOM id).
- `problem_user`: every product shows the same placeholder image regardless of the actual item.
- `problem_user`: "Add to cart" doesn't update the button label or cart badge.
- `performance_glitch_user`: noticeably slow transition from login to the inventory page.

See `selenium_ate_9686_14.pdf` for the full report, test case table, and design technique used for the data-driven test.
