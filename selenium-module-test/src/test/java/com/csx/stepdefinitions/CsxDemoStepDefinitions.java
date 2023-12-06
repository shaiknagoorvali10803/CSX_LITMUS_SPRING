package com.csx.stepdefinitions;

import com.csx.test.util.LoggingException;
import com.csx.test.util.ScreenshotUtils;
import com.csx.test.util.SeleniumUtil;
import com.csx.page.actions.CsxDemoPageActions;
import com.csx.springConfig.annotation.LazyAutowired;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

@CucumberContextConfiguration
@SpringBootTest
public class CsxDemoStepDefinitions {
    private static final Logger LOGGER = LoggerFactory.getLogger(CsxDemoStepDefinitions.class);
    @LazyAutowired
    private CsxDemoPageActions pageActions;
    @LazyAutowired
    private WebDriver driver;

    @LazyAutowired
    ScreenshotUtils screenshotUtils;
    @Autowired
    ScenarioContext scenarioContext;

    @Before
    public void settingScenario(Scenario scenario) {
        scenarioContext.setScenario(scenario);
    }

    @Given("I am on a {string} browser")
    public void determineBrowserType(final String browserType) {
        switch (browserType) {
            case "mobile":
                LOGGER.info("resizing window size for mobile");
                SeleniumUtil.resizeWindowForMobile(driver);
                break;
            case "tablet":
                LOGGER.info("resizing window size for tablet");
                SeleniumUtil.resizeWindowForTablet(driver);
                break;
            case "desktop":
                LOGGER.info("resizing window size for desktop");
            default:
                SeleniumUtil.waitByTime(3000);
                SeleniumUtil.maximizeWindow(driver);
                break;
        }
    }

    @When("I navigate to csx.com")
    public void navigateToCsxWebsite() throws LoggingException, InterruptedException {
        driver.get("https://www.csx.com");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
    }

    @Then("I should see the copyright information")
    public void verifyCopyrightInformation() throws LoggingException {
        Assertions.assertTrue(pageActions.isHeaderImagePresent());
        screenshotUtils.insertScreenshot("CSX Home Page");
    }
}
