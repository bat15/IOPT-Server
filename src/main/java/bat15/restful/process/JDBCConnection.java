/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.restful.process;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Павел
 */
public class JDBCConnection {
    
    
    public enum RequestType { JARUS, DPI };
//    private final Map<String, HTableInterface> hbaseTables = new HashMap<>();
    private boolean isDpi;
    

    
    Connection jdbcConn = null;

    private JDBCConnection(){

        String url = "";
        
        
        try{
            Class.forName("org.postgresql.Driver");
            System.out.println("Драйвер подключен");            
        }
        catch(ClassNotFoundException ex)
        {
            System.out.println("!!!CLASS_NOT_FOUND_ERROR!!!");
            ex.printStackTrace();
        }   
        
        

        url = "jdbc:postgresql://localhost:5432/wsobjects?user=postgres&password=1234&ssl=false";

        try
        {
            jdbcConn = DriverManager.getConnection(url);
            // use connection
        }
        catch (Exception e)
        {
            // log error
            System.out.println("!!!SQL Connection ERROR ");
            e.printStackTrace();
        }
        
        
    }
    
    public static JDBCConnection connect () {
        return new JDBCConnection();
    }
    
    
//    public void disconnect(){
//        closeTables();
//        closeConnection();
//    }  
    public void disconnect()
    {
        if (jdbcConn != null)
        {
            try { jdbcConn.close(); } catch (SQLException e) {}
        }      
        
        System.out.println("JDBC Connection closed");
    }

    
    public ArrayList<Object> getResult(String k, long startDate, long endData)
    {       
        try{
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver connected");            
        }
        catch(ClassNotFoundException ex)
        {
            System.out.println("!!!CLASS_NOT_FOUND_ERROR!!!");
            ex.printStackTrace();
        }   
        
        Statement  stmt = null;
        ResultSet rs = null;
        
        try
        {  
            if(jdbcConn == null) System.out.println("!!!!!!!!!!! jdbcConn Nullpointer ERROR !!!!!");
            
            stmt = jdbcConn.createStatement();
            String sql;
            sql = " SELECT * FROM table " + 
                  " WHERE id='" + k + 
                  "' AND dat>'" + startDate +
                  "' AND dat<'" + endData + "';";
            
            System.out.println("SQL_Str: " + sql);
            
            rs = stmt.executeQuery(sql);
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("!!!!!!!!!!! SQL QUERY ERROR !!!!!");
        }
        
         return new ArrayList();   
    }    
               
    

}

