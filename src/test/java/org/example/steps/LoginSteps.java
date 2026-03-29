package org.example.steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.pages.LoginPageLayout;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.Assert.*;

public class LoginSteps {

    private static final String BASE_URL =
            "https://opensource-demo.orangehrmlive.com/";

    private final WebDriverContext webDriverContext;
    private LoginPageLayout loginPageLayout;

    // Cucumber-PicoContainer injects the shared WebDriverContext
    public LoginSteps(WebDriverContext webDriverContext) {
        this.webDriverContext = webDriverContext;
    }

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        webDriverContext.setWebDriver(new ChromeDriver(options));
    }

    @After
    public void tearDown() {
        if (webDriverContext.getWebDriver() != null) {
            webDriverContext.getWebDriver().quit();
        }
    }

    @Given("the user is on the OrangeHRM login page")
    public void theUserIsOnTheLoginPage() {
        webDriverContext.getWebDriver().get(BASE_URL + "web/index.php/auth/login");
        loginPageLayout = new LoginPageLayout(webDriverContext.getWebDriver());
    }

    @When("the user enters username {string} and password {string}")
    public void theUserEntersCredentials(String username, String password) {
        loginPageLayout.typeUsername(username);
        loginPageLayout.typePassword(password);
    }

    @And("the user clicks the login button")
    public void theUserClicksLoginButton() {
        loginPageLayout.submitLogin();
    }

    @Then("the user should be redirected to the dashboard page")
    public void theUserShouldBeRedirectedToDashboard() {
        assertTrue("Expected to land on the dashboard page",
                loginPageLayout.isDashboardVisible());
    }

    @Then("an error message {string} should be displayed")
    public void anErrorMessageShouldBeDisplayed(String expectedMessage) {
        assertEquals(expectedMessage, loginPageLayout.getErrorMessage());
    }
}
