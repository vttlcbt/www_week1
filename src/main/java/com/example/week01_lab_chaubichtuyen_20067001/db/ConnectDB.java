package com.example.week01_lab_chaubichtuyen_20067001.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static ConnectDB instance;
    private Connection connection;

    public ConnectDB() throws ClassNotFoundException, SQLException {
        try{
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
       try{
           String url = "jdbc:mariadb://localhost:3306/Lab01?createDatabaseIfNotExist=true";
           connection = DriverManager.getConnection(url, "root", "root");
       }catch (Exception e){
           System.out.println("Error: " + e.getMessage());
       }
    }

    public Connection getConnection() {
        return connection;
    }

    public static ConnectDB getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null)
            instance = new ConnectDB();
        return instance;
    }
}