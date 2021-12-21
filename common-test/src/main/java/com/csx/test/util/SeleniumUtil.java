
package com.csx.test.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

public class SeleniumUtil {
	public static final String videoFileType = "avi";
	private static final String SPINNER_XPATH = "//app-block-ui/div/p-blockui";
	public static final String ERROR_MSG = "Some error has occurred while performing operation::{}";
	public static final String IS_ENTERED = " is entered";
	public static final String AND_PASSWORD = " and Password: ";
	public static final String USERNAME = "Username: ";
	private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumUtil.class);
	public static final int DRIVER_WAIT_TIME_IN_SECS = 30;
	private static final String DROPDOWN_ITEM_SELECTOR_IN_OVERLAY = "//ul/li[*]/span[text()='%s']";
	private static final String DROPDOWN_PARTIAL_MATCH_ITEM_SELECTOR_IN_OVERLAY = "//ul/li[*]/span[contains(text(),'%s')]";
	private static final String DROPDOWN_OVERLAY = "//ul";
	private static final String DROPDOWN_CLICKABLE_LABEL = "div/label";
	private static final String DEFAULT_RACFID = "t_client.crew.hr.admin@csx.local";
	private static final String DEFAULT_RACFID_PASSWORD = "*20q#ZUAa7qe1a9!";
	private static final String DEFAULT_SXLOGON_STAGING_URL = "https://crew-qa.go-dev.csx.com/#";
	public static final String LOGINPAGE_USERNAME_TEXTBOX_OBJECT_ID = "okta-signin-username";
	private static final String LOGINPAGE_PASSWORD_TEXTBOX_OBJECT_ID = "okta-signin-password";
	public static final String LOGINPAGE_LOGIN_BUTTON_OBJECT_ID = "okta-signin-submit";
	public static final String downloadPath = System.getProperty("user.dir");
	public static final String testUserName = "t_client.crew.hr.admin@csx.local";
	public static final String testPassWord = "*20q#ZUAa7qe1a9!";

	private SeleniumUtil() {
	}

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
	 * ---------------------------Logon to non prod------------------------------------------
	 */
	public static void logonNonProd(WebDriver driver){
		WebElement userName = driver.findElement(By.id(LOGINPAGE_USERNAME_TEXTBOX_OBJECT_ID));
		WebElement pwd = driver.findElement(By.id(LOGINPAGE_PASSWORD_TEXTBOX_OBJECT_ID));
		WebElement submit = driver.findElement(By.id(LOGINPAGE_LOGIN_BUTTON_OBJECT_ID));
		SeleniumUtil.isElementDisplayed(driver, pwd);
		SeleniumUtil.setValueToElement(driver, userName, testUserName);
		SeleniumUtil.setValueToElement(driver, pwd, testPassWord);
		SeleniumUtil.clickElementbyWebElementWithOutSendKeys(driver, submit);
	}

	/**
	 * ---------------------------Resize window for Tablet------------------------------------------
	 */
	public static void resizeWindowForTablet(final WebDriver driver) {
		driver.manage().window().setSize(new Dimension(700, 750));
	}

	/**
	 * ------------------------------- System Date in customFormat --------- Author: Perraj Kumar K (S9402)-----------------------------
	 */
	public static String todayDate(String dateTimeFormat) {
		LocalDateTime dateTime = LocalDateTime.now();
		return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format like "MM/dd/yyyy
																				// | HH:mm"
	}

	/**
	 * ------------------------------- Add days to System Date in custom Format--------------Author: Perraj Kumar K (S9402)------------------------
	 */
	public static String addDays(String dateTimeFormat, int days) {
		LocalDateTime dateTime = LocalDateTime.now().plusDays(days);
		return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format like "MM/dd/yyyy
																				// | HH:mm"
	}

	/**
	 * ------------------------------- minus days to System Date in custom Format------------------Author: Perraj Kumar K (S9402)--------------------
	 */
	public static String minusDays(String dateTimeFormat, int days) {
		LocalDateTime dateTime = LocalDateTime.now().minusDays(days);
		return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format like "MM/dd/yyyy
																				// | HH:mm"
	}

	/**
	 * ------------------------------- Add hours to System Date in custom Format-----------------Author: Perraj Kumar K (S9402)--------------
	 */
	public static String addHours(String dateTimeFormat, int hours) {
		LocalDateTime dateTime = LocalDateTime.now().plusHours(hours);
		return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format like "MM/dd/yyyy
																				// | HH:mm"
	}

	/**
	 * ------------------------------- Add minutes to System Date in custom Format-------------Author: Perraj Kumar K (S9402)-------------------------
	 */
	public static String addMinutes(String dateTimeFormat, int minutes) {
		LocalDateTime dateTime = LocalDateTime.now().plusMinutes(minutes);
		return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format like "MM/dd/yyyy
																				// | HH:mm"
	}

	/**
	 * ------------------------------- minus hours to System Date in custom Format---------------Author: Perraj Kumar K (S9402)-----------------------
	 */
	public static String minusHours(String dateTimeFormat, int hours) {
		LocalDateTime dateTime = LocalDateTime.now().minusHours(hours);
		return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format like "MM/dd/yyyy
																				// | HH:mm"
	}

	/**
	 * ------------------------------- Verification of Webtable sort ----------------------------Author: Perraj Kumar K (S9402)------------------------
	 */

	public static void sortingandCompareWebtableColumns(WebDriver driver, int colNum) {
		driver.findElement(By.xpath("//tr/th[" + colNum + "]")).click();
		List<WebElement> headerList = driver.findElements(By.xpath("//tr/td[" + colNum + "]"));
		List<String> originalList = headerList.stream()
				.map(s -> s.findElement(By.xpath("//tr/td[" + colNum + "]")).getText()).collect(Collectors.toList());
		List<String> sortedList = originalList.stream().sorted().collect(Collectors.toList());
		Assertions.assertTrue(originalList.equals(sortedList));
	}
	
	/**
	 * ------------------------------- Get the Webtable cell value -------------------------------Author: Perraj Kumar K (S9402)---------------------
	 */
	
	public static String getWebTableData(WebDriver driver, int rowNum, int colNum) {
		WebElement webElement = driver.findElement(By.xpath("//tr[" + rowNum +"]/td[" + colNum + "]"));
		return getValueByElement(driver, webElement);
	}

	/**
	 * ------------------------------- Oracle Database connection and retrieve specific column data ------------Author: Perraj Kumar K (S9402)--------------------------
	 */
	
	public static void oracleConnectionExecuteSql(String dbUrl, String username, String password,
			String sql) {
		try  {		
			Connection dbConnection = DriverManager.getConnection(dbUrl, username, password);
			dbConnection.setAutoCommit(false);
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			dbConnection.commit();
			rs.close();
			dbConnection.close();
			}		
			catch (SQLException | NullPointerException e) {
			LOGGER.error(ERROR_MSG, e);
			fail();
		}
	}

	public static List<Map<String, String>> oracleConnectionRowRetrieve(String dbUrl, String username, String password,
			String sql) {
		try (Connection dbConnection = DriverManager.getConnection(dbUrl, username, password);
				Statement stmt = dbConnection.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			
			ResultSetMetaData rsmd = rs.getMetaData();
			
			List<String> cols = new ArrayList<>();
			
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				cols.add(rsmd.getColumnName(i));
			}
			
			List<Map<String, String>> rows = new ArrayList<>();
			Map<String, String> rsData = null;
			
			while (rs.next()) {
				rsData = new HashMap<>();
				
				for (String colName : cols) {
					rsData.put(colName, rs.getString(colName));
				}
				
				rows.add(rsData);
			}
			dbConnection.close();
			return rows;

		} catch (SQLException | NullPointerException e) {
			LOGGER.error(ERROR_MSG, e);
			fail();
		}
		return Collections.emptyList();
	}
	
	public static String[][] oracleConnectionDataRetrieve(String dbUrl, String username, String password, String sql)
	 {
		
		try (Connection dbConnection = DriverManager.getConnection(dbUrl, username, password);
				Statement stmt = dbConnection.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			int rowCnt, colCnt;
			ResultSetMetaData rsmd = rs.getMetaData();
			colCnt = rsmd.getColumnCount();
			rs.last();
			rowCnt = rs.getRow();
			rs.first();
			String[][] resultData = new String[50][50];
			int i = 0;
			while (rs.next()) {
				for (int j = 0; j < colCnt; j++) {
					resultData[i][j] = rs.getString(j + 1);
				}
				i++;
			}
			return resultData;

		} catch (SQLException | NullPointerException e) {
			LOGGER.error(ERROR_MSG, e);
			fail();
		}
		return null;
	}

	/**
	 * ------------------------------- File download confirmation ------------------------------Author: Perraj Kumar K (S9402)--------
	 */
	public static boolean isFileDownloaded(String partialFileName) {
		File dir = new File(downloadPath);
		File[] files = dir.listFiles();
		boolean flag = false;
		for (int i = 1; i < files.length; i++) {
			if (files[i].getName().contains(partialFileName)) {
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * ------------------------------- Get Newest File --------------------------Author: Perraj Kumar K (S9402)------------
	 */
	
	public static String getTheNewestFile(String filePath, String ext) {
	    //File theNewestFile = null;
	    File dir = new File(filePath);
	    FileFilter fileFilter = new WildcardFileFilter("*." + ext);
	    File[] files = dir.listFiles(fileFilter);
	    String name = "";

	    if (files.length > 0) {
	        /** The newest file comes first **/
	        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
	        //theNewestFile = files[0];
	        name = files[0].getName();
	    }

	    return name;
	}

	/**
	 * -------------------------------Delete file -------------------------Author: Perraj Kumar K (S9402)-------------
	 */

	public static void deleteExistingFile(String partialFileName) {
		File dir = new File(downloadPath);
		File[] files = dir.listFiles();
		for (int i = 1; i < files.length; i++) {
			if (files[i].getName().contains(partialFileName)) {
				files[i].delete();
			}
		}
	}

	/**
	 * -------------------------------Get file name -------------------------Author: Perraj Kumar K (S9402)-------------
	 */
	public static String getFilename(String partialFileName) {
		File dir = new File(downloadPath);
		File[] files = dir.listFiles();
		String reportFileName = "";
		for (int i = 1; i < files.length; i++) {
			if (files[i].getName().contains(partialFileName)) {
				reportFileName = files[i].getName();
			}
		}
		return reportFileName;
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
	 * -------------------------------Get PDF Content -------------------------Author: Perraj Kumar K (S9402)-------------
	 */
	public static String getPDFContent(WebDriver driver, String url) throws IOException {
		driver.get(url);
		URL pdfUrl = new URL(driver.getCurrentUrl());
		InputStream inputStream = pdfUrl.openStream();
		BufferedInputStream bufferedIPS = new BufferedInputStream(inputStream);
		PDDocument doc = PDDocument.load(bufferedIPS);
		String content = new PDFTextStripper().getText(doc);
		String pdfContent = content.toLowerCase();
		doc.close();
		return pdfContent;
	}

	/**
	 * -------------------------------Excel Reader-----------------------Author: Perraj Kumar K (S9402)---------------
	 * 
	 * @throws IOException
	 */
	public static XSSFSheet excelRead(String fileName, int sheetNum) throws IOException {
		FileInputStream fis = new FileInputStream(fileName);
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet = workbook.getSheetAt(sheetNum);
		fis.close();
		return sheet;
	}

	/**
	 * -------------------------------Excel Row Count-------------------Author: Perraj Kumar K (S9402)-------------------
	 */
	public static int getRowCnt(XSSFWorkbook workbook, String sheetName) {
		int index = workbook.getSheetIndex(sheetName);
		if (index == -1)
			return 0;
		else {
			XSSFSheet sheet = workbook.getSheetAt(index);
			return sheet.getLastRowNum() + 1;
		}
	}

	/**
	 * -------------------------------Excel Getting one cell data --------------Author: Perraj Kumar K (S9402)------------------------
	 */
	public static XSSFCell getCellData(XSSFSheet sheet, int colNum, int rowNum) {
		XSSFRow row = sheet.getRow(rowNum - 1);
		XSSFCell cell = row.getCell(colNum);
		return cell;
	}

	/*
	 * -------------------------------Excel formatting cell data based on value --------------Author: Perraj Kumar K (S9402)------------------------
	 */
	public static Object getCellValue(Cell cell) {
		if (cell.getCellType() == CellType.STRING)
			return cell.getStringCellValue();
		else if (cell.getCellType() == CellType.BOOLEAN)
			return cell.getBooleanCellValue();
		else if (cell.getCellType() == CellType.NUMERIC)
			return cell.getNumericCellValue();
		else if (cell.getCellType() == CellType.FORMULA)
			return String.valueOf(cell.getNumericCellValue());
		else
			return null;

	}

	/**
	 * -------------------------------Excel entire column data of one row ---------------Author: Perraj Kumar K (S9402)-----------------------
	 */
	public static List getColumnData(XSSFSheet sheet, int row) {
		List colDataList = new ArrayList<>();
		for (int i = 1; i <= sheet.getLastRowNum() + 1; i++) {
			XSSFCell cellVal = SeleniumUtil.getCellData(sheet, row, i);
			colDataList.add(SeleniumUtil.getCellValue(cellVal));
		}
		return colDataList;
	}
	/**
	-------------------------Author: Perraj Kumar K (S9402)----------------------------------------
	 */
	public static List<String> getCSVColumnData(String filePath, int colNum) {		
		try {
			Reader reader;
			reader = Files.newBufferedReader(Paths.get(filePath));
			CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
			List<String> colValList = new ArrayList<String>();

			for (CSVRecord csvr : csvParser) {
				colValList.add(csvr.get(colNum));
			}
			csvParser.close();
			return colValList;
		} catch (IOException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}		

		return null;
	}

	/**
	 * -------------------------------Simple Date Format--------------------------------------
	 */
	public static String todayDate() {
		return new SimpleDateFormat("MM/dd/yyyy HH:mm").format(new Date());
	}

	/**
	 * ------------------------------------------------------------------------------------------------------------------
	 * Click on the WebElement With ID and handling WebDriverWait to handle
	 * NoSuchElementException Passing Element as String
	 */
	public static void clickElementByID(final WebDriver driver, final String elementID) {
		final WebDriverWait wait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(elementID)));
		wait.until(ExpectedConditions.elementToBeClickable(By.id(elementID)));
		driver.findElement(By.id(elementID)).click();
	}

	/**
	 * ------------------------------------------------------------------------------------------------------------------
	 * Click on the WebElement With XPATH and handling WebDriverWait to handle
	 * NoSuchElementException Passing Element as String
	 */
	public static void clickElementbyXPath(final WebDriver driver, final String elementID) {
		try {
			final WebDriverWait wait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(elementID)));
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(elementID)));
			driver.findElement(By.xpath(elementID)).click();
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
	}

	/**
	 * ------------------------------------------------------------------------------------------------------------------
	 * Click on the WebElement With XPATH and handling WebDriverWait to handle
	 * NoSuchElementException Passing Element as WebElement
	 */
	public static void clickElementbyWebElement(final WebDriver driver, final WebElement elementID) {
		try {
			final WebDriverWait wait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
			wait.until(ExpectedConditions.visibilityOf(elementID));
			wait.until(ExpectedConditions.elementToBeClickable(elementID));
			elementID.sendKeys("");
			elementID.click();
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		} catch (final Exception e) {
			// If clickElementbyWebElement method failed to click on the element with
			// sendKyes
			// then this catch block will be executed with calling
			// clickElementbyWebElementWithOutSendKeys method
			clickElementbyWebElementWithOutSendKeys(driver, elementID);
		}
	}

	/**
	 * ------------------------------------------------------------------------------------------------------------------
	 * Click on the WebElement With XPATH and handling WebDriverWait to handle
	 * NoSuchElementException Passing Element as WebElement This method doesn't use
	 * sendKeys
	 */
	public static void clickElementbyWebElementWithOutSendKeys(final WebDriver driver, final WebElement elementID) {
		try {
			final WebDriverWait wait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
			wait.until(ExpectedConditions.visibilityOf(elementID));
			wait.until(ExpectedConditions.elementToBeClickable(elementID));
			elementID.click();
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
	}

	/**
	 * ---------------------------------------------------------------------------------------------------
	 * Submit Element by XPATH
	 */
	public static void submitElementbyXPath(final WebDriver driver, final String elementID) {
		final JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", driver.findElement(By.xpath(elementID)));
	}

	/**
	 * ----------------------------------------------------------------------------------------------------
	 * Scroll till Web Element
	 */
	public static void scrollToWebElement(final WebDriver driver, final WebElement webelement) {
		final JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].scrollIntoView(true);", webelement);
	}

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * Validate Element
	 */
	public static void validateElement(final WebDriver driver, final String xpath, final String testCaseName,
			final String expectedResult) {
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			final WebElement element = driver.findElement(By.xpath(xpath));
			Assertions.assertEquals(expectedResult.trim(), element.getText().trim());
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
	}

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * Set value to Element
	 */
	public static void setValueToElementByXpath(final WebDriver driver, final String xpath, final String inputValue) {
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			final WebElement element = driver.findElement(By.xpath(xpath));
			element.clear();
			element.sendKeys(inputValue);
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
	}

	public static void setValueToElementByXpath(final WebDriver driver, final WebElement element,
			final String inputValue) {
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			driverWait.until(ExpectedConditions.visibilityOf(element));
			element.clear();
			element.sendKeys(inputValue);
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
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
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			driverWait.until(ExpectedConditions.visibilityOf(webElement));
			webElement.clear();
			webElement.sendKeys(inputValue);
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
	}

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * get value by element
	 */
	public static String getValueByElement(final WebDriver driver, final WebElement webElement) {
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.visibilityOf(webElement)).isDisplayed()) {
				return webElement.getText().trim();
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
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
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.visibilityOf(webElement)).isDisplayed()) {
				return webElement.getAttribute("value").trim();
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
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
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.visibilityOf(webElement)) != null) {
				return webElement.isEnabled();
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
		return false;
	}
	
	public static Boolean getEnableDisableByElement(final WebDriver driver, String xPath) {
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		WebElement webElement = driver.findElement(By.xpath(xPath));
		try {
			if (driverWait.until(ExpectedConditions.visibilityOf(webElement)) != null) {
				return webElement.isEnabled();
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
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
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.visibilityOf(webElement)) != null) {
				final String attribute = webElement.getAttribute("disabled");
				return attribute != null && attribute.equalsIgnoreCase("true");
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
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
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		Boolean isEnabled = null;
		try {
			isEnabled = driverWait.until(driver1 -> {
				final Boolean isElementEnabled = webElement.isEnabled();
				return testIsEnabled ? isElementEnabled : !isElementEnabled;
			});
		} catch (final org.openqa.selenium.TimeoutException ele) {
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
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.visibilityOf(webElement)) != null) {
				final String webElementCSS = getElementsCSS(driver, webElement, "webelemnt CSS");
				if (webElementCSS != null && !webElementCSS.isEmpty()) {
					return webElementCSS.contains("loadingSpinner");
				} else {
					return false;
				}
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
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
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.visibilityOf(webElement)) != null) {
				final String webElementCSS = getElementsCSS(driver, webElement, "webelemnt CSS");
				if (webElementCSS != null && !webElementCSS.isEmpty()) {
					return webElementCSS.contains("ui-state-disabled");
				} else {
					return false;
				}
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
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
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			driverWait.until(ExpectedConditions.visibilityOf(webElement));
			final Actions actObj = new Actions(driver);
			actObj.moveToElement(webElement).build().perform();
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
	}

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * Check & Uncheck box
	 */
	public static void clickCheckedUnCheckedElement(final WebDriver driver, final WebElement elementID) {
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			driverWait.until(ExpectedConditions.visibilityOf(elementID));
			driverWait.until(ExpectedConditions.elementToBeClickable(elementID));
			final boolean chk = elementID.isEnabled();
			if (chk == true) {
				elementID.click();
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
	}

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * Mouse over element select
	 */
	public static void mouseOverElementSelect(final WebDriver driver, final WebElement webElement) {
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			driverWait.until(ExpectedConditions.visibilityOf(webElement)).isSelected();
			final Actions actObj = new Actions(driver);
			actObj.moveToElement(webElement).build().perform();
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
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
			return driver.findElements(By.xpath(xPath)).size() > 0;
		} catch (final org.openqa.selenium.NoSuchElementException ele) {
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
	public static String getCssClassesByElement(final WebDriver driver, final WebElement webElement,
			final String elementName) {
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.visibilityOf(webElement)).isDisplayed()) {
				return webElement.getAttribute("class").trim();
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
		return null;
	}

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * Get CSS value by element
	 */
	public static String getCSSValueByElement(final WebDriver driver, final WebElement webElement,
			final String cssAttributeName, final String elementName) {
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.visibilityOf(webElement)).isDisplayed()) {
				return webElement.getCssValue(cssAttributeName);
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
		return null;
	}

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * get data table size by xpath
	 */
	public static int getDataTableSizeByXpath(final WebDriver driver, final String tableXPath,
			final String testCaseName) {
		final List<WebElement> rows = driver.findElements(By.xpath(tableXPath + "/tbody/tr"));
		return rows.size();
	}

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * get mobile view card size by xpath
	 */
	public static int getMobileViewCardsSizeByXpath(final WebDriver driver, final String cardXPath,
			final String testCaseName) {
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
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.visibilityOf(webElement)).isDisplayed()) {
				return webElement.isDisplayed();	
			}
			
		} catch (final NoSuchElementException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
			return false;
		}
		return true;
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
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.visibilityOf(webElement)).isDisplayed()) {
				return webElement.getAttribute("class");
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
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
		return driver.findElement(By.xpath(xpath));
	}

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * Xpath eists
	 */
	public static boolean xPathExists(final WebDriver driver, final String xpath) {
		final WebDriverWait driverWait = new WebDriverWait(driver, 0);
		try {
			driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			return true;
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
			return false;
		}
	}

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * get value by element no log
	 */
	public static String getValueByElementNoLog(final WebDriver driver, final WebElement webElement,
			final String elementName) {
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.visibilityOf(webElement)).isDisplayed()) {
				return webElement.getText().trim();
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
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
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.visibilityOf(webElement)) != null) {
				isVisible = true;
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
		return isVisible;
	}

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * is element enabled
	 */
	public static Boolean isElementEnabled(final WebDriver driver, final WebElement webElement,
			final String elementName) {
		Boolean isEnabled = false;
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.visibilityOf(webElement)) != null) {
				isEnabled = webElement.isEnabled();
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
		return isEnabled;
	}

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * is element not present
	 */
	public static Boolean isElementNotPresent(final WebDriver driver, final String xPath) {
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			return driverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xPath)));
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
	public static Boolean isElementNotPresentWithWait(final WebDriver driver, final String xPath,
			final int waitTimeinSec) {
		final WebDriverWait driverWait = new WebDriverWait(driver, waitTimeinSec);
		try {
			return driverWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xPath)));
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
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.visibilityOf(webElement)).isDisplayed()) {
				return webElement.isEnabled();
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
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
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xPath))).isDisplayed()) {
				final WebElement webElement = driver.findElement(By.xpath(xPath));
				return webElement.getText().trim();
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
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
	public static boolean checkValueInList(final WebDriver driver, final List<WebElement> webElement,
			final String expectedValueInList, final String elementName) {
		final Boolean isValueExist = false;
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.visibilityOfAllElements(webElement)) != null) {
				final List<WebElement> elements = webElement;
				for (int i = 0; i < elements.size(); i++) {
					final String listValue = elements.get(i).getText().trim();
					if (listValue.contains(expectedValueInList)) {
						return true;
					}
				}
				return isValueExist;
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
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
		final WebDriverWait driverWait = new WebDriverWait(driver, driverWaitTimeInSecs);
		// get window handlers as list
		final List<String> browserTabs = new ArrayList<>(driver.getWindowHandles());
		// Ship to new tab and check if correct page opened
		String shipmentInstructionsTitle = null;
		driver.switchTo().window(browserTabs.get(2));
		if (driverWait.until(ExpectedConditions.titleContains("Main Page"))) {
			shipmentInstructionsTitle = driver.getTitle();
		}
		driver.switchTo().window(browserTabs.get(0));
		return shipmentInstructionsTitle;
	}

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * is text there
	 */
	public static String isTextThere(final WebDriver driver, final WebElement element, final Boolean testIsEnabled,
			final String elementName, final String attributeName) {
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
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
		} catch (org.openqa.selenium.TimeoutException | InterruptedException ele) {
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
			final JavascriptExecutor js = (JavascriptExecutor) driver;
			return (String) js.executeScript("return document.getElementById(" + "'" + elementId + "'" + ").value");
		} catch (final org.openqa.selenium.TimeoutException ele) {
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
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		driverWait.until(ExpectedConditions.visibilityOf(dropDownElement));
		driverWait.until(ExpectedConditions
				.elementToBeClickable(dropDownElement.findElement(By.xpath(DROPDOWN_CLICKABLE_LABEL))));
		clickElementbyWebElement(driver, dropDownElement.findElement(By.xpath(DROPDOWN_CLICKABLE_LABEL)));
		try {
			Thread.sleep(300);
		} catch (final InterruptedException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
		driverWait.until(ExpectedConditions.visibilityOf(dropDownElement.findElement(By.xpath(DROPDOWN_OVERLAY))));
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
		final WebDriverWait driverWait = new WebDriverWait(driver, driverWaitTimeInSecs);
		driverWait.until(ExpectedConditions.visibilityOf(dropDownElement));
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
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		driverWait.until(ExpectedConditions.visibilityOf(checkBox));
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
		final WebDriverWait wait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[contains(text(),' Equipment ')]")));
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
	public static Boolean isSpinnerLoaded(final WebDriver driver, final WebElement webElement,
			final String elementName) {
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			if (driverWait.until(ExpectedConditions.invisibilityOf(webElement)) != null) {
				final String webElementCSS = getElementsCSS(driver, webElement, "webelemnt CSS");
				if (webElementCSS != null && !webElementCSS.isEmpty()) {
					return webElementCSS.contains("loadingSpinner");
				} else {
					return false;
				}
			}
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
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
		final Wait<WebDriver> wait = new FluentWait<>(driver).pollingEvery(Duration.ofMillis(200))
				.withTimeout(Duration.ofSeconds(DRIVER_WAIT_TIME_IN_SECS)).ignoring(NoSuchElementException.class);
		// making sure that spinner is present
		wait.until(d -> ExpectedConditions.visibilityOf(d.findElement(By.xpath(SPINNER_XPATH))));

		// we have to wait until spinner goes away
		final Wait<WebDriver> wait1 = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		wait1.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath(SPINNER_XPATH))));
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
		} catch (final org.openqa.selenium.TimeoutException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
		return null;
	}

	public static void sxoLogon(final WebDriver driver, final String url, final String racfId, final String pwd) {
		navigateToURL(driver, url);
		LOGGER.info("Launched sxo url: " + url);
		driver.findElement(By.id(LOGINPAGE_USERNAME_TEXTBOX_OBJECT_ID)).sendKeys(racfId);
		driver.findElement(By.id(LOGINPAGE_PASSWORD_TEXTBOX_OBJECT_ID)).sendKeys(pwd);
		LOGGER.info(USERNAME + racfId + AND_PASSWORD + pwd + IS_ENTERED);
		driver.findElement(By.id(LOGINPAGE_LOGIN_BUTTON_OBJECT_ID)).click();
		LOGGER.info("Login button is clicked");
		try {
			Thread.sleep(5000);
		} catch (final InterruptedException e) {
			LOGGER.info("Logging Failed");
		}
	}

	/**
	 * -------------------------------------------------------------------------------------------------------
	 * SXO LOGON Method with Default Staging URL and Credentials
	 */
	public static void sxoLogon(final WebDriver driver) {
		navigateToURL(driver, DEFAULT_SXLOGON_STAGING_URL);
		LOGGER.info("Launched sxo url: " + DEFAULT_SXLOGON_STAGING_URL);
		driver.findElement(By.id(LOGINPAGE_USERNAME_TEXTBOX_OBJECT_ID)).sendKeys(DEFAULT_RACFID);
		driver.findElement(By.id(LOGINPAGE_PASSWORD_TEXTBOX_OBJECT_ID)).sendKeys(DEFAULT_RACFID_PASSWORD);
		LOGGER.info(USERNAME + DEFAULT_RACFID + AND_PASSWORD + DEFAULT_RACFID_PASSWORD + IS_ENTERED);
		driver.findElement(By.id(LOGINPAGE_LOGIN_BUTTON_OBJECT_ID)).click();
		LOGGER.info("Login button is clicked");
		try {
			Thread.sleep(5000);
		} catch (final InterruptedException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
	}

	/**
	 * ------------------------------------------------------------------------------------------------------
	 * 
	 * @param driver
	 * @param url    SXO LOGON Method with URL Passing. This method used default
	 *               Racf ID and Pwd.
	 */
	public static void sxoLogon(final WebDriver driver, final String url) {
		navigateToURL(driver, url);
		LOGGER.info("Launched sxo url:{} ", url);
//		SeleniumUtil.setValueToElement(driver, LOGINPAGE_USERNAME_TEXTBOX_OBJECT_ID, DEFAULT_RACFID);
		driver.findElement(By.id(LOGINPAGE_USERNAME_TEXTBOX_OBJECT_ID)).sendKeys(DEFAULT_RACFID);
		driver.findElement(By.id(LOGINPAGE_PASSWORD_TEXTBOX_OBJECT_ID)).sendKeys(DEFAULT_RACFID_PASSWORD);
		LOGGER.info(USERNAME + DEFAULT_RACFID + AND_PASSWORD + DEFAULT_RACFID_PASSWORD + IS_ENTERED);
		driver.findElement(By.id(LOGINPAGE_LOGIN_BUTTON_OBJECT_ID)).click();
		LOGGER.info("Login button is clicked");
		try {
			Thread.sleep(5000);
		} catch (final InterruptedException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
	}

	/**
	 * ----This Method is for Sorting of Ascending&Descending Order---------------
	 */

	public static void verifyAscendingAndDescending(final WebDriver driver, final String XPATH,
			final String elementName) {
		try {
			final JavascriptExecutor js = (JavascriptExecutor) driver;
			final List<WebElement> AllAscendingAndDescending = driver.findElements(By.xpath(XPATH));
			for (final WebElement AscAndDsc : AllAscendingAndDescending) {
				final String value = AscAndDsc.getText();
				final String script = "return arguments[0].innerHTML";
				LOGGER.info(elementName + ":" + value);
			}
		} catch (final org.openqa.selenium.TimeoutException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
	}

	public static void clearTextField(final WebDriver driver, final WebElement webElement, final String inputValue) {
		final WebDriverWait driverWait = new WebDriverWait(driver, DRIVER_WAIT_TIME_IN_SECS);
		try {
			driverWait.until(ExpectedConditions.visibilityOf(webElement));
			webElement.sendKeys(Keys.CONTROL, Keys.chord("a"));
			webElement.sendKeys(Keys.BACK_SPACE);
		} catch (org.openqa.selenium.TimeoutException | NoSuchElementException ele) {
			LOGGER.error(ERROR_MSG, ele);
			fail();
		}
	}

	/**
	 * -------------------------------Add days to current date--------------------------------------
	 */
	public static Date addDays(final int daysCount) {
		Date date = new Date();
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, daysCount);
		date = cal.getTime();
		return date;
	}
}
