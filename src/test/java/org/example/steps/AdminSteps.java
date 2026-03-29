package org.example.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.pages.AdminPage;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.*;

public class AdminSteps {

    private static final String ADMIN_URL =
            "https://opensource-demo.orangehrmlive.com/web/index.php/admin/viewSystemUsers";

    // Shared driver is provided by LoginSteps via the Cucumber context.
    // We use a simple holder so both step classes access the same instance.
    private final DriverHolder holder;
    private AdminPage adminPage;

    public AdminSteps(DriverHolder holder) {
        this.holder = holder;
    }

    @Given("the admin navigates to the Admin page")
    public void navigateToAdminPage() {
        holder.getDriver().get(ADMIN_URL);
        adminPage = new AdminPage(holder.getDriver());
    }

    @When("the admin searches by username {string}")
    public void searchByUsername(String username) {
        adminPage.searchByUsername(username);
    }

    @Then("the search results should contain a user with username {string}")
    public void resultsShouldContainUser(String username) {
        assertTrue("Expected user '" + username + "' in search results",
                adminPage.isUserInResults(username));
    }

    @When("the admin clicks the Add button")
    public void clickAddButton() {
        adminPage.clickAdd();
    }

    @And("the admin saves the user form without filling any fields")
    public void saveFormEmpty() {
        adminPage.clickSave();
    }

    @Then("required field error messages should be displayed")
    public void requiredErrorsShouldBeDisplayed() {
        assertTrue("Expected required field error messages to be visible",
                adminPage.hasRequiredErrors());
    }
}
