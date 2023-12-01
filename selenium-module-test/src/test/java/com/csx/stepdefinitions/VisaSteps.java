package com.csx.stepdefinitions;

import com.csx.page.actions.GooglePageActions;
import com.csx.page.actions.VisaPageActions;
import com.csx.springConfig.annotation.LazyAutowired;
import com.csx.test.util.ScreenshotUtils;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.testng.Assert;

import java.time.LocalDate;

public class VisaSteps {
    @Autowired
    protected WebDriver driver;

    @Autowired
    protected WebDriverWait wait;

    @LazyAutowired
    private VisaPageActions registrationPage;

    @Autowired
    ScreenshotUtils screenshotUtils;
    @LazyAutowired
    private GooglePageActions googlePage;

    Scenario scenario;

    @LazyAutowired
    ScenarioContext scenarioContext;

    @PostConstruct
    private void init() {
        PageFactory.initElements(this.driver, this);
    }

    @Before
    public void settingScenario(Scenario scenario) {
        this.scenario=scenario;
        scenarioContext.setScenario(scenario);
        System.out.println("scenarion object in Visa page By : ==>"+ scenario );
    }

    @Given("I am on VISA registration form")
    public void launchSite() throws InterruptedException {
        this.driver.navigate().to("https://vins-udemy.s3.amazonaws.com/sb/visa/udemy-visa.html");
        screenshotUtils.insertScreenshot1(scenario,"screenshot");
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

    @And("I enter the comment {string}")
    public void enterComment(String comment) {
        this.registrationPage.setComments(comment);
    }

    @And("I submit the form")
    public void submit() throws InterruptedException {
        screenshotUtils.insertScreenshot1(scenario,"screenshot");
        screenshotUtils.insertScreenshot("screenshot");
        //Allure.addAttachment("Screenshot", new ByteArrayInputStream(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
        this.registrationPage.submit();
    }

    @Then("I should see get the confirmation number")
    public void verifyConfirmationNumber() throws InterruptedException {
        boolean isEmpty = StringUtils.isEmpty(this.registrationPage.getConfirmationNumber().trim());
        screenshotUtils.insertScreenshot1(scenario,"screenshot");
        screenshotUtils.insertScreenshot("screenshot");
        Assert.assertFalse(isEmpty);
        Thread.sleep(2000);
    }

}
