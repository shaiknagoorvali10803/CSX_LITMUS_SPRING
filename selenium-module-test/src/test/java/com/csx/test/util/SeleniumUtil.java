package com.csx.test.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.fail;

public class SeleniumUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumUtil.class);
    private static final String SPINNER_XPATH = "//app-block-ui/div/p-blockui";
    public static final String ERROR_MSG = "Some error has occurred while performing operation::{}";
    private static final String DROPDOWN_ITEM_SELECTOR_IN_OVERLAY = "//ul/li[*]/span[text()='%s']";
    private static final String DROPDOWN_PARTIAL_MATCH_ITEM_SELECTOR_IN_OVERLAY = "//ul/li[*]/span[contains(text(),'%s')]";
    private static final String DROPDOWN_OVERLAY = "//ul";
    private static final String DROPDOWN_CLICKABLE_LABEL = "div/label";
    public static final int DRIVER_WAIT_TIME_IN_SECS = 30;
    private static int maxSyncTime = 60;

    /**
     * ---------------------------Maximize Window------------------------------------------
     */
    public static void maximizeWindow(final WebDriver driver) {
        driver.manage().window().maximize();
    }

    /**
     * ---------------------------Resize window for Mobile------------------------------------------
     */
    public static void resizeWindowForMobile(final WebDriver driver) {
        driver.manage().window().setSize(new Dimension(50, 750));
    }

    /**
     * ---------------------------Resize window for Tablet------------------------------------------
     */
    public static void resizeWindowForTablet(final WebDriver driver) {
        driver.manage().window().setSize(new Dimension(700, 750));
    }

    /**
     * -------------------------------Create New Tab and Switch to new tab -----------------Author: Perraj Kumar K (S9402)---------------------
     */
    public static void switchNewTab(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("window.open()");
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * Click on the WebElement With ID and handling WebDriverWait to handle
     * NoSuchElementException Passing Element as String
     */
    public static void clickElementByID(final WebDriver driver, final String elementID) {
        try {
            waitUntilClickById(driver, elementID);
            driver.findElement(By.id(elementID)).click();
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * Click on the WebElement With XPATH and handling WebDriverWait to handle
     * NoSuchElementException Passing Element as String
     */
    public static void clickElementbyXPath(final WebDriver driver, final String xpath) {
        try {
            waitUntilClickByXpath(driver, xpath);
            driver.findElement(By.xpath(xpath)).click();
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * Click on the WebElement With XPATH and handling WebDriverWait to handle
     * NoSuchElementException Passing Element as WebElement
     */
    public static void clickElementbyWebElement(final WebDriver driver, final WebElement webElement) {
        try {
            waitUntilClickByElement(driver, webElement);
            webElement.sendKeys("");
            webElement.click();
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        } catch (final Exception e) {
            clickElementbyWebElementWithOutSendKeys(driver, webElement);
        }
    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * Click on the WebElement With XPATH and handling WebDriverWait to handle
     * NoSuchElementException Passing Element as WebElement This method doesn't use
     * sendKeys
     */
    public static void clickElementbyWebElementWithOutSendKeys(final WebDriver driver, final WebElement webElement) {
        try {
            waitUntilClickByElement(driver, webElement);
            webElement.click();
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * ---------------------------------------------------------------------------------------------------
     * Submit Element by XPATH
     */
    public static void submitElementbyXPath(final WebDriver driver, final String xpath) {
        waitUntilClickByXpath(driver, xpath);
        final JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", driver.findElement(By.xpath(xpath)));
    }

    /**
     * ----------------------------------------------------------------------------------------------------
     * Scroll till Web Element
     */
    public static void scrollToWebElement(final WebDriver driver, final WebElement webelement) {
        waitUntilVisibleByElement(driver, webelement);
        final JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].scrollIntoView(true);", webelement);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Validate Element
     */
    public static void validateElement(final WebDriver driver, final String xpath, final String testCaseName,
                                       final String expectedResult) {
        try {
            waitUntilVisibleByXpath(driver, xpath);
            final WebElement element = driver.findElement(By.xpath(xpath));
            Assertions.assertEquals(expectedResult.trim(), element.getText().trim());
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Set value to Element
     */
    public static void setValueToElementByXpath(final WebDriver driver, final String xpath, final String inputValue) {
        try {
            waitUntilClickByXpath(driver, xpath);
            final WebElement element = driver.findElement(By.xpath(xpath));
            element.clear();
            element.sendKeys(inputValue);
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    public static void setValueToElementByXpath(final WebDriver driver, final WebElement element,
                                                final String inputValue) {
        try {
            waitUntilClickByElement(driver, element);
            element.clear();
            element.sendKeys(inputValue);
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * ---------------------------------------------------------------------------------------------------------
     * Set Value to WebElement using XPATH and handling WebDriverWait to handle
     * NoSuchElementException
     */
    public static void setValueToElement(final WebDriver driver, final WebElement webElement, final String inputValue) {
        try {
            waitUntilClickByElement(driver, webElement);
            webElement.clear();
            webElement.sendKeys(inputValue);
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get value by element
     */
    public static String getValueByElement(final WebDriver driver, final WebElement webElement) {
        try {
            if (waitUntilVisibleByElement(driver, webElement).isDisplayed()) {
                return webElement.getText().trim();
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * Is element not empty-
	 */
	public static boolean isElementNotEmpty(final WebDriver driver, final WebElement webElement,
                                            final String elementName) {
		return StringUtils.isNotEmpty(getValueByElement(driver, webElement));
	}

    /**
     * ----------------------------------------------------------------------------
     * Get Text Value of the Input Element using XPATH and handling WebDriverWait to
     * handle NoSuchElementException //Handled the webelement from -
     * ShipmentEnquiryObjectRepo and Actions class
     */
    public static String getInputElementValue(final WebDriver driver, final WebElement webElement) {
        try {
            if (waitUntilVisibleByElement(driver, webElement).isDisplayed()) {
                return webElement.getAttribute("value").trim();
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get Enable Disable by element
     */
    public static Boolean getEnableDisableByElement(final WebDriver driver, final WebElement webElement) {
        try {
            if (waitUntilVisibleByElement(driver, webElement) != null) {
                return webElement.isEnabled();
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return false;
    }

    public static Boolean getEnableDisableByElement(final WebDriver driver, String xPath) {
        try {
            if (waitUntilVisibleByXpath(driver, xPath) != null) {
                WebElement webElement = driver.findElement(By.xpath(xPath));
                return webElement.isEnabled();
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return false;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is Disable by element
     */
    public static Boolean isDisableByElement(final WebDriver driver, final WebElement webElement) {
        try {
            if (waitUntilVisibleByElement(driver, webElement) != null) {
                final String attribute = webElement.getAttribute("disabled");
                return attribute != null && attribute.equalsIgnoreCase("true");
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return false;
    }

    /**
     * ----------------------------------------------------------------------------------------------
     * this method checks if the input's enabled status has changed by overriding
     * the apply method and applying the condition that we are looking for pass
     * testIsEnabled true if checking whether the input has become enabled pass
     * testIsEnabled false if checking whether the input has become disabled
     */
    public static Boolean isEnabledDisabledWaitingForChange(final WebDriver driver, final WebElement webElement,
                                                            final Boolean testIsEnabled) {
        Boolean isEnabled = null;
        try {
            final Boolean isElementEnabled = waitUntilVisibleByElement(driver, webElement).isEnabled();
            isEnabled = testIsEnabled ? isElementEnabled : !isElementEnabled;
        } catch (final TimeoutException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return isEnabled;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is Spinner Enabled
     */
    public static Boolean isSpinnerEnabled(final WebDriver driver, final WebElement webElement) {
        try {
            if (waitUntilVisibleByElement(driver, webElement) != null) {
                final String webElementCSS = getElementsCSS(driver, webElement, "webelemnt CSS");
                if (webElementCSS != null && !webElementCSS.isEmpty()) {
                    return webElementCSS.contains("loadingSpinner");
                } else {
                    return false;
                }
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return false;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Is disabled by element CSS
     */
    public static Boolean isDisabledByElementCSS(final WebDriver driver, final WebElement webElement) {
        try {
            if (waitUntilVisibleByElement(driver, webElement) != null) {
                final String webElementCSS = getElementsCSS(driver, webElement, "webelemnt CSS");
                if (webElementCSS != null && !webElementCSS.isEmpty()) {
                    return webElementCSS.contains("ui-state-disabled");
                } else {
                    return false;
                }
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return false;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Mouse over Element
     */
    public static void mouseOverElement(final WebDriver driver, final WebElement webElement) {
        try {
            waitUntilVisibleByElement(driver, webElement);
            final Actions actObj = new Actions(driver);
            actObj.moveToElement(webElement).build().perform();
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }
    /**
     * -------------------------------------------------------------------------------------------------------
     * Mouse over Element
     */
    public static void mouseOverElement(final WebDriver driver, String xpath) {
        try {
            WebElement element=waitUntilVisibleByXpath(driver, xpath);
            final Actions actObj = new Actions(driver);
            actObj.moveToElement(element).build().perform();
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }
    public static void rightClickAndSelect(WebDriver driver, WebElement selectProElement, WebElement clickOption) {
        Actions action = new Actions(driver);
        action.contextClick(selectProElement).pause(1000).moveToElement(clickOption).click(clickOption).build().perform();
    }

    public static void mouseOverElementByJS(final WebDriver driver, final WebElement element){
        String strJavaScript = "var element = arguments[0];"
                + "var mouseEventObj = document.createEvent('MouseEvents');"
                + "mouseEventObj.initEvent( 'mouseover', true, true );"
                + "element.dispatchEvent(mouseEventObj);";
        JavascriptExecutor js =  (JavascriptExecutor) driver;
        js.executeScript(strJavaScript, element);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Check & Uncheck box
     */
    public static void clickCheckedUnCheckedElement(final WebDriver driver, final WebElement elementID) {
        try {
            waitUntilClickByElement(driver, elementID);
            final boolean chk = elementID.isEnabled();
            if (chk == true) {
                elementID.click();
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Mouse over element select
     */
    public static void mouseOverElementSelect(final WebDriver driver, final WebElement webElement) {
        try {
            waitUntilVisibleByElement(driver, webElement);
            final Actions actObj = new Actions(driver);
            actObj.moveToElement(webElement).build().perform();
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * Navigate to URL
     */
    public static void navigateToURL(final WebDriver driver, final String url) {
        driver.get(url);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Reload page
     */
    public static void reloadPage(final WebDriver driver) {
        driver.navigate().refresh();
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Navigate to URL in new tab
     */
    public static void navigateToURLInNewTab(final WebDriver driver, final String url) {
        driver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
        final ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(0));
        driver.get(url);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Is element present
     */
    public static boolean isElementPresent(final WebDriver driver, final String xPath) {
        try {
            return waitUntilVisibleByElements(driver, xPath).size() > 0;
        } catch (final NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
            return false;
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Is Anchor present
     */
    public static boolean isAnchorPresent(final WebDriver driver, final String text) {
        return SeleniumUtil.isElementPresent(driver, "//a[contains(text(),'" + text + "')]");
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get CSS classes by element
     */
    public static String getCssClassesByElement(final WebDriver driver, final WebElement webElement, final String elementName) {
        try {
            if (waitUntilVisibleByElement(driver, webElement).isDisplayed()) {
                return webElement.getAttribute("class").trim();
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Get CSS value by element
     */
    public static String getCSSValueByElement(final WebDriver driver, final WebElement webElement, final String cssAttributeName, final String elementName) {
        try {
            if (waitUntilVisibleByElement(driver, webElement).isDisplayed()) {
                return webElement.getCssValue(cssAttributeName);
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get data table size by xpath
     */
    public static int getDataTableSizeByXpath(final WebDriver driver, final String tableXPath, final String testCaseName) {
        waitUntilVisibleByElements(driver, tableXPath);
        final List<WebElement> rows = driver.findElements(By.xpath(tableXPath + "/tbody/tr"));
        return rows.size();
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get mobile view card size by xpath
     */
    public static int getMobileViewCardsSizeByXpath(final WebDriver driver, final String cardXPath, final String testCaseName) {
        waitUntilVisibleByElements(driver, cardXPath);
        final List<WebElement> rows = driver.findElements(By.xpath(cardXPath));
        return rows.size();
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * To switch to a different window
     */
    public static void switchToOtherWindow(final WebDriver driver, final int windowNumber) {
        final List<String> browserTabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(browserTabs.get(windowNumber));
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is element displayed
     */
    public static boolean isElementDisplayed(final WebDriver driver, final WebElement webElement) {
        try {
            return waitUntilVisibleByElement(driver, webElement).isDisplayed();
        } catch (final NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
            return false;
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is element not displayed
     */
    public static boolean isElementNotDisplayed(final WebElement webElement) {
        try {
            return !webElement.isDisplayed();
        } catch (final NoSuchElementException ele) {
            return true;
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get element's CSS
     */
    public static String getElementsCSS(final WebDriver driver, final WebElement webElement, final String elementName) {
        try {
            if (waitUntilVisibleByElement(driver, webElement).isDisplayed()) {
                return webElement.getAttribute("class");
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get web element from string path
     */
    public static WebElement getWebElementFromStringPath(final WebDriver driver, final String xpath) {
        waitUntilVisibleByXpath(driver, xpath);
        return driver.findElement(By.xpath(xpath));
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Xpath eists
     */
    public static boolean xPathExists(final WebDriver driver, final String xpath) {
        try {
            waitUntilVisibleByXpath(driver, xpath);
            return true;
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
            return false;
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get value by element no log
     */
    public static String getValueByElementNoLog(final WebDriver driver, final WebElement webElement, final String elementName) {
        try {
            if (waitUntilVisibleByElement(driver, webElement).isDisplayed()) {
                return webElement.getText().trim();
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * check visibility by element
     */
    public static Boolean checkVisibilityByElement(final WebDriver driver, final WebElement webElement) {
        Boolean isVisible = false;
        try {
            if (waitUntilVisibleByElement(driver, webElement) != null) {
                isVisible = true;
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return isVisible;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is element enabled
     */
    public static Boolean isElementEnabled(final WebDriver driver, final WebElement webElement, final String elementName) {
        Boolean isEnabled = false;
        try {
            if (waitUntilVisibleByElement(driver, webElement) != null) {
                isEnabled = webElement.isEnabled();
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return isEnabled;
    }

    public static boolean isElementEnabled(final WebDriver driver, final WebElement webElement,int time) {
        boolean flag =false;
        final WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(time));
        try {
            driverWait.until(ExpectedConditions.visibilityOf(webElement));
            if(webElement.isEnabled()){
                flag=true;
            }
        } catch (Exception ele) {
            flag= false;
        }
        return  flag;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is element not present
     */
    public static Boolean isElementNotPresent(final WebDriver driver, final String xPath) {
        try {
            return waitUntilInvisibleByXpath(driver, xPath);
        } catch (final Exception ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
            return true;
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is element not present with wait
     */
    public static Boolean isElementNotPresentWithWait(final WebDriver driver, final String xPath, final int waitTimeinSec) {
        try {
            return waitUntilInvisibleByXpathByTime(driver, xPath, waitTimeinSec);
        } catch (final Exception ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
            return true;
        }
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is button enabled
     */
    public static Boolean isButtonEnabled(final WebDriver driver, final WebElement webElement) {
        try {
            if (waitUntilVisibleByElement(driver, webElement).isDisplayed()) {
                return webElement.isEnabled();
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get value by xpath
     */
    public static String getValueByXpath(final WebDriver driver, final String xPath) {
        try {
            if (waitUntilVisibleByXpath(driver, xPath).isDisplayed()) {
                final WebElement webElement = driver.findElement(By.xpath(xPath));
                return webElement.getText().trim();
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is form control input field filled and valid
     */
    public static boolean isFormControlInputFieldFilledAndValid(final WebDriver driver, final WebElement webElement) {
        final String fieldCSS = SeleniumUtil.getElementsCSS(driver, webElement, "tsFailedPaymentOopsImage");
        return fieldCSS.contains("ui-state-filled") && !fieldCSS.contains("ng-invalid");
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * This method checks the value in list and return boolean reusult.
     */
    public static boolean checkValueInList(final WebDriver driver, final List<WebElement> webElements, final String expectedValueInList, final String elementName) {
        final Boolean isValueExist = false;
        try {
            if (waitUntilVisibleByElements(driver, webElements) != null) {
                final List<WebElement> elements = webElements;
                for (int i = 0; i < elements.size(); i++) {
                    final String listValue = elements.get(i).getText().trim();
                    if (listValue.contains(expectedValueInList)) {
                        return true;
                    }
                }
                return isValueExist;
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return isValueExist;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get URL of new tab
     */
    public static String getUrlOfNewTab(final WebDriver driver, final int driverWaitTimeInSecs) {
        // get window handlers as list
        final List<String> browserTabs = new ArrayList<>(driver.getWindowHandles());
        // Ship to new tab and check if correct page opened
        String shipmentInstructionsTitle = null;
        driver.switchTo().window(browserTabs.get(2));
        if (waitUntilTitleDisplaysByTime(driver, "Main Page", driverWaitTimeInSecs)) {
            shipmentInstructionsTitle = driver.getTitle();
        }
        driver.switchTo().window(browserTabs.get(0));
        return shipmentInstructionsTitle;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is text there
     */
    public static String isTextThere(final WebDriver driver, final WebElement element, final Boolean testIsEnabled, final String elementName, final String attributeName) {
        final WebDriverWait driverWait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        try {
            driverWait.until(ExpectedConditions.visibilityOf(element));
            driverWait.until(ExpectedConditions.elementToBeClickable(element));
            Thread.sleep(3000);
            return driverWait.until(drivers -> {
                if (element.getAttribute(attributeName).length() > 0) {
                    element.click();
                    return element.getAttribute(attributeName);
                }
                return null;
            });
        } catch (TimeoutException | InterruptedException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get element value by ID
     */
    public static String getElementValueById(final WebDriver driver, final String elementId, final String elementName) {
        try {
            waitUntilVisibleById(driver, elementId);
            final JavascriptExecutor js = (JavascriptExecutor) driver;
            return (String) js.executeScript("return document.getElementById(" + "'" + elementId + "'" + ").value");
        } catch (final TimeoutException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * select value in PrimeNgDropdown
     */
    public static void selectValueInPrimeNgDropDown(final WebDriver driver, final WebElement dropDownElementOrParent,
                                                    final String value) {
        final WebElement dropDownElement = findLabelPrimeNgDropdown(driver, dropDownElementOrParent);
        final WebElement element = dropDownElement
                .findElement(By.xpath(String.format(DROPDOWN_ITEM_SELECTOR_IN_OVERLAY, value)));
        clickElementbyWebElement(driver, element);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * select partial match value in PrimeNgDropdown
     */
    public static void selectPartialMatchValueInPrimeNgDropDown(final WebDriver driver,
                                                                final WebElement dropDownElementOrParent, final String value) {
        final WebElement dropDownElement = findLabelPrimeNgDropdown(driver, dropDownElementOrParent);
        final WebElement element = dropDownElement
                .findElement(By.xpath(String.format(DROPDOWN_PARTIAL_MATCH_ITEM_SELECTOR_IN_OVERLAY, value)));
        clickElementbyWebElement(driver, element);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * find Label PrimeNgDropdown
     */
    private static WebElement findLabelPrimeNgDropdown(final WebDriver driver,
                                                       final WebElement dropDownElementOrParent) {
        WebElement dropDownElement = null;
        if (dropDownElementOrParent != null && "p-dropdown".equalsIgnoreCase(dropDownElementOrParent.getTagName())) {
            dropDownElement = dropDownElementOrParent;
        } else if (dropDownElementOrParent != null) {
            dropDownElement = dropDownElementOrParent.findElement(By.tagName("p-dropdown"));
        } else {
            dropDownElement = driver.findElement(By.tagName("p-dropdown"));
        }
        waitUntilClickByXpath(driver, DROPDOWN_CLICKABLE_LABEL);
        clickElementbyWebElement(driver, dropDownElement.findElement(By.xpath(DROPDOWN_CLICKABLE_LABEL)));
        try {
            Thread.sleep(300);
        } catch (final InterruptedException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        waitUntilVisibleByXpath(driver, DROPDOWN_OVERLAY);
        return dropDownElement;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get PrimeNgDropdown value
     */
    public static String getPrimeNgDropDownValue(final WebDriver driver, final int driverWaitTimeInSecs,
                                                 final WebElement dropDownElementOrParent, final String elementName, final String dropDownLabel) {
        WebElement dropDownElement = null;
        if (dropDownElementOrParent != null && "p-dropdown".equalsIgnoreCase(dropDownElementOrParent.getTagName())) {
            dropDownElement = dropDownElementOrParent;
        } else if (dropDownElementOrParent != null) {
            dropDownElement = dropDownElementOrParent.findElement(By.tagName("p-dropdown"));
        } else {
            dropDownElement = driver.findElement(By.tagName("p-dropdown"));
        }
        waitUntilVisibleByElement(driver, dropDownElement);
        return getValueByElement(driver, dropDownElement.findElement(By.xpath(dropDownLabel)));
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is PrimeNg check box checked
     */
    public static boolean isPrimeNgCheckboxChecked(final WebDriver driver, final WebElement parentElement) {
        WebElement checkBox = null;
        if (parentElement != null) {
            checkBox = parentElement.findElement(By.tagName("p-checkbox"));
        } else {
            checkBox = driver.findElement(By.tagName("p-checkbox"));
        }
        waitUntilVisibleByElement(driver, checkBox);
        return checkBox.findElement(By.xpath("div/div[2]")).getAttribute("class").contains("ui-state-active");
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * high lighter method
     */
    public static void highLighterMethod(final WebDriver driver, final WebElement element) {
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red;');", element);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * Remove high lighter
     */
    public static void removeHighLighter(final WebDriver driver, final WebElement element) {
        final JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background: white;');", element);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get place holder text
     */
    public static String getPlaceHolderText(final WebElement element) {
        return element.getAttribute("placeholder");
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get current screen URL
     */
    public static String getCurrentScreenUrl(final WebDriver driver) {
        waitUntilVisibleByXpath(driver, "//th[contains(text(),' Equipment ')]");
        return driver.getCurrentUrl();

    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * focus out of text area
     */
    public static void focusOutOfTextArea(final WebElement webElement) {
        final WebElement destWebElement = webElement;
        destWebElement.sendKeys(Keys.TAB);
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is spinner loaded
     */
    public static Boolean isSpinnerLoaded(final WebDriver driver, final WebElement webElement, final String elementName) {
        try {
            if (waitUntilInvisibleByElement(driver, webElement) != null) {
                final String webElementCSS = getElementsCSS(driver, webElement, "webelemnt CSS");
                if (webElementCSS != null && !webElementCSS.isEmpty()) {
                    return webElementCSS.contains("loadingSpinner");
                } else {
                    return false;
                }
            }
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return false;
    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * is angular spinner loaded
     */
    public static void waitForApiCallInAngular(final WebDriver driver) {
        try{
            final Wait<WebDriver> wait = new FluentWait<>(driver).pollingEvery(Duration.ofMillis(200))
                    .withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS)).ignoring(NoSuchElementException.class);
            // making sure that spinner is present
            wait.until(d -> ExpectedConditions.visibilityOf(d.findElement(By.xpath(SPINNER_XPATH))));
            // we have to wait until spinner goes away
            waitUntilInvisibleByXpath(driver, SPINNER_XPATH);
        }
        catch (Exception e){
           LOGGER.info("failed in waiting for API call");
        }

    }

    /**
     * -------------------------------------------------------------------------------------------------------
     * get hidden element value by xpath
     */
    public static String getHiddenElementValueByXPath(final WebDriver driver, final String xPath,
                                                      final String elementName) {
        try {
            final JavascriptExecutor js = (JavascriptExecutor) driver;
            final WebElement hiddenDiv = driver.findElement(By.xpath(xPath));
            String value = hiddenDiv.getText();
            final String script = "return arguments[0].innerHTML";
            return value = (String) ((JavascriptExecutor) driver).executeScript(script, hiddenDiv);
        } catch (final TimeoutException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
        return null;
    }



    /**
     * ----This Method is for Sorting of Ascending&Descending Order---------------
     */

    public static void verifyAscendingAndDescending(final WebDriver driver, final String XPATH, final String elementName) {
        try {
            waitUntilVisibleByElements(driver, XPATH);
            final JavascriptExecutor js = (JavascriptExecutor) driver;
            final List<WebElement> AllAscendingAndDescending = driver.findElements(By.xpath(XPATH));
            for (final WebElement AscAndDsc : AllAscendingAndDescending) {
                final String value = AscAndDsc.getText();
                final String script = "return arguments[0].innerHTML";
                LOGGER.info(elementName + ":" + value);
            }
        } catch (final TimeoutException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    public static void clearTextField(final WebDriver driver, final WebElement webElement, final String inputValue) {
        try {
            waitUntilVisibleByElement(driver, webElement);
            webElement.sendKeys(Keys.CONTROL, Keys.chord("a"));
            webElement.sendKeys(Keys.BACK_SPACE);
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * -------------------------------Return the BGColor of the element in terms of Hexa Decimal Value --------------------------------------
     */

    public static String getElementBGColor(final WebDriver driver, final WebElement element) {
        String color = element.getCssValue("background-color");
        String[] hexValue = color.replace("rgba(", "").replace(")", "").split(",");
        hexValue[0] = hexValue[0].trim();
        int hexValue1 = Integer.parseInt(hexValue[0]);
        hexValue[1] = hexValue[1].trim();
        int hexValue2 = Integer.parseInt(hexValue[1]);
        hexValue[2] = hexValue[2].trim();
        int hexValue3 = Integer.parseInt(hexValue[2]);
        String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
        System.err.println("BGColor is :" + actualColor);
        return actualColor;
    }

    /**
     * -------------------------------wait methods --------------------------------------
     */
    public static void waitUntilClickById(final WebDriver driver, final String elementID) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(elementID)));
        wait.until(ExpectedConditions.elementToBeClickable(By.id(elementID)));
    }

    public static void waitUntilClickByXpath(final WebDriver driver, final String xapth) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xapth)));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xapth)));
    }

    public static void waitUntilClickByXpath(final WebDriver driver, final String xapth, int time) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xapth)));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xapth)));
    }

    public static void waitUntilClickByElement(final WebDriver driver, final WebElement webElement) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        wait.until(ExpectedConditions.visibilityOf((webElement)));
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    public static void waitUntilClickByElementByTime(final WebDriver driver, final WebElement webElement, int time) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        wait.until(ExpectedConditions.visibilityOf((webElement)));
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }

    public static WebElement waitUntilClickByLocator(final WebDriver driver, final By locator) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void waitUntilClickByLocatorByTime(final WebDriver driver, final By locator, int time) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitUntilVisibleByXpath(final WebDriver driver, final String Xpath) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpath)));
    }

    public static void waitUntilVisibleById(final WebDriver driver, final String elementID) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(elementID)));
    }

    public static WebElement waitUntilVisibleByLocator(final WebDriver driver, final By Locator) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(Locator));
    }

    public static WebElement waitUntilVisibleByXpathByTIme(final WebDriver driver, final String Xpath, int time) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpath)));
    }

    public static WebElement waitUntilVisibleByElement(final WebDriver driver, final WebElement element) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitUntilVisibleByElementByTIme(final WebDriver driver, final WebElement element, int time) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitUntilPresenceByXpath(final WebDriver driver, final String Xpath) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(Xpath)));
    }

    public static Boolean waitUntilInvisibleByXpath(final WebDriver driver, final String Xpath) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Xpath)));
    }

    public static Boolean waitUntilInvisibleByXpathByTime(final WebDriver driver, final String Xpath, int time) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Xpath)));
    }

    public static List<WebElement> waitUntilVisibleByElements(final WebDriver driver, final List<WebElement> webElements) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        return wait.until(ExpectedConditions.visibilityOfAllElements(webElements));
    }
    public static List<WebElement> waitUntilVisibleByElementsByTime(final WebDriver driver, final List<WebElement> webElements, int time) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        return wait.until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    public static List<WebElement> waitUntilVisibleByElements(final WebDriver driver, final String Xpath) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(Xpath)));
    }
    public static List<WebElement> waitUntilVisibleByElementsByTime(final WebDriver driver, final String Xpath, int time) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(Xpath)));
    }

    public static Boolean waitUntilInvisibleByElement(final WebDriver driver, final WebElement element) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        return wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public static Boolean waitUntilTitleDisplays(final WebDriver driver, final String title) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        return wait.until(ExpectedConditions.titleContains(title));
    }

    public static Boolean waitUntilTitleDisplaysByTime(final WebDriver driver, final String title, int time) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        return wait.until(ExpectedConditions.titleContains(title));
    }

    public static void waitByTime(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            LOGGER.info("Fail in wait due to : " + e);
            Thread.currentThread().interrupt();
        }
    }

    public static FluentWait webDriverFluentWait(WebDriver driver) {
        return new FluentWait(driver)
                .withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS))
                .pollingEvery(Duration.ofSeconds(3))
                .ignoring(NoSuchElementException.class, NoSuchFrameException.class);
    }

    public static FluentWait webDriverFluentWait(WebDriver driver, int time) {
        return new FluentWait(driver)
                .withTimeout(Duration.ofSeconds(time))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class, NoSuchFrameException.class);
    }

    public static List<WebElement> waitForElementsByTime(WebDriver driver, By element, int timeout) {
        new WebDriverWait(driver, Duration.ofSeconds(timeout)).until(ExpectedConditions.visibilityOfElementLocated(element));
        return driver.findElements(element);
    }

    public static List<WebElement> waitForElements(WebDriver driver, By element) {
        return waitForElementsByTime(driver, element, DRIVER_WAIT_TIME_IN_SECS);
    }

    public static void fluentWaitForElementLoading(WebDriver driver, final By byElement) {
        try {
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                    .withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS))
                    .pollingEvery(Duration.ofMillis(5))
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(TimeoutException.class);
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(byElement));
            if (!element.isDisplayed()) {
                LOGGER.info("Element " + byElement + " is not displayed");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to find the element and the reason is " + e);
        }
    }

    public static void waitTillLoadingCompletes(WebDriver driver, final WebElement element) {
        try {
            long start = System.currentTimeMillis();
            int elapsedTime = 0;
            waitByTime(250);
            while (element.isDisplayed()) {
                LOGGER.info("Waiting for Loading Screens to go away");
                if (elapsedTime > maxSyncTime) {
                    break;
                }
                waitByTime(500);
                elapsedTime = Math.round((System.currentTimeMillis() - start) / 1000F);
            }
        } catch (Exception e) {
            if ((e.toString().contains("NoSuchElementException") || e.getMessage().contains("NoStale")))
                LOGGER.info("Loading screenshot is not present now");
            else {
                LOGGER.error(Level.FINEST + "Fail in waitTillLoadingCompletes " + e);
            }
        }
    }

    /**
     * -----wait untill DOM load  ---------
     */
    public static void waitUntilDomLoad(WebDriver driver) {
        LOGGER.info("Inside waitForElementLoading");
        FluentWait fluentWait = readyStateWait(driver);
        if (driver.getTitle().contains("/maintenix/")) {
            ExpectedCondition<Boolean> jQueryLoad;
            try {
                jQueryLoad = webDriver -> ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
            } catch (Exception e) {
                jQueryLoad = webDriver -> (true);
                LOGGER.error("Failed to waitForElementLoading: " + e);
            }
            fluentWait.until(jQueryLoad);
        }
        try {
            ExpectedCondition<Boolean> docLoad =
                    webDriver -> ((Boolean) ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete"));
            fluentWait.until(docLoad);
        } catch (Exception e) {
            LOGGER.error("Failed to waitForElementLoading " + e);
        }
        LOGGER.info("Dom load completed");
    }
    private static FluentWait readyStateWait(WebDriver driver) {
        return new FluentWait(driver).withTimeout(Duration.ofSeconds(15)).pollingEvery(Duration.ofSeconds(1)).ignoring(WebDriverException.class);
    }

    /**
     * Select dropdown options based on the index value
     *
     * @param driver
     * @param element
     * @param index
     */

    public static String selectDropdownByIndexNumber(final WebDriver driver, WebElement element, int index) {
        String selctedValue = null;
        try {
            final Wait<WebDriver> wait =
                    new FluentWait<>(driver).pollingEvery(Duration.ofMillis(200)).withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS))
                            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
            wait.until(ExpectedConditions.visibilityOfAllElements(element));
            highLighterMethod(driver, element);
            Select selectElement = new Select(element);
            selectElement.selectByIndex(index);
            selctedValue = selectElement.getFirstSelectedOption().getText();
        } catch (NoSuchElementException | StaleElementReferenceException
                | TimeoutException e) {
            LOGGER.error(ERROR_MSG, e);
            fail();
        }
        return selctedValue;
    }

    public static String selectDropdownByIndexNumber(final WebDriver driver, String xpath, int index) {
        String selctedValue = null;
        try {
            final Wait<WebDriver> wait =
                    new FluentWait<>(driver).pollingEvery(Duration.ofMillis(200)).withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS))
                            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            WebElement element = driver.findElement(By.xpath(xpath));
            highLighterMethod(driver, element);
            Select selectElement = new Select(element);
            selectElement.selectByIndex(index);
            selctedValue = selectElement.getFirstSelectedOption().getText();
        } catch (NoSuchElementException | StaleElementReferenceException
                | TimeoutException e) {
            LOGGER.error(ERROR_MSG, e);
            fail();
        }
        return selctedValue;
    }

    /**
     * ------------------------------------------------------------------------------------------------------------------
     * Select dropdown options based on visible text
     */

    public static void selectDropdownByText(final WebDriver driver, WebElement element, String text, String... optionalText) {
        try {
            final Wait<WebDriver> wait =
                    new FluentWait<>(driver).pollingEvery(Duration.ofMillis(200)).withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS))
                            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
            wait.until(ExpectedConditions.visibilityOfAllElements(element));
            Select selectElement = new Select(element);
            if (optionalText.length > 0) {
                waitByTime(3000);
                selectElement.selectByVisibleText(optionalText[0]);
            } else {
                selectElement.selectByVisibleText(text.trim());
                WebElement option = selectElement.getFirstSelectedOption();
                String defaultItem = option.getText();
                if (defaultItem.equalsIgnoreCase(text.trim())) {
                    LOGGER.info("Required option " + text + " is selected on the dropdown");
                } else {
                    selectElement.selectByVisibleText(text.trim());
                    LOGGER.info("Required option " + text + " is selected on the dropdown on re-try");
                }
            }
        } catch (NoSuchElementException | StaleElementReferenceException
                | TimeoutException e) {
            LOGGER.error(ERROR_MSG, e);
            fail();
        }
    }

    public static String selectDropdownByValue(final WebDriver driver, WebElement element, String value) {
        String selctedValue = null;
        try {
            final Wait<WebDriver> wait =
                    new FluentWait<>(driver).pollingEvery(Duration.ofMillis(200)).withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS))
                            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
            wait.until(ExpectedConditions.visibilityOfAllElements(element));
            highLighterMethod(driver, element);
            Select selectElement = new Select(element);
            selectElement.selectByVisibleText(value);
            selctedValue = selectElement.getFirstSelectedOption().getText();
        } catch (NoSuchElementException | StaleElementReferenceException
                | TimeoutException e) {
            LOGGER.error(ERROR_MSG, e);
            fail();
        }
        return selctedValue;
    }
    public static String selectDropdownByValue(final WebDriver driver, String xpath, String value) {
        String selctedValue =null;
        try {
            final Wait<WebDriver> wait =
                    new FluentWait<>(driver).pollingEvery(Duration.ofMillis(200)).withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS))
                            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            WebElement element= driver.findElement(By.xpath(xpath));
            highLighterMethod(driver, element);
            Select selectElement = new Select(element);
            selectElement.selectByVisibleText(value);
            selctedValue =selectElement.getFirstSelectedOption().getText();
        } catch (NoSuchElementException | StaleElementReferenceException
                 | org.openqa.selenium.TimeoutException e) {
            LOGGER.error(ERROR_MSG, e);
            fail();
        }
        return selctedValue;
    }

    public static String selectDropdownByValueReturnOption(final WebDriver driver, String xpath, String value) {
        String selctedValue = null;
        try {
            final Wait<WebDriver> wait =
                    new FluentWait<>(driver).pollingEvery(Duration.ofMillis(200)).withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS))
                            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            WebElement element = driver.findElement(By.xpath(xpath));
            highLighterMethod(driver, element);
            Select selectElement = new Select(element);
            selectElement.selectByVisibleText(value);
            selctedValue = selectElement.getFirstSelectedOption().getText();
        } catch (NoSuchElementException | StaleElementReferenceException
                | TimeoutException e) {
            LOGGER.error(ERROR_MSG, e);
            fail();
        }
        return selctedValue;
    }
    public static void selectDropdownByValueReturnVoid(final WebDriver driver, String xpath, String value) {
        try {
            final Wait<WebDriver> wait =
                    new FluentWait<>(driver).pollingEvery(Duration.ofMillis(200)).withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS))
                            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            WebElement element = driver.findElement(By.xpath(xpath));
            highLighterMethod(driver, element);
            Select selectElement = new Select(element);
            selectElement.selectByVisibleText(value);
        } catch (NoSuchElementException | StaleElementReferenceException
                | TimeoutException e) {
            LOGGER.error(ERROR_MSG, e);
            fail();
        }
    }
    public static void selectDropdownByValueReturnVoid(final WebDriver driver, WebElement element, String value) {
        try {
            final Wait<WebDriver> wait =
                    new FluentWait<>(driver).pollingEvery(Duration.ofMillis(200)).withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS))
                            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
            wait.until(ExpectedConditions.visibilityOf(element));
            highLighterMethod(driver, element);
            Select selectElement = new Select(element);
            selectElement.selectByVisibleText(value);
        } catch (NoSuchElementException | StaleElementReferenceException
                | TimeoutException e) {
            LOGGER.error(ERROR_MSG, e);
            fail();
        }
    }

    public static String selectDropdownByValueFromList(final WebDriver driver, WebElement element, List<String> values) {
        String selctedValue = null;
        try {
            final Wait<WebDriver> wait =
                    new FluentWait<>(driver).pollingEvery(Duration.ofMillis(200)).withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS))
                            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
            wait.until(ExpectedConditions.visibilityOfAllElements(element));
            highLighterMethod(driver, element);
            Select selectElement = new Select(element);
            for (String value : values) {
                try {
                    selectElement.selectByVisibleText(value);
                } catch (Exception e) {
                    LOGGER.info("failed in selecting the :" + value);
                    continue;
                }
            }
            selctedValue = selectElement.getFirstSelectedOption().getText();
        } catch (NoSuchElementException | StaleElementReferenceException
                | TimeoutException e) {
            LOGGER.error(ERROR_MSG, e);
            fail();
        }
        return selctedValue;
    }

    /**
     * Select value from dropdown
     */
    public static List<String> getAllOptions(WebDriver driver, WebElement dropdown) {
        try {
            Select options = new Select(dropdown);
            highLighterMethod(driver, dropdown);
            List<WebElement> values = options.getOptions();
            List<String> dropdownValue = new ArrayList<>();
            for (WebElement value : values) {
                dropdownValue.add(getValueByElement(driver, value));
            }
            return dropdownValue;
        } catch (Exception e) {
            LOGGER.error("Getting error while using Select tag", dropdown);
            fail();
            return null;
        }
    }

    /**
     * Select value from dropdown byxpath
     */
    public static List<String> getAllOptions(WebDriver driver, String xpath) {
        WebElement dropdown = getElement(driver, xpath);
        try {
            Select options = new Select(dropdown);
            highLighterMethod(driver, dropdown);
            List<WebElement> values = options.getOptions();
            List<String> dropdownValue = new ArrayList<>();
            for (WebElement value : values) {
                dropdownValue.add(getValueByElement(driver, value));
            }
            return dropdownValue;
        } catch (Exception e) {
            LOGGER.error("Getting error while using Select tag", dropdown);
            fail();
            return null;
        }
    }

    /**
     * ---------------------------------------------------------------------------------------------------
     * This Method is using action class and then will move to element and click on it
     */

    public static void moveToElementAndclick(final WebDriver driver, WebElement webElement) {
        try {
            final Wait<WebDriver> wait =
                    new FluentWait<>(driver).pollingEvery(Duration.ofMillis(200)).withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS))
                            .ignoring(NoSuchElementException.class).ignoring(StaleElementReferenceException.class);
            wait.until(ExpectedConditions.elementToBeClickable(webElement));
            Actions action = new Actions(driver);
            action.moveToElement(webElement).click().build().perform();
        } catch (TimeoutException e) {
            LOGGER.error(ERROR_MSG, e);
            fail();
        }
    }

    public static void setValueToElementByJS(WebDriver driver, WebElement element, String value) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        waitUntilClickByElement(driver, element);
        executor.executeScript("arguments[0].scrollIntoView(true);", element);
        executor.executeScript("arguments[0].value='" + value + "';", element);
    }

    public static void setValueToElementByJS(WebDriver driver, String xpath, String value) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        waitUntilClickByXpath(driver, xpath);
        WebElement element = driver.findElement(By.xpath(xpath));
        executor.executeScript("arguments[0].scrollIntoView(true);", element);
        executor.executeScript("arguments[0].value='" + value + "';", element);
    }

    public static WebElement getElement(WebDriver driver, String xpath) {
        return waitUntilVisibleByXpath(driver, xpath);
    }

    public static List<WebElement> getElements(WebDriver driver, String xpath) {
        return waitUntilVisibleByElements(driver, xpath);
    }

    public static List<WebElement> getElements(WebDriver driver, String xpath,int waitDelay) {
        List<WebElement> webElements = waitUntilVisibleByElementsByTime(driver, xpath, waitDelay);
        return webElements;
    }

    public static boolean isElementVisible(final WebDriver driver, final WebElement webElement) {
        try {
            waitUntilVisibleByElement(driver, webElement);
            return true;
        } catch (Exception ele) {
            return false;
        }
    }

    public static boolean isElementVisible(final WebDriver driver, String xpath) {
        try {
            waitUntilVisibleByXpath(driver, xpath);
            return true;
        } catch (Exception ele) {
            return false;
        }
    }

    public static boolean isElementVisible(final WebDriver driver, String xpath, int duration) {
        try {
            waitUntilVisibleByXpathByTIme(driver, xpath,duration);
            return true;
        } catch (Exception ele) {
            return false;
        }
    }

    public static boolean isElementVisible(final WebDriver driver, final WebElement webElement, int duration) {
        try {
            waitUntilVisibleByElementByTIme(driver, webElement,duration);
            return true;
        } catch (Exception ele) {
            return false;
        }
    }

    public static List<String> getAllOptionsNonSelect(WebDriver driver, WebElement dropdown, List<WebElement> elements) {
        List<String> dropdownValue = new ArrayList<>();
        try {
            clickElementbyWebElement(driver, dropdown);
            for (WebElement value : elements) {
                waitByTime(250);
                dropdownValue.add(getValueByElement(driver, value));
            }
            return dropdownValue;
        } catch (Exception e) {
            e.printStackTrace();
            return dropdownValue;
        }
    }

    public static List<String> getValuesFromList(WebDriver driver, List<WebElement> elements) {
        waitUntilVisibleByElements(driver, elements);
        List<String> dropdownValue = new ArrayList<>();
        try {
            for (WebElement value : elements) {
                waitByTime(100);
                highLighterMethod(driver, value);
                dropdownValue.add(getValueByElement(driver, value));
            }
            return dropdownValue;
        } catch (Exception e) {
            e.printStackTrace();
            return dropdownValue;
        }
    }

    public static List<String> getValuesFromList(WebDriver driver, String xpath) {
        waitUntilVisibleByXpath(driver, xpath);
        List<WebElement> elements = driver.findElements(By.xpath(xpath));
        List<String> dropdownValue = new ArrayList<>();
        try {
            for (WebElement value : elements) {
                waitByTime(100);
                highLighterMethod(driver, value);
                dropdownValue.add(getValueByElement(driver, value));
            }
            return dropdownValue;
        } catch (Exception e) {
            e.printStackTrace();
            return dropdownValue;
        }
    }
    public static String replaceCharOccurrence(String text, String replaceFrom, String replaceTo, int occurrenceIndex) {
        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile(replaceFrom);
        Matcher m = p.matcher(text);
        int count = 0;
        while (m.find()) {
            if (count++ == occurrenceIndex - 1) {
                m.appendReplacement(sb, replaceTo);
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static List<String> getValuesFromListWithCount(WebDriver driver, List<WebElement> elements, int noElements) {
        waitUntilVisibleByElements(driver, elements);
        List<String> dropdownValue = new ArrayList<>();
        try {
            for (int j = 0; j < noElements; j++) {
                waitByTime(250);
                dropdownValue.add(getValueByElement(driver, elements.get(j)));
            }
            return dropdownValue;
        } catch (Exception e) {
            e.printStackTrace();
            return dropdownValue;
        }
    }

    public static List<String> getValuesFromListWithCount(WebDriver driver, String xpath, int noElements) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS));
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(xpath)));
        List<WebElement> elements =getElements(driver,xpath);
        List<String> dropdownValue = new ArrayList<>();
        try {
            for (int j = 0; j < noElements; j++) {
                waitByTime(250);
                dropdownValue.add(getValueByElement(driver, elements.get(j)));
            }
            return dropdownValue;
        } catch (Exception e) {
            e.printStackTrace();
            return dropdownValue;
        }
    }

    public static void shiftFocus(WebDriver driver) {
        Actions action = new Actions(driver);
        action.sendKeys(Keys.TAB).perform();
    }
    public static void waitTillLoadingCompletes(WebElement element) {
        try {
            long start = System.currentTimeMillis();
            int elapsedTime = 0;
           waitByTime(500);
            while (element.isDisplayed()) {
                if (elapsedTime > maxSyncTime) {
                    break;
                }
                waitByTime(500);
                elapsedTime = Math.round((System.currentTimeMillis() - start) / 1000F);
            }
        } catch (Exception e) {
            if ((e.toString().contains("NoSuchElementException") || e.getMessage().contains("NoStale"))) {
            } else {

            }
        }
    }

    public static void waitForElementsDisplayed(WebDriver driver, List<WebElement> elements) {
        try {
            waitUntilVisibleByElements(driver, elements);
        } catch (Exception e) {
            LOGGER.info("no Elements visible ");
        }
    }

    public static void waitForElementsDisplayed(WebDriver driver, String xpath) {
        try {
            waitUntilVisibleByElements(driver, xpath);
        } catch (Exception e) {
            LOGGER.info("no Elements visible ");
        }
    }

    public static void waitForElementsDisplayed(WebDriver driver, String xpath, int time) {
        try {
            waitUntilVisibleByElementsByTime(driver, xpath,time);
        } catch (Exception e) {
            LOGGER.info("no Elements visible ");
        }
    }

    public static void waitForElementsDisplayed(WebDriver driver, List<WebElement> elements, int time) {
         try {
             waitUntilVisibleByElementsByTime(driver, elements,time);
        } catch (Exception e) {
            LOGGER.info("no Elements visible ");
        }
    }

    public static Boolean isElementClickable(WebDriver driver, WebElement element) {
        Boolean flag = false;
        waitUntilVisibleByElement(driver, element);
        try {
            if (isElementEnabled(driver, element, "")) {
                element.click();
                shiftFocus(driver);
                flag = true;
            }
        } catch (Exception e) {
            LOGGER.info("Element is not clickable" + element.toString());
            e.printStackTrace();
        }
        return flag;
    }

    public static Boolean isElementClickable(WebDriver driver, String xpath) {
        Boolean flag = false;
        WebElement element = null;
        waitUntilVisibleByXpath(driver, xpath);
        try {
            element = driver.findElement(By.xpath(xpath));
            if (isElementEnabled(driver, element, "")) {
                element.click();
                shiftFocus(driver);
                flag = true;
            }
        } catch (Exception e) {
            LOGGER.info("Element is not clickable" + element.toString());
            e.printStackTrace();
        }
        return flag;
    }

    public static Boolean isElementClickable(WebDriver driver, String xpath, int time) {
        Boolean flag = false;
        WebElement element = null;
        waitUntilVisibleByXpathByTIme(driver, xpath,time);
        try {
            element = driver.findElement(By.xpath(xpath));
            if (isElementEnabled(driver, element, "")) {
                element.click();
                shiftFocus(driver);
                flag = true;
            }
        } catch (Exception e) {
            LOGGER.info("Element is not clickable" + element.toString());
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * select any random element from list of elements
     */
    public static WebElement selectRandomElementFromList(List<WebElement> elementList) {

        int size = elementList.size();
        int randomElement = ThreadLocalRandom.current().nextInt(0, size);
        return elementList.get(randomElement);
    }

    public static int generateRandomNumber(int min, int max) {
        Random random = new Random();
        int randomNum = random.nextInt(max - min + 1) + min;
        return randomNum;
    }

    /**
     * ---------------------------------------------------------------------------------------------------
     * This method gets the value of the attribute passed in parameter
     */

    public static String getValueByAttribute(WebElement element, String attribute) {
        try {
            LOGGER.info("attribute : " + attribute + " has value as: " + element.getAttribute(attribute));
            return element.getAttribute(attribute);
        } catch (Exception e) {
            LOGGER.error("Getting error while using getting attribute", attribute);
            fail();
            return null;
        }
    }

    /**
     * this method generates random String between the give values
     */
    public static String generateRandomIntegerAsString(int min, int max) {
        Random random = new Random();
        String randomNumber = String.valueOf(random.nextInt(max - min) + min);
        LOGGER.info("randomly generated number between " + min + " - " + max + " is: " + randomNumber);
        return randomNumber;
    }

    /**
     * Scroll till Web Element
     */
    public static void scrollinHeight(final WebDriver driver, final int value) {
        final JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("window.scrollBy(0," + value + ")", "");
    }

    /**
     * This method zoom in the screen
     */
    public static void zoomIN() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_SUBTRACT);
            robot.keyRelease(KeyEvent.VK_SUBTRACT);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        } catch (Exception e) {
            LOGGER.error("Robot class gives error");
        }
    }

    /**
     * perform click on element using java script executor
     */
    public static void clickElementByJS(final WebDriver driver, final WebElement elementID) {
        final JavascriptExecutor executor = (JavascriptExecutor) driver;
        try {
            waitUntilClickByElement(driver, elementID);
            executor.executeScript("arguments[0].click();", elementID);
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    public static void clickElementByJS(final WebDriver driver, String xpath) {
        final JavascriptExecutor executor = (JavascriptExecutor) driver;
        WebElement elementID = null;
        try {
            waitUntilClickByXpath(driver, xpath);
            elementID = driver.findElement(By.xpath(xpath));
            executor.executeScript("arguments[0].click();", elementID);
        } catch (TimeoutException | NoSuchElementException ele) {
            LOGGER.error(ERROR_MSG, ele);
            fail();
        }
    }

    /**
     * -----double click operation ---------
     */
    public static void doubleclick_AllElements(WebDriver driver, String xpath, String colName) throws InterruptedException {
        List<WebElement> findElements = driver.findElements(By.xpath(xpath));
        for (WebElement webElement : findElements) {
            if (webElement.getText().equals(colName)) {
                doubleClick(driver,webElement);
                waitByTime(500);
            }
        }

    }

     /**
     * -----Double click using Actions class  ---------
     */
    public static void doubleClick(final WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        waitUntilVisibleByElement(driver, element);
        waitByTime(1000);
        if (!isElementVisible(driver,element)) {
            scrollToWebElement(driver,element);
        }
        waitUntilVisibleByElement(driver, element);
        if (isElementVisible(driver,element)) {
            highLighterMethod(driver,element);
            actions.moveToElement(element).build();
            actions.doubleClick(element).build().perform();
        } else {
            waitUntilVisibleByElement(driver, element);
            highLighterMethod(driver,element);
            actions.moveToElement(element).build();
            actions.doubleClick(element).build().perform();
        }
    }
    public static void rightClick(WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        waitUntilVisibleByElement(driver, element);
        waitByTime(1000);
        if (!isElementVisible(driver, element)) {
            scrollToWebElement(driver, element);
        }
        waitUntilVisibleByElement(driver, element);
        if (isElementVisible(driver, element)) {
            actions.moveToElement(element).build();
            actions.click(element).build().perform();
        } else {
            waitUntilVisibleByElement(driver, element);
            actions.moveToElement(element).build();
            actions.contextClick(element).build().perform();
        }
    }

    public static void singleClick(WebDriver driver, WebElement element) {
        Actions actions = new Actions(driver);
        waitUntilVisibleByElementByTIme(driver, element, maxSyncTime);
        waitByTime(1000);
        if (!isElementVisible(driver, element)) {
            scrollToWebElement(driver, element);
        }
        waitUntilVisibleByElementByTIme(driver, element, maxSyncTime);
        if (isElementVisible(driver, element)) {
            actions.moveToElement(element).build();
            actions.click(element).build().perform();
        } else {
            waitUntilVisibleByElementByTIme(driver, element, maxSyncTime);
            actions.moveToElement(element).build();
            actions.click(element).build().perform();
        }
    }

    public static String getSelectedValue(WebDriver driver, WebElement element) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(maxSyncTime));
        wait.until(ExpectedConditions.elementToBeClickable(element));
        Select select = new Select(element);
        String text = getValueByElement(driver, select.getFirstSelectedOption());
        return text;
    }

    public void changefocus(final WebDriver driver) {
        Actions actions = new Actions(driver);
        actions.moveByOffset(20, 8).click().build().perform();
    }
    public static void switchFrame(WebDriver driver, WebElement element) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(maxSyncTime));
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(element));
        driver.switchTo().frame(element);

    }
    public static void rightClickAndElementSelect(WebDriver driver, WebElement element, WebElement elementSel) {
        Actions actions = new Actions(driver);
        waitUntilVisibleByElement(driver, element);
        waitByTime(1000);
        if (!isElementVisible(driver, element)) {
            scrollToWebElement(driver, element);
        }
        waitUntilVisibleByElement(driver, element);
        if (isElementVisible(driver, element)) {
            actions.moveToElement(element).build();
            actions.click(element).build().perform();
        } else {
            waitUntilVisibleByElement(driver, element);
            actions.moveToElement(element).build();
            actions.contextClick(element).pause(1000).click(elementSel).build().perform();
        }
    }

    public static List<String> getElementsText(WebDriver driver, List<WebElement> elements) {
        List<String> elementText = new ArrayList<>();
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(maxSyncTime));
        wait.until(ExpectedConditions.visibilityOfAllElements(elements));
        for (WebElement ele : elements) {
            SeleniumUtil.scrollToWebElement(driver, ele);
            elementText.add(ele.getText().replaceAll("\\R", " ").trim());
        }
        return elementText;
    }


}