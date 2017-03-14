/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.iot.rest.process;


import bat15.iot.store.JDBCConnection;
import bat15.security.Security;
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
public class ProcessorGUIClient {
    
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
    

    
    public void putPropertyValue(String data) {
        
        
    }
    




    
    public String getPropertyValue(int type){
        
        String data = "";
        
        if(type == 0) data = "{test_json: {name: value}}";
        else if(type ==1) data = "{name: value}";
                
                
        return data;
    }
    
//    public String getNoModel(){
////        Path file = Paths.get("the-file-name.txt");
////        Files.write(file, lines, Charset.forName("UTF-8"));
//        return getModel("");
//    }    
    
    
    
    public String getModel(String modelQuery, String userId, String newValue, boolean isMinimal){
        
        String testData = "FAIL";
        String jsonData = "NONE";
         
        if(modelQuery == null) {
            testData = "[{error: null}]";
            return testData;
        }
        else if(modelQuery.isEmpty()) {
            testData = "[{error : no_model_name_defined}]";
            return testData;
        }
        
        String model = "";
        String object = "";
        String property = "";
        String script = "";
        
        ArrayList<String> paths = new ArrayList();
        
        System.out.println("modelQuery: " + modelQuery);
        
        paths.addAll(Arrays.asList(modelQuery.split("/")));
        
        
        
        
        switch (paths.size()) {
            case 0://error - no model_name defined
                testData = "[{error : no_model_name_defined}]";
                System.out.println("case 0 size: " + paths.size());
                System.out.println("!!! " + testData);
                
                break;
            case 1://model_name
                System.out.println("case 1 size: " + paths.size());
                
                try{
  
                    String value = null;
                    HashMap<String, String> pathUnits = new HashMap();
                    
                    pathUnits.put("model", paths.get(0));
  
                    if(newValue == null) {
                        
                        System.out.println("GET SCRIPT PATH: " + pathUnits.toString());
                        value = getModelJson(pathUnits, userId);
                        
                        System.out.println("Script value: " + value);
                        return value;
                    }
                    else return null;
  
                }catch(Exception ex){}
                
                break;
            case 2://object_name
                try{
  
                    String value = null;
                    HashMap<String, String> pathUnits = new HashMap();
                    
                    pathUnits.put("model", paths.get(0));
                    pathUnits.put("object", paths.get(1));
                    
  
                    if(newValue == null) {
                        
                        System.out.println("GET SCRIPT PATH: " + pathUnits.toString());
                        value = getObjectJson(pathUnits, userId);
                        
                        System.out.println("Script value: " + value);
                        return value;
                    }
                    else return null;
                    
                }catch(Exception ex){}
                
                
                break;
            case 3://property_name
                try{
  
                    String value = null;
                    HashMap<String, String> pathUnits = new HashMap();
                    
                    pathUnits.put("model", paths.get(0));
                    pathUnits.put("object", paths.get(1));
                    pathUnits.put("property", paths.get(2));
                    
  
                    if(newValue == null) {
                        
                        System.out.println("GET SCRIPT PATH: " + pathUnits.toString());
                        value = getPropertyJson(pathUnits, userId, isMinimal);
                        
                        System.out.println("Script value: " + value);
                        return value;
                    }
                    else{
                        
                        return putPropertyValue(pathUnits,userId,newValue);
                        
                    }
                }catch(Exception ex){}

                    
                break;
                
            case 4://script_name
                try{
  
                    HashMap<String, String> pathUnits = new HashMap();
                    
                    pathUnits.put("model", paths.get(0));
                    pathUnits.put("object", paths.get(1));
                    pathUnits.put("property", paths.get(2));
                    pathUnits.put("script", paths.get(3));
  
                    if(newValue == null) {
                        
                        System.out.println("GET SCRIPT PATH: " + pathUnits.toString());
                        String value = getScriptJson(pathUnits, userId, isMinimal);
                        
                        System.out.println("Script value: " + value);
                        return value;
                    }
                    else{
                        return putScriptValue(pathUnits,userId,newValue);
                    }

                }catch(Exception ex){}
                

                
                break;      
            default://error - to much arguments
                testData = "[{error : to_much_arguments}]";
                System.out.println("!!! " + testData);
                break;
        }
            
        String response;
        
                
        return  jsonData;
    }
    
    
    
    
 
