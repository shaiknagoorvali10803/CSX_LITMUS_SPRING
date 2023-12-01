package com.csx.test.util;


import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class XLUtility_xlsx {
    public static FileInputStream fi;
    public static FileOutputStream fo;
    public static XSSFWorkbook workbook;
    public static XSSFSheet sheet;
    public static XSSFRow row;
    public static XSSFCell cell;
    public static CellStyle style;
    private static Map<String, Integer> columns = new HashMap<>();

    public XLUtility_xlsx() {
    }

    public static int getRowCount(String filePath,String sheetName) throws IOException {
        fi = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);
        int rowcount = sheet.getLastRowNum();
        workbook.close();
        fi.close();
        return rowcount;
    }

    public static int getCellCount(String filePath,String sheetName, int rownum) throws IOException {
        fi = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rownum);
        int cellcount = row.getLastCellNum();
        workbook.close();
        fi.close();
        return cellcount;
    }

    public static Map<String, Integer> getColumns(String ExcelPath, String SheetName) throws IOException {
        File f = new File(ExcelPath);

        if (!f.exists()) {
            f.createNewFile();
            System.out.println("File doesn't exist, so created!");
        }

        fi = new FileInputStream(ExcelPath);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(SheetName);
        if (sheet == null) {
            sheet = workbook.createSheet(SheetName);
        }

        //adding all the column header names to the map 'columns'
        sheet.getRow(0).forEach(cell -> {
            columns.put(cell.getStringCellValue(), cell.getColumnIndex());
        });
        return columns;
    }

    public static String getCellData(String filePath,String sheetName, int rownum, int colnum) throws IOException {
        String data = null;
        fi = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rownum);
        try {
            cell = row.getCell(colnum);
        } catch (Exception e) {
            data = "";
        }
        DataFormatter formatter = new DataFormatter();
        try {
            if (data != "") {
                data = formatter.formatCellValue(cell); //Returns the formatted value of a cell as a String regardless of the cell type.
            }
        } catch (Exception e) {
            data = "";
        }
        workbook.close();
        fi.close();
        return data;
    }

    public static void setCellData(String filePath,String sheetName, int rownum, int colnum, String data) throws IOException {
        File xlfile = new File(filePath);
        if (!xlfile.exists())    // If file not exists then create new file
        {
            workbook = new XSSFWorkbook();
            fo = new FileOutputStream(filePath);
            workbook.write(fo);
        }

        fi = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fi);

        if (workbook.getSheetIndex(sheetName) == -1) // If sheet not exists then create new Sheet
            workbook.createSheet(sheetName);

        sheet = workbook.getSheet(sheetName);

        if (sheet.getRow(rownum) == null)   // If row not exists then create new Row
            sheet.createRow(rownum);
        row = sheet.getRow(rownum);

        cell = row.createCell(colnum);
        cell.setCellValue(data);
        fo = new FileOutputStream(filePath);
        workbook.write(fo);
        workbook.close();
        fi.close();
        fo.close();
    }


    public static void fillGreenColor(String filePath,String sheetName, int rownum, int colnum) throws IOException {
        fi = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);

        row = sheet.getRow(rownum);
        cell = row.getCell(colnum);

        style = workbook.createCellStyle();

        style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        cell.setCellStyle(style);
        workbook.write(fo);
        workbook.close();
        fi.close();
        fo.close();
    }


    public static void fillRedColor(String filePath,String sheetName, int rownum, int colnum) throws IOException {
        fi = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fi);
        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rownum);
        cell = row.getCell(colnum);

        style = workbook.createCellStyle();

        style.setFillForegroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        cell.setCellStyle(style);
        workbook.write(fo);
        workbook.close();
        fi.close();
        fo.close();
    }

    public static List<String> getExcelOperationMerged(String filePath,String sheetName) throws IOException {
        fi = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fi);
        int index = workbook.getSheetIndex(sheetName);
        sheet = workbook.getSheetAt(index);
        Iterator iterator = sheet.iterator();
        List<String> excelValuesList = new ArrayList<>();
        while (iterator.hasNext()) {
            XSSFRow row = (XSSFRow) iterator.next();
            Iterator cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                XSSFCell cell = (XSSFCell) cellIterator.next();
                switch (cell.getCellType()) {
                    case STRING:
                        System.out.print("  " + cell.getStringCellValue());
                        excelValuesList.add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        System.out.print("  " + cell.getNumericCellValue());
                        excelValuesList.add(String.valueOf(cell.getNumericCellValue()));
                        break;
                    case BOOLEAN:
                        System.out.print("  " + cell.getBooleanCellValue());
                        excelValuesList.add(String.valueOf(cell.getBooleanCellValue()));
                        break;

                }
            }
            System.out.println();
        }
        return excelValuesList;
    }


}
