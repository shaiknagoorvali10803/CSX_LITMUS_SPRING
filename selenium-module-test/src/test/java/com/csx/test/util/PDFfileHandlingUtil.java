package com.csx.test.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.WebDriver;

import java.io.*;
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

	/**
	 * -------------------------Author: Shaik.Nagoorvali (Z3594)----------------------------------------
	 */

	public static String getPDFData(String filepath) {
		PDDocument document = null;
		PDFTextStripper pdfStripper = null;
		String pdfText = null;
		try {
			File file = new File(filepath);
			FileInputStream fis = new FileInputStream(file);
			document = PDDocument.load(fis);
			pdfStripper = new PDFTextStripper();
			pdfText = pdfStripper.getText(document);
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pdfText;
	}

	/**
	 * -------------------------Author: Shaik.Nagoorvali (Z3594)----------------------------------------
	 */

	public static String getPDFData(String filepath, int pageNo) {
		PDDocument document = null;
		PDFTextStripper pdfStripper = null;
		String pdfText = null;
		try {
			File file = new File(filepath);
			document = PDDocument.load(file);
			pdfStripper = new PDFTextStripper();
			pdfStripper.setStartPage(pageNo);
			pdfStripper.setEndPage(pageNo);
			pdfText = pdfStripper.getText(document);
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pdfText;
	}

	/**
	 * -------------------------Author: Shaik.Nagoorvali (Z3594)----------------------------------------
	 */

	public static String getPDFData(String filepath, int startPage, int endPage) {
		PDDocument document = null;
		PDFTextStripper pdfStripper = null;
		String pdfText = null;
		try {
			File file = new File(filepath);
			document = PDDocument.load(file);
			pdfStripper = new PDFTextStripper();
			pdfStripper.setStartPage(startPage);
			pdfStripper.setEndPage(endPage);
			pdfText = pdfStripper.getText(document);
			document.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pdfText;
	}
}