    public String putScriptValue(HashMap<String, String> pathUnits, String userId,String value)
    {
        String result = null;
        if(connection.updateScriptValueByPath(pathUnits, userId, value)) result = "OK";
        else result = "FAIL";
     
        return result;
    }
            
    public String putPropertyValue(HashMap<String, String> pathUnits, String userId,String value)
    {
        String result = null;
        if(connection.updatePropertyValueByPath(pathUnits, userId, value)) result = "OK";
        else result = "FAIL";
     
        return result;
    }
    

   
    
    public String getScriptJson(HashMap<String, String> pathUnits, String userId, boolean isMinimal)
    {
        String jsonResult = null;
        HashMap<String, String> result = new HashMap();

        try{
            result = connection.selectScriptValueByPath(pathUnits, userId);
        } catch(Exception ex){
            System.out.println();
        }
        
        
        jsonResult = "{";
        if(!result.isEmpty())
        {
            if(!isMinimal){
                    jsonResult += 
                            "\"name\":\"" + result.get("name") + "\"," +
                            "\"value\":" + result.get("value");

            }
            else{
                jsonResult += "\"" + result.get("name") +  "\":\"" + result.get("value") + "\"";

            }
        }
        
        
        jsonResult += "}";
        
        
        return jsonResult;
    }
    
    
    public String getPropertyJson(HashMap<String, String> pathUnits, String userId, boolean isMinimal)
    {
        String jsonResult = null;
        HashMap<String, String> result = new HashMap();

        try{
            result = connection.selectPropertyValueByPath(pathUnits, userId);
        } catch(Exception ex){
            System.out.println();
        }
        
        jsonResult = "{";
        
        if(!result.isEmpty())
        {
            if(!isMinimal){

                jsonResult += "\"name\":\"" + result.get("name") + "\","
                        + "\"type\":" + result.get("type") + ","
                        + "\"value\":\"" + result.get("value") + "\"";
            }else
            {
                jsonResult += "\"" + result.get("name") +  "\":\"" + result.get("value") + "\"";
                        
            }
        }
        
        jsonResult += "}";
        
        return jsonResult;
    } 

    public String getObjectJson(HashMap<String, String> pathUnits, String userId)
    {
        String jsonResult = null;
        HashMap<String, String> result = new HashMap();

        try{
            result = connection.selectObjectValueByPath(pathUnits, userId);
        } catch(Exception ex){
            System.out.println();
        }
        
        jsonResult = "{"
                + "\"name\":" + result.get("name")
                + "}";
        
        return jsonResult;
    } 
    
    public String getModelJson(HashMap<String, String> pathUnits, String userId)
    {
        String jsonResult = null;
        HashMap<String, String> result = new HashMap();

        try{
            result = connection.selectModelValueByPath(pathUnits, userId);
        } catch(Exception ex){
            System.out.println();
        }
        
        jsonResult = "{"
                + "\"name\":" + result.get("name")
                + "}";
        
        return jsonResult;
    } 
    
    
    
    public String getModelFromSnapshotJson(String snapshotJson, String modelId)
    {
     
        JsonObject snapshotObject = null;
        
        try{
            snapshotObject = (JsonObject) jsonParser.parse(snapshotJson);
        }catch(Exception ex){} 
        
        JsonArray models = snapshotObject.getAsJsonArray("Models");

        
        JsonObject modelJson = null;
        for(int i=0; i<models.size(); i++){
            
            
            
            JsonObject innerObject = (JsonObject) models.get(i);
            
                System.out.println("The " + i + " element of the models array: "+ innerObject);
                
//                String currentModelId = (String)innerObject.get("ModelId");
                
//                if(currentModelId.equals(modelId)) return innerObject.toJSONString(); 
        }
      
        return "NOT_FOUND";
    }
    
    
    public void putProperty(String propertyJson)
    {
        
        
        JsonObject snapshotObject = null;
        
        try{
            snapshotObject = (JsonObject) jsonParser.parse(propertyJson);
        }catch(Exception ex){}
        
        
        System.out.println("snapshotObject: " + snapshotObject);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

}