package com.csx.test.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ------------------------------- Author: Perraj Kumar K (S9402)-------------------------
 */

public class DateTimeUtil {

	//------------------------------- System Date in customFormat --------- 
	public static String todayDate(String dateTimeFormat) {
		LocalDateTime dateTime = LocalDateTime.now();
		return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format like "MM/dd/yyyy
																				// | HH:mm"
	}

	 //------------------------------- Add days to System Date in custom Format
	public static String addDays(String dateTimeFormat, int days) {
		LocalDateTime dateTime = LocalDateTime.now().plusDays(days);
		return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format like "MM/dd/yyyy
																				// | HH:mm"
	}

	 // ------------------------------- minus days to System Date in custom Format
	 
	public static String minusDays(String dateTimeFormat, int days) {
		LocalDateTime dateTime = LocalDateTime.now().minusDays(days);
		return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format like "MM/dd/yyyy
																				// | HH:mm"
	}

	
	//------------------------------ Add hours to System Date in custom Format-----------------
	public static String addHours(String dateTimeFormat, int hours) {
		LocalDateTime dateTime = LocalDateTime.now().plusHours(hours);
		return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format like "MM/dd/yyyy
																				// | HH:mm"
	}

	 //------------------------------- Add minutes to System Date in custom Format
	public static String addMinutes(String dateTimeFormat, int minutes) {
		LocalDateTime dateTime = LocalDateTime.now().plusMinutes(minutes);
		return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format like "MM/dd/yyyy
																				// | HH:mm"
	}


	//------------------------------- minus hours to System Date in custom Format
	public static String minusHours(String dateTimeFormat, int hours) {
		LocalDateTime dateTime = LocalDateTime.now().minusHours(hours);
		return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat)); // give any custom format like "MM/dd/yyyy
																				// | HH:mm"
	}
}
