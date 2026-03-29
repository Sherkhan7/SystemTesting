package org.example.steps;

import org.openqa.selenium.WebDriver;

/**
 * Shared context object injected by Cucumber-PicoContainer into every step
 * class that needs the same WebDriver instance within one scenario.
 */
public class WebDriverContext {

    private WebDriver webDriver;

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }
}

