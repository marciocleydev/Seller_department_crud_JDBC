package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {

    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null) {
            try {
                Properties pop = loadProperties();
                String url = pop.getProperty("dburl");
                conn = DriverManager.getConnection(url, pop);
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
        return conn;
    }

    //loadProperties
    public static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties pop = new Properties();
            pop.load(fs);
            return pop;
        } catch (IOException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static void closeResource(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                throw new DbException(e.getMessage());
            }
        }
    }
}
