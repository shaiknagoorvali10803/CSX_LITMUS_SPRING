package com.csx.page.actions;

import com.csx.page.objects.CsxDemoPageObjects;
import com.csx.test.util.WebDriverProvider;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 * Page actions always has to be singleton
 *
 */
@Singleton
public class CsxDemoPageActions {

  @Inject
  private CsxDemoPageObjects pageObjects;

  @Inject
  WebDriverProvider driverProvider;

  /**
   * Because WELD injects objects after constructor is called
   */
  @PostConstruct
  private void setup() {
    PageFactory.initElements(driverProvider.getInstance(), pageObjects);
  }

  public boolean isHeaderImagePresent() {
    final WebDriverWait driverWait = new WebDriverWait(driverProvider.getInstance(), 10);
    if (driverWait.until(ExpectedConditions.visibilityOf(pageObjects.csxImage)).isDisplayed()) {
      return  pageObjects.csxImage.isDisplayed();
    }
    return false;
  }
}
