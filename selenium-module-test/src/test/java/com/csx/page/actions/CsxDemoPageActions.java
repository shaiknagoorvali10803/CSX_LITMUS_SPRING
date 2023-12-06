package com.csx.page.actions;

import com.csx.page.objects.CsxDemoPageObjects;
import com.csx.springConfig.annotation.LazyAutowired;
import com.csx.springConfig.annotation.Page;
import com.csx.stepdefinitions.ScenarioContext;
import com.csx.test.util.ScreenshotUtils;
import com.csx.test.util.SeleniumUtil;
import io.cucumber.java.Scenario;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

@Page
public class CsxDemoPageActions {

  @LazyAutowired
  private CsxDemoPageObjects pageObjects;

  @Autowired
  private WebDriver driver;
  @Autowired
  private WebDriverWait wait;
  @LazyAutowired
  private SeleniumUtil utils;

  @LazyAutowired
  ScreenshotUtils screenshotUtils;

  @Autowired
  ScenarioContext scenarioContext;

  @PostConstruct
  private void init(){
    PageFactory.initElements(this.driver, this.pageObjects);
  }

  public boolean isHeaderImagePresent() {
    return SeleniumUtil.isElementDisplayed(driver,pageObjects.csxImage);
  }
}
