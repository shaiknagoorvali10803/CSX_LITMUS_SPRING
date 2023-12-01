package com.csx.page.actions;

import com.csx.page.objects.VisaPageObjects;
import com.csx.springConfig.annotation.LazyAutowired;
import com.csx.springConfig.annotation.Page;
import com.csx.stepdefinitions.ScenarioContext;
import com.csx.test.util.ScreenshotUtils;
import com.csx.test.util.SeleniumUtil;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Objects;
@Page
public class VisaPageActions {
    @LazyAutowired
    ScreenshotUtils screenshotUtils;
    @Autowired
    WebDriver driver;

    @Autowired
    ScenarioContext scenarioContext;

    @LazyAutowired
    VisaPageObjects pageObjects;

    @Autowired
    WebDriverWait wait;
    @PostConstruct
    private void init(){
        PageFactory.initElements(this.driver, this.pageObjects);
    }
    public void setNames(String firstName, String lastName){
        pageObjects.firstName.sendKeys(firstName);
        pageObjects.lastName.sendKeys(lastName);
    }

    public void setCountryFromAndTo(String countryFrom, String countryTo){
        new Select(pageObjects.fromCountry).selectByValue(countryFrom);
        new Select(pageObjects.toCountry).selectByValue(countryTo);
    }

    public void setBirthDate(LocalDate localDate){
        new Select(pageObjects.year).selectByVisibleText(String.valueOf(localDate.getYear()));
        new Select(pageObjects.day).selectByVisibleText(String.valueOf(localDate.getDayOfMonth()));
        new Select(pageObjects.month).selectByValue(localDate.getMonth().toString());
        screenshotUtils.insertScreenshot1(scenarioContext.getScenario(),"screenshot");
    }
    public void setContactDetails(String email, String phone){
        pageObjects.email.sendKeys(email);
        pageObjects.phone.sendKeys(phone);
    }

    public void setComments(String comments){
        pageObjects.comments.sendKeys(Objects.toString(comments, ""));
    }

    public void submit(){
        SeleniumUtil.clickElementByJS(driver,pageObjects.submit);
    }

    public String getConfirmationNumber(){
        this.wait.until((d) -> pageObjects.requestNumber.isDisplayed());
        return pageObjects.requestNumber.getText();
    }


}
