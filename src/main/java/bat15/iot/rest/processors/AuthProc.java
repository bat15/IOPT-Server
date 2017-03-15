/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.iot.rest.processors;


import bat15.server.Settings;
import bat15.server.JDBCConnection;
import bat15.server.Security;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import javax.annotation.Resource;




import java.security.MessageDigest;

/**
 *
 * @author Павел
 */
@Stateless
@LocalBean
public class AuthProc {
    
    @Resource(lookup = "IOPT-Server")
    private Properties settings;
    
    JsonParser jsonParser; 
    
    JDBCConnection connection;

    private final Object lock = new Object();
    
    @PostConstruct
    @Schedule(second="0", minute="0", hour="*/6")
    public void init() {
        synchronized (lock) {           
            // проверяем пропертисы, которые сохранены в glassfish'е
            Settings.validate(settings);
            jsonParser = new JsonParser();
            
            String host = "";
            String port = "";
            String dbName = "";
            String user = "";
            String password = "";
            
            
            try{
                
                host = settings.getProperty("db_host");
                port = settings.getProperty("db_port");
                dbName = settings.getProperty("db_name");
                user = settings.getProperty("db_user");
                password = Security.getPasswordFromFile(settings.getProperty("db_passfile"));

                
                connection = JDBCConnection.createConnection(host, port, dbName, user, password);   
                
                
                
            }catch(Exception ex){
                System.out.println("jdbcConnection connect fail");
                
                System.out.println("host: " + host);
                System.out.println("port: " + port);
                System.out.println("dbName: " + dbName);
                System.out.println("user: " + user);
                System.out.println("password: " + password);
                
                System.out.println("settings db_passfile: " + settings.getProperty("db_passfile"));
                
                ex.printStackTrace();
            }        
            
            //if(connection.) System.out.println("JDBC connection Established!");
            
            
            System.out.println("tables: " + connection.showTables());
            
            
            
//            try{
//                hbaseConnection = HbaseConnection.connect(settings);   
//            }catch(Exception ex){ex.printStackTrace();}
        }
    } 
    
    
    @PreDestroy
    public void shutdown()
    {
        try{
            connection.disconnect();
        }catch(Exception ex){
            System.out.println("jdbcConnection disconnect try");
            ex.printStackTrace();
        }        
        
//        try{
//            hbaseConnection.disconnect();
//        }catch(Exception ex){ex.printStackTrace();}
//        
//        System.out.println("########## WebTechCcbo Bean shutted down.  Shutdown at : "+new Date());
    }    

    
    public boolean authByPassword(String login, String passwordFromClient)
    {
        boolean flag = false;
        String password = null;
        
        
        
        try{
            password = connection.selectValue(Settings.USERS_TABLE_NAME, "\"password\"", "login", login);
            
            System.out.println("password: " + password);
        }catch(Exception ex){
            return false;
        }
        
        return password!= null && passwordFromClient.replace(" ", "").equals(password.replace(" ", ""));
    }
    
    
    public String getCookieByLogin(String user, String ip)//for tests only
    {
        String id = null;
        
        try{
            id = connection.selectValue(Settings.USERS_TABLE_NAME, "\"id\"", "login", user);
        }catch(Exception ex)
        {
            return "EXCEPTION_FAIL";
        }
        
        
        HashMap<String, String> whereFields = new HashMap();
        whereFields.put("ip", ip);
        whereFields.put("id_user", id);
        
        try{
            return connection.selectValue(Settings.COOKIES_TABLE_NAME, "\"value\"", whereFields);
        }catch(Exception ex)
        {
            return "EXCEPTION_FAIL";
        }  
    }
    
    public boolean authByHash(String hashForCheck, boolean isAppkey)
    {
        
        boolean flag = false;
        String userId = null;
            
        if(isAppkey)
        {
            try{
                userId = connection.selectValue(Settings.APPKEYS_TABLE_NAME, "\"id_user\"", "value", hashForCheck);
            }catch(Exception ex){
                flag = false;
            }              
        }
        else
        {
            try{
                userId = connection.selectValue(Settings.COOKIES_TABLE_NAME, "\"id_user\"", "value", hashForCheck);
            }catch(Exception ex){
                flag = false;
            }            
        }
        
        
        if(userId != null) flag = true;
        
        return flag;        
    }
    
    public String getUserIdByHash(String hashForCheck, boolean isAppkey)
    {
        
        boolean flag = false;
        String userId = null;
            
        if(isAppkey)
        {
            try{
                userId = connection.selectValue(Settings.APPKEYS_TABLE_NAME, "\"id_user\"", "value", hashForCheck);
            }catch(Exception ex){
                flag = false;
            }              
        }
        else
        {
            try{
                userId = connection.selectValue(Settings.COOKIES_TABLE_NAME, "\"id_user\"", "value", hashForCheck);
            }catch(Exception ex){
                flag = false;
            }            
        }
        
        
        return userId;     
    }   
    
