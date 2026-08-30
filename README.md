# Selenium E2E Test Suite — Practice Software Testing (Toolshop)

Automated end-to-end tests for [practicesoftwaretesting.com](https://practicesoftwaretesting.com), a demo tool shop built and maintained specifically for test-automation practice. Built with Selenium 4, JUnit 5, and the Page Object pattern.

## Why this site

The assignment requires a login/search/form feature with a visible result and a way to trigger an error, while avoiding sites that block automation. `practicesoftwaretesting.com` fits well: it has a real login flow, a product search with filters, an add-to-cart-and-checkout flow, and client-side/server-side form validation — and every element carries a stable `data-test` attribute, since the site is purpose-built for this kind of testing.

`automationexercise.com` was tried first (it's on the assignment's suggestion list) but was rejected: it currently sits behind a persistent "please wait while your request is verified" bot-check page that never clears, which is exactly the kind of automation-blocking behaviour the assignment says to avoid.

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
│   └── BaseTest.java           # @BeforeEach/@AfterEach browser lifecycle
├── pages/
│   ├── LoginPage.java          # login form
│   ├── AccountPage.java        # "My account" landing page after login
│   ├── ProductsPage.java       # home page: catalogue + search
│   ├── ProductDetailPage.java  # single product page + add-to-cart
│   └── CartPage.java           # cart / checkout step 1
└── tests/
    ├── SmokeTest.java             # T1 - page loads, title/catalogue visible
    ├── LoginPositiveTest.java     # T3 - login → search → add to cart → cart total
    ├── LoginNegativeTest.java     # T4 - wrong password / empty required fields
    ├── SearchNegativeTest.java    # T4 - nonsense search returns no results
    └── LoginDataDrivenTest.java   # T6 - parameterized login validation (EP)
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
| Smoke test | Home page loads with the correct title and a rendered product catalogue |
| Locators | `By.id`, `By.cssSelector` — no positional XPath |
| Positive path | Login → land on account page → search → open product → add to cart → cart contents & total |
| Negative path | Wrong password, empty required fields, and a nonsense search all surface the right message |
| Explicit waits | `WebDriverWait` + `ExpectedConditions` (including `.or(...)` and a custom lambda condition) throughout — no `Thread.sleep` |
| Data-driven test | `@ParameterizedTest` over 4 equivalence partitions of the login form |
| Page Objects | Tests only call page object methods, never raw locators |
| Lifecycle | Fresh browser per test via `@BeforeEach` / `@AfterEach` |

## Equivalence partitions used for T6

The login form takes two inputs (email, password). Partitions:

| Partition | Input | Expected outcome |
|---|---|---|
| P1 | Well-formed, registered email + correct password | Success — redirected to `/account` |
| P2 | Well-formed, registered email + wrong password | Server-side error: "Invalid email or password" |
| P3 | Syntactically invalid email (missing `@`) | Client-side error: "Email format is invalid" |
| P4 | Both required fields empty | Client-side errors: "Email is required" / "Password is required" |

## Known site defects / odd behaviour found during exploration

- On the cart page, the `[data-test='product-title']` element has a trailing-whitespace text node before an Angular comment marker in the DOM; ChromeDriver's `getText()` doesn't trim it consistently there (unlike the equivalent element on the catalogue page), so the Page Object trims defensively.
- The account nav menu (which shows the logged-in user's name) populates slightly after the rest of the "My account" page — it's a separate async call, so a wait purely on the page title is a race condition; `AccountPage` waits on both elements.
- Navigating directly to `/auth/login` while already authenticated does not redirect away from the login form — the form (and its previous stale error/cart state in the header) is shown regardless of session state.
