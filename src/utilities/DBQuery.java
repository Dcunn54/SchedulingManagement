/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Handles functionality for adding, updating, and deleting information to/from 
 * the database
 * @author Daniel
 */
public class DBQuery {
    
    private static Statement statement;
    
    public static void setStatement(Connection conn) throws SQLException{
        statement =  conn.createStatement();
    }
    
    public static Statement getStatement(){
        return statement;
    }
    
    /**
     * Handles database queries (does not delete or add to/from database)
     * @param query
     * @return
     * @throws SQLException 
     */
    public static ResultSet Query(String query) throws SQLException{
        Connection conn = DBConnection.getConnection();
        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();
        return statement.executeQuery(query);
    }
    
    /**
     * Handles database queries (does delete and/or add to/from the database)
     * @param update
     * @return
     * @throws SQLException 
     */
    public static int Update(String update) throws SQLException{
        Connection conn = DBConnection.getConnection();
        DBQuery.setStatement(conn);
        Statement statement = DBQuery.getStatement();
        return statement.executeUpdate(update);
    } 
    
}