    public String getUserIdByLogin(String login)
    {
        
        boolean flag = false;
        String userId = null;
            

        try{
            userId = connection.selectValue(Settings.USERS_TABLE_NAME, "\"id\"", "login", login);
        }catch(Exception ex){
            flag = false;
        }              

        return userId;     
    }      
    
    
    
    
    
  
    
    public String upsertCookie(String sessionId, String login, String ipAddress)
    {
        boolean flag = false;
        
        String newCookieValue = null;
        
        ArrayList<String> tables = new ArrayList();
        tables.add(Settings.USERS_TABLE_NAME);
        tables.add(Settings.COOKIES_TABLE_NAME);
        
        ArrayList<String> whereConditions = new ArrayList();
        whereConditions.add("\"login\"='" + login + "'");
        whereConditions.add("\"id_user\"=\"user\".\"id\"");
        
        
        
        String foundUserId = connection.selectValue(tables, "\"id_user\"", whereConditions);
        
        
        System.out.println("foundUserId: " + foundUserId);

        boolean isExist = false;
        
        if(foundUserId!=null && !foundUserId.isEmpty()) isExist = true;
        
        
        int iter = 0;
        while(!flag && iter < 5)
        {
            iter++;
            
            
            try{
                if(!flag) newCookieValue = sessionId + Security.getHashFromString((new Date()).getTime());
            }catch(Exception ex){}          
            
            
            
            if(!isExist)
            {
                if(foundUserId==null || foundUserId.isEmpty()) foundUserId = connection.selectValue(Settings.USERS_TABLE_NAME, "\"id\"", "login", login);
                
                
                Map<String,String> fields = new HashMap();
                fields.put("id_user", foundUserId);
                fields.put("ip", ipAddress);
                fields.put("value", newCookieValue);
                     
                flag = connection.insertRow(Settings.COOKIES_TABLE_NAME, fields);
            }else{

                Map<String,String> setFields = new HashMap();
                setFields.put("value", newCookieValue);

                Map<String,String> whereFields = new HashMap();
                whereFields.put("ip", ipAddress);
                
                flag = connection.updateQuery(Settings.COOKIES_TABLE_NAME, setFields, whereFields);
            }
        }    
        
        return newCookieValue;
    }
    
    public String getAppkey(String idModel, String cookieValue)
    {
        
        ArrayList<String> tables = new ArrayList();
        tables.add(Settings.COOKIES_TABLE_NAME);
        tables.add(Settings.USERS_TABLE_NAME);
        tables.add(Settings.APPKEYS_TABLE_NAME);
        
        ArrayList<String> whereConditions = new ArrayList();  
        whereConditions.add("\"cookie\".\"value\"='" + cookieValue + "'");
        whereConditions.add("\"cookie\".\"id_user\"=\"user\".\"id\"");
        whereConditions.add("\"appkey\".\"id_user\"=\"user\".\"id\"");
        whereConditions.add("\"id_model\"='" + idModel + "'");


        
        String appkey = null;
        try{
            appkey = connection.selectValue(tables, "\"appkey\"",  "\"value\"", whereConditions);
        }catch(Exception ex){
        
        }
        
        return appkey;
        
    }    
    
    public void generateAppkey(String idModel, String cookieValue, String newAppkey)
    {
        String idUser = null;
        
        ArrayList<String> tables = new ArrayList();
        tables.add(Settings.USERS_TABLE_NAME);
        tables.add(Settings.COOKIES_TABLE_NAME);
        
        
        ArrayList<String> whereConditions = new ArrayList();
        whereConditions.add("\"cookie\".\"value\"='" + cookieValue + "'" );
        whereConditions.add("\"user\".\"id\"=\"cookie\".\"id_user\"");
        
        
        
        
        try{
            idUser = connection.selectValue(tables, "\"id_user\"", whereConditions);
        }catch(Exception ex){}
        
        Map<String, String> setFields = new HashMap();       
        setFields.put("value", newAppkey);
        
        Map<String, String> whereFields = new HashMap();  
        whereFields.put("id_user", idUser);
        whereFields.put("id_model", idModel);
        
        
        System.out.println("!!!before update");
        System.out.print("idModel: " + idModel);
        System.out.print("idUser: " + idUser);
        System.out.print("newAppkey: " + newAppkey);        
        
        
        try{
            connection.updateQuery(Settings.APPKEYS_TABLE_NAME, setFields, whereFields);
        }catch(Exception ex){}
        
    }
        

    

 
    
}