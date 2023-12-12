package com.csx.page.actions;

import com.csx.page.objects.GooglePageObjects;
import com.csx.springConfig.annotation.LazyAutowired;
import com.csx.springConfig.annotation.Page;
import com.csx.stepdefinitions.ScenarioContext;
import com.csx.test.util.ScreenshotUtils;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.BooleanUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.MalformedURLException;
import java.net.URL;

@Page
public class GooglePageActions {
    @Autowired
    private WebDriver driver;
    @Autowired
    private WebDriverWait wait;
    @LazyAutowired
    private ScreenshotUtils screenshotUtils;
    @Autowired
    private ScenarioContext scenarioContext;
    @LazyAutowired
    private GooglePageObjects pageObjects;

    @Value("${application.url}")
    private String url;

    @Value("${headless}")
    private String headless;

    @PostConstruct
    private void init() {
        PageFactory.initElements(this.driver, this.pageObjects);
     }
    public void goTo() throws InterruptedException {
        if (BooleanUtils.toBoolean(headless)) {
            driver.get("https://chromedriver.storage.googleapis.com/index.html?path=79.0.3945.36/");
            Thread.sleep(2000);
            WebElement btnDownload = driver.findElement(By.xpath(".//a[text()='chromedriver_win32.zip']"));
            btnDownload.click();
            Thread.sleep(5000);
        }
        this.driver.navigate().to(url);
    }
    public void search(final String keyword){
        pageObjects.searchBox.sendKeys(keyword);
        screenshotUtils.insertScreenshot1(scenarioContext.getScenario(),"screenshot");
        pageObjects.searchBox.sendKeys(Keys.TAB);
        pageObjects.searchBtns
                .stream()
                .filter(e -> e.isDisplayed() && e.isEnabled())
                .findFirst()
                .ifPresent(WebElement::click);
    }
    public int getCount(){

        return pageObjects.results.size();


    }
    public boolean isAt() {
        return this.wait.until((d) -> pageObjects.searchBox.isDisplayed());
    }
}
