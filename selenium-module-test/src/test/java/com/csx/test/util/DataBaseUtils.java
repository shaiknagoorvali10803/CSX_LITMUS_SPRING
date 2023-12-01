package com.csx.test.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.fail;

public class DataBaseUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseUtils.class);
    public static final String ERROR_MSG = "Some error has occurred while performing operation::{}";

    /**
     * ------------------------------- Oracle Database connection and retrieve specific column data
     * --------------------------------------
     */

    public static void oracleConnectionExecuteSql(String dbUrl, String username, String password, String sql) {
        try {
            Connection dbConnection = DriverManager.getConnection(dbUrl, username, password);
            Statement stmt = dbConnection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            dbConnection.close();
        } catch (NullPointerException | SQLException e) {
            LOGGER.error(ERROR_MSG, e);
            fail();
        }
    }

    public static List<Map<String, String>> oracleConnectionRowRetrieve(String dbUrl, String username, String password, String sql) {
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

    public static String[][] oracleConnectionDataRetrieve(String dbUrl, String username, String password, String sql) {
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
}
