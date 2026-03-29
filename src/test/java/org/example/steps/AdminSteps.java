package org.example.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.pages.AdminPageLayout;

import static org.junit.Assert.*;

public class AdminSteps {

    private static final String ADMIN_URL =
            "https://opensource-demo.orangehrmlive.com/web/index.php/admin/viewSystemUsers";

    // Shared driver is provided by LoginSteps via the Cucumber context.
    // We use a shared context so both step classes access the same instance.
    private final WebDriverContext webDriverContext;
    private AdminPageLayout adminPageLayout;

    public AdminSteps(WebDriverContext webDriverContext) {
        this.webDriverContext = webDriverContext;
    }

    @Given("the admin navigates to the Admin page")
    public void navigateToAdminPage() {
        webDriverContext.getWebDriver().get(ADMIN_URL);
        adminPageLayout = new AdminPageLayout(webDriverContext.getWebDriver());
    }

    @When("the admin searches by username {string}")
    public void searchByUsername(String username) {
        adminPageLayout.searchByUsername(username);
    }

    @Then("the search results should contain a user with username {string}")
    public void resultsShouldContainUser(String username) {
        assertTrue("Expected user '" + username + "' in search results",
                adminPageLayout.isUsernameInResults(username));
    }

    @When("the admin clicks the Add button")
    public void clickAddButton() {
        adminPageLayout.openAddUserForm();
    }

    @And("the admin saves the user form without filling any fields")
    public void saveFormEmpty() {
        adminPageLayout.submitUserForm();
    }

    @Then("required field error messages should be displayed")
    public void requiredErrorsShouldBeDisplayed() {
        assertTrue("Expected required field error messages to be visible",
                adminPageLayout.areRequiredErrorsVisible());
    }
}
