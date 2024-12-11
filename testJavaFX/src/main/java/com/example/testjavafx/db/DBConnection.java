package com.example.testjavafx.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    static String user = "postgres";
    static String password = "root";
    static  String url = "jdbc:postgresql://localhost:5432/test_javaFX";
    static String driver = "org.postgresql.Driver";
    public static Connection getCon(){
        Connection con = null;
        try {
            Class.forName(driver);
            try{
                con = DriverManager.getConnection(url,user,password);
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
        return con;
    }
}
