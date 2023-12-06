package com.csx.page.actions;

import com.csx.page.objects.HomePageObjects;
import com.csx.springConfig.annotation.LazyAutowired;
import com.csx.springConfig.annotation.Page;
import com.csx.stepdefinitions.ScenarioContext;
import com.csx.test.util.ScreenshotUtils;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
@Page
public class HomePageActions {
    @LazyAutowired
    ScreenshotUtils screenshotUtils;

    @Autowired
    WebDriver driver;

    @Autowired
    WebDriverWait wait;

    @LazyAutowired
    HomePageObjects pageObjects;

    @Autowired
    ScenarioContext scenarioContext;

    @Value("${application.url}")
    public String url;

    @PostConstruct
    private void init(){
        PageFactory.initElements(this.driver, this.pageObjects);
    }

    public void goTo(){
        this.driver.get(url);
    }

    public void search(final String keyword) throws InterruptedException {
        pageObjects.searchBox.sendKeys(keyword);
        screenshotUtils.insertScreenshot1(scenarioContext.getScenario(),"screenshot");
        pageObjects.searchBox.sendKeys(Keys.TAB);
        pageObjects.searchBtns
                .stream()
                .filter(e -> e.isDisplayed() && e.isEnabled())
                .findFirst()
                .ifPresent(WebElement::click);
        Thread.sleep(3000);
        screenshotUtils.insertScreenshot1(scenarioContext.getScenario(),"screenshot");
    }

}
