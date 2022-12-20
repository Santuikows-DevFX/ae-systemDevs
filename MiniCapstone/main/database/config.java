package main.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class config {

    private Connection conn;
    public Connection getConnection() { 

        try {
            
            String driver = "com.mysql.cj.jdbc.Driver";
            String dbURL = "jdbc:mysql://127.0.0.1:3306/ae-db";
            String user = "aedevs";
            String password = "rm)6A/7[PByk*yOd";

            Class.forName(driver);
            conn = DriverManager.getConnection(dbURL, user, password);
            // System.out.println("CONNECTED");

            return conn;

        } catch (Exception e) {
        }

        return null;
    }
    
}
