package com.csx.stepdefinitions;

import com.csx.page.actions.GooglePageActions;
import com.csx.page.actions.VisaPageActions;
import com.csx.springConfig.annotation.LazyAutowired;
import com.csx.test.util.ScreenshotUtils;
import com.csx.test.util.SeleniumUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.Uninterruptibles;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.testng.Assert;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;

public class GoogleSteps {

    @Autowired
    protected WebDriver driver;
    @Autowired
    protected WebDriverWait wait;
    @Autowired
    ScenarioContext scenarioContext;
    @Autowired
    ScreenshotUtils screenshotUtils;
    @LazyAutowired
    private GooglePageActions googlePage;

    @LazyAutowired
    private VisaPageActions registrationPage;

    @Given("I am on the google site")
    public void launchSite() throws InterruptedException {
        this.googlePage.goTo();
        screenshotUtils.insertScreenshot1(scenarioContext.getScenario(),"screenshot");
        screenshotUtils.insertScreenshot("screenshot");
    }

    @When("I enter {string} as a keyword")
    public void enterKeyword(String keyword) throws InterruptedException {
        this.googlePage.search(keyword);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='result-stats']")));
        screenshotUtils.insertScreenshot1(scenarioContext.getScenario(),"screenshot");
        screenshotUtils.insertScreenshot("screenshot");
        screenshotUtils.addLog("searching for String :" + keyword);
    }

    @Then("I should see search results page")
    public void clickSearch() throws IOException, JSONException {
        Uninterruptibles.sleepUninterruptibly(Duration.ofSeconds(4));
        Assert.assertTrue(this.googlePage.isAt());
    }
    @And("I enter the comment {string}")
    public void enterComment(String comment) {
        this.registrationPage.setComments(comment);
    }

    @And("I submit the form")
    public void submit() throws InterruptedException {
        screenshotUtils.insertScreenshot1(scenarioContext.getScenario(),"screenshot");
        screenshotUtils.insertScreenshot("screenshot");
        //Allure.addAttachment("Screenshot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        this.registrationPage.submit();
    }

    @Then("I should see get the confirmation number")
    public void verifyConfirmationNumber() throws InterruptedException {
        boolean isEmpty = StringUtils.isEmpty(this.registrationPage.getConfirmationNumber().trim());
        screenshotUtils.insertScreenshot1(scenarioContext.getScenario(),"screenshot");
        screenshotUtils.insertScreenshot("screenshot");
        Assert.assertFalse(isEmpty);
        Thread.sleep(2000);
    }



}
