package com.csx.test.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.WebDriver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * ------------------------------Author: Perraj Kumar K (S9402)-------------
 */

public class PDFfileHandlingUtil {
//Get PDF content in string format
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
}
