package com.csx.stepdefinitions;

import com.csx.page.actions.CsxDemoPageActions;
import com.csx.test.util.LoggingException;
import com.csx.test.util.SeleniumUtil;
import com.csx.test.util.WebDriverProvider;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class CsxDemoStepDefinitions {
  private static final Logger LOGGER = LoggerFactory.getLogger(CsxDemoStepDefinitions.class);

  @Inject
  private WebDriverProvider driverProvider;

  @Inject
  CsxDemoPageActions pageActions;

  @Given("I am on a mobile browser")
  public void determineBrowserType() {
        LOGGER.info("resizing window size for mobile");
        SeleniumUtil.resizeWindowForMobile(driverProvider.getInstance());
  }

  @When("I navigate to csx.com")
  public void navigateToCsxWebsite() throws LoggingException, InterruptedException {
    driverProvider.getInstance().get("https://www.csx.com");
  }

  @Then("I should see the copyright information")
  public void verifyCopyrightInformation() throws LoggingException {
    Assertions.assertFalse(pageActions.isHeaderImagePresent());
  }
}
