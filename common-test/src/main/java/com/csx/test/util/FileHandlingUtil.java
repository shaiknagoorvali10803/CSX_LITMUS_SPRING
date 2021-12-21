package com.csx.test.util;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * --------------------------------Author: Perraj Kumar K (S9402)---------------------
 */

public class FileHandlingUtil {

	public static final String downloadPath = System.getProperty("user.dir");

	//File download confirmation
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
	
	//Get Newest File
	public static String getTheNewestFile(String filePath, String ext) {
	    File dir = new File(filePath);
	    FileFilter fileFilter = new WildcardFileFilter("*." + ext);
	    File[] files = dir.listFiles(fileFilter);
	    String name = "";

	    if (files.length > 0) {
	        /** The newest file comes first **/
	        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
	        name = files[0].getName();
	    }

	    return name;
	}

//Delete file with some partial filename match

	public static void deleteExistingFile(String partialFileName) {
		File dir = new File(downloadPath);
		File[] files = dir.listFiles();
		for (int i = 1; i < files.length; i++) {
			if (files[i].getName().contains(partialFileName)) {
				files[i].delete();
			}
		}
	}

// Get actual filename with partial file name match
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

//Create new tab and switch to new tab
	public static void switchNewTab(WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("window.open()");
		List<String> tabs = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
	}


}
