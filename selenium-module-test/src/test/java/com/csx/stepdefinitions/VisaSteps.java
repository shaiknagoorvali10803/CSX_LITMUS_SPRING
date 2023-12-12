package com.csx.stepdefinitions;

import com.csx.page.actions.GooglePageActions;
import com.csx.page.actions.VisaPageActions;
import com.csx.springConfig.annotation.LazyAutowired;
import com.csx.test.util.ScreenshotUtils;
import com.csx.test.util.SeleniumUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class VisaSteps {
    @Autowired
    private WebDriver driver;

    @Autowired
    private WebDriverWait wait;

    @LazyAutowired
    private VisaPageActions registrationPage;

    @Autowired
    private ScreenshotUtils screenshotUtils;
    @LazyAutowired
    private GooglePageActions googlePage;

    @LazyAutowired
    private ScenarioContext scenarioContext;

    @PostConstruct
    private void init() {
        PageFactory.initElements(this.driver, this);
    }

    @Given("I am on VISA registration form")
    public void launchSite() throws InterruptedException {
        this.driver.navigate().to("https://vins-udemy.s3.amazonaws.com/sb/visa/udemy-visa.html");
        screenshotUtils.insertScreenshot1(scenarioContext.getScenario(),"screenshot");
        screenshotUtils.insertScreenshot("screenshot");
    }

    @When("I select my from country {string} and to country {string}")
    public void selectCountry(String from, String to) {
        this.registrationPage.setCountryFromAndTo(from, to);
    }

    @And("I enter my dob as {string}")
    public void enterDob(String dob) {
        this.registrationPage.setBirthDate(LocalDate.parse(dob));
    }

    @And("I enter my name as {string} and {string}")
    public void enterNames(String fn, String ln) {
        this.registrationPage.setNames(fn, ln);
    }

    @And("I enter my contact details as {string} and {string}")
    public void enterContactDetails(String email, String phone) {
        this.registrationPage.setContactDetails(email, phone);
    }


    @Then("I should see at least {int} results")
    public void verifyResults(int count) throws InterruptedException, IOException {
        Assert.assertTrue(this.googlePage.getCount() >= count);
        SeleniumUtil.clickElementbyXPath(driver,"//a[normalize-space()='Images']");
        Thread.sleep(3000);
        screenshotUtils.insertScreenshot1(scenarioContext.getScenario(),"screenshot");
        screenshotUtils.insertScreenshot("screenshot");
        driver.findElement(By.xpath("//a[normalize-space()='Videos']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='result-stats']")));
        screenshotUtils.insertScreenshot1(scenarioContext.getScenario(),"screenshot");
        screenshotUtils.insertScreenshot("screenshot");
        screenshotUtils.addLog(Arrays.asList("nagoor", "rubia", "nazim", "rayan"));

    }

}
