package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AdminPage {

    private static final String ADMIN_LIST_URL =
            "https://opensource-demo.orangehrmlive.com/web/index.php/admin/viewSystemUsers";

    private final WebDriver driver;
    private final WebDriverWait wait;
    // Longer wait for autocomplete (triggers an API call)
    private final WebDriverWait autocompleteWait;

    // Search form — scoped inside the filter card, not the add/edit form
    private final By usernameSearchInput = By.xpath(
            "//div[contains(@class,'oxd-table-filter')]//label[normalize-space()='Username']" +
            "/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private final By searchButton = By.xpath(
            "//div[contains(@class,'oxd-table-filter')]//button[@type='submit']");
    private final By addButton = By.cssSelector(
            ".orangehrm-header-container button");

    // Results table
    private final By resultRows = By.xpath(
            "//div[contains(@class,'oxd-table-body')]//div[contains(@class,'oxd-table-row')]");
    private final By noRecordsNotice = By.xpath(
            "//*[contains(text(),'No Records Found')]");

    // Add-user form — label-based XPath
    private final By userRoleDropdown = By.xpath(
            "//label[normalize-space()='User Role']/ancestor::div[contains(@class,'oxd-input-group')]" +
            "//div[contains(@class,'oxd-select-text')]");
    private final By employeeNameInput = By.cssSelector(
            ".oxd-autocomplete-wrapper input");
    private final By statusDropdown = By.xpath(
            "//label[normalize-space()='Status']/ancestor::div[contains(@class,'oxd-input-group')]" +
            "//div[contains(@class,'oxd-select-text')]");
    private final By usernameInput = By.xpath(
            "//label[normalize-space()='Username']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private final By saveButton = By.cssSelector(
            "button[type='submit']");
    private final By requiredErrors = By.cssSelector(
            ".oxd-input-field-error-message");

    public AdminPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.autocompleteWait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void navigateToList() {
        driver.get(ADMIN_LIST_URL);
        // Wait until the search form is ready before proceeding
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameSearchInput));
    }

    public void searchByUsername(String username) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(usernameSearchInput));
        input.clear();
        input.sendKeys(username);
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    public boolean isUserInResults(String username) {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(resultRows),
                ExpectedConditions.visibilityOfElementLocated(noRecordsNotice)
        ));
        List<WebElement> rows = driver.findElements(resultRows);
        for (WebElement row : rows) {
            if (row.getText().contains(username)) {
                return true;
            }
        }
        return false;
    }

    public void clickAdd() {
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
    }

    public void fillUserRole(String role) {
        wait.until(ExpectedConditions.elementToBeClickable(userRoleDropdown)).click();
        By option = By.xpath(
                "//div[@role='option']//span[text()='" + role + "']");
        wait.until(ExpectedConditions.elementToBeClickable(option)).click();
    }

    public void fillEmployeeName(String name) {
        String searchTerm = name.split("\\s+")[0];

        WebElement input = wait.until(
                ExpectedConditions.elementToBeClickable(employeeNameInput));
        input.click();
        input.sendKeys(searchTerm);

        // Wait until at least one option appears
        By optionLocator = By.cssSelector(".oxd-autocomplete-option");
        autocompleteWait.until(ExpectedConditions.visibilityOfElementLocated(optionLocator));

        // Use Actions with pauses: the Vue combobox needs time to process
        // each key event before the next one arrives
        new Actions(driver)
                .sendKeys(input, Keys.ARROW_DOWN)
                .pause(Duration.ofMillis(5000))
                .sendKeys(input, Keys.ENTER)
                .perform();
    }

    public void fillStatus(String status) {
        wait.until(ExpectedConditions.elementToBeClickable(statusDropdown)).click();
        By option = By.xpath(
                "//div[@role='option']//span[text()='" + status + "']");
        wait.until(ExpectedConditions.elementToBeClickable(option)).click();
    }

    public void fillUsername(String username) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(usernameInput));
        input.clear();
        input.sendKeys(username);
    }

    public void fillPassword(String password) {
        By passwordField = By.xpath(
                "//label[normalize-space()='Password']/ancestor::div[contains(@class,'oxd-input-group')]//input[@type='password']");
        By confirmPasswordField = By.xpath(
                "//label[normalize-space()='Confirm Password']/ancestor::div[contains(@class,'oxd-input-group')]//input[@type='password']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(password);
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordField)).sendKeys(password);
    }

    public void clickSave() {
        wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
    }

    public boolean hasRequiredErrors() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(requiredErrors));
        return !driver.findElements(requiredErrors).isEmpty();
    }
}
