package com.csx.test.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

/**
-------------------------Author: Perraj Kumar K (S9402)----------------------------------------
 */
public class CSVUtil {
	public static final String ERROR_MSG = "Some error has occurred while performing operation::{}";
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVUtil.class);
	
//Get the data from CSV file as string list
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

}
