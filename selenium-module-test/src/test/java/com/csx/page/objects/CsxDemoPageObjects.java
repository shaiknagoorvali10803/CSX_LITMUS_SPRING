package com.csx.page.objects;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import javax.inject.Singleton;

@Singleton
public class CsxDemoPageObjects {

  @FindBy(how = How.XPATH, using = "//*[@id=\"header_logo\"]/a/img")
  public WebElement csxImage;
}
