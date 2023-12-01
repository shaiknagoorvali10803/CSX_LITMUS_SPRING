package com.csx.test.util;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class WebTableUtil {
// Sort webtable and verify sort is correct or not

	public static void sortingandCompareWebtableColumns(WebDriver driver, int colNum) {
		driver.findElement(By.xpath("//tr/th[" + colNum + "]")).click();
		List<WebElement> headerList = driver.findElements(By.xpath("//tr/td[" + colNum + "]"));
		List<String> originalList = headerList.stream()
				.map(s -> s.findElement(By.xpath("//tr/td[" + colNum + "]")).getText()).collect(Collectors.toList());
		List<String> sortedList = originalList.stream().sorted().collect(Collectors.toList());
		Assertions.assertTrue(originalList.equals(sortedList));
	}
	
//Get web table data using row number and column number	
	public static String getWebTableData(WebDriver driver, int rowNum, int colNum) {
		WebElement webElement = driver.findElement(By.xpath("//tr[" + rowNum +"]/td[" + colNum + "]"));
		return SeleniumUtil.getValueByElement(driver, webElement);
	}

}
