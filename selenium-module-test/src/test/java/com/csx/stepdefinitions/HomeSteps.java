package com.csx.stepdefinitions;

import com.csx.page.actions.HomePageActions;
import com.csx.springConfig.annotation.LazyAutowired;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

public class HomeSteps {
    @LazyAutowired
    private HomePageActions homePage;
    @Autowired
    protected WebDriver driver;

    @Autowired
    protected WebDriverWait wait;

    @Autowired
    ScenarioContext scenarioContext;

    @PostConstruct
    private void init(){
        PageFactory.initElements(this.driver, this);
    }

    @Given("I am Google Page")
    public void launchSite() {
        this.homePage.goTo();
         }

    @When("Search for the Word {string}")
    public void enterKeyword(String keyword) throws InterruptedException {
        this.homePage.search(keyword);
    }

}
