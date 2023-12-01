package com.csx.test.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
-------------------------Author: Perraj Kumar K (S9402)----------------------------------------
 */

public class LogonNonProd {
	public static final String LOGINPAGE_USERNAME_TEXTBOX_OBJECT_ID = "okta-signin-username";
	private static final String LOGINPAGE_PASSWORD_TEXTBOX_OBJECT_ID = "okta-signin-password";
	public static final String LOGINPAGE_LOGIN_BUTTON_OBJECT_ID = "okta-signin-submit";
	public static final String testUserName = "t_client.crew.hr.admin@csx.local";
	public static final String testPassWord = "*20q#ZUAa7qe1a9!";

	public static void logonNonProd(WebDriver driver){
		WebElement userName = driver.findElement(By.id(LOGINPAGE_USERNAME_TEXTBOX_OBJECT_ID));
		WebElement pwd = driver.findElement(By.id(LOGINPAGE_PASSWORD_TEXTBOX_OBJECT_ID));
		WebElement submit = driver.findElement(By.id(LOGINPAGE_LOGIN_BUTTON_OBJECT_ID));
		SeleniumUtil.isElementDisplayed(driver, pwd);
		SeleniumUtil.setValueToElement(driver, userName, testUserName);
		SeleniumUtil.setValueToElement(driver, pwd, testPassWord);
		SeleniumUtil.clickElementbyWebElementWithOutSendKeys(driver, submit);
	}
}
