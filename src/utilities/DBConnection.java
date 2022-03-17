/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handles functionality for connecting to the database
 * @author Daniel
 */
public class DBConnection {
    
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//wgudb.ucertify.com:3306/";
    private static final String dbName = "WJ06PbX";
    
    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName;
    
    private static final String MYSQLJBCDriver = "com.mysql.jdbc.Driver";
    
    private static final String username = "U06PbX";
    private static Connection conn = null;
    
    /**
     * Opens connection to the database
     * @return 
     */
    public static Connection startConnection(){
       try {
           Class.forName(MYSQLJBCDriver);
           conn = DriverManager.getConnection(jdbcURL, username, Password.getPassword());
           
           System.out.println("Connection Successful");
       } catch (SQLException e){
           e.printStackTrace();
       } catch (ClassNotFoundException e){
           e.printStackTrace();
       }
       
       return conn;
    }
    
    public static Connection getConnection(){
        return conn;
    }
    
    /**
     * Closes connection to the database
     */
    public static void closeConnection(){
        try{
            conn.close();
            System.out.println("Connection Closed");
        } catch (Exception e){
            // do nothing
        }
    }
    
}
