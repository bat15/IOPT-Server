/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.iot.rest.processors;


import bat15.iot.store.JDBCConnection;
import bat15.iot.entities.Model;
import bat15.iot.entities.Property;
import bat15.iot.entities.Script;
import bat15.security.Security;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
public class ProcessorLoadModel {
    
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
//                hbaseConnection = HbaseConnection.connect(properties);   
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
    
    

   
    

    


    public Long insertModelInDB(Model model)
    {

        Long id = null;
        
        id = Long.parseLong(model.getId());
        
        
        
        System.out.println("BEFORE INSERT");
        
        if(id < 0 || id==null)
            id = connection.getMaxId(Settings.MODELS_TABLE_NAME);
   

        id++;
        
        HashMap<String, String> fields = new HashMap();
        fields.put("id", id.toString());
        fields.put("id_user", model.geUsertId());

        fields.put("name", model.getName());
        fields.put("path_unit", model.getPathUnit());
        
        
        try{
            connection.insertRow(Settings.MODELS_TABLE_NAME, fields);
        }catch(Exception ex){
            return null;
        }
     
        return id;

    }
    
    
    public Long insertObjectInDB(bat15.iot.entities.Object object, String modelId)
    {
      
        
        Long id = null;
        
        id = Long.parseLong(object.getId());
        
        
        
        if(id < 0 || id==null)
            id = connection.getMaxId(Settings.OBJECTS_TABLE_NAME);


        id++;

        
        HashMap<String, String> fields = new HashMap();
        fields.put("id_model", modelId);
//            fields.put("id_model", object.getModelId());

        fields.put("id", id.toString());

        fields.put("name", object.getName());
        fields.put("path_unit", object.getPathUnit());

        
        try{
            connection.insertRow(Settings.OBJECTS_TABLE_NAME, fields);
        }catch(Exception ex){return null;}
    
        return id;

    }   
    
    
    public Long insertPropertyInDB(Property property, String objectId)
    {
  
        Long id = null;
        
        id = Long.parseLong(property.getId());
        
        
        
        if(id < 0 || id==null)
            id = connection.getMaxId(Settings.PROPERTIES_TABLE_NAME);
        
            
        id++;

        HashMap<String, String> fields = new HashMap();
        
        
        fields.put("id_object", objectId);
//            fields.put("id_object", property.getObjectId());



        fields.put("id", id.toString());


        fields.put("type", property.getType());
        fields.put("name", property.getName());
        fields.put("value", property.getValue());
        fields.put("path_unit", property.getPathUnit());


            
        try{
            connection.insertRow(Settings.PROPERTIES_TABLE_NAME, fields);
        }catch(Exception ex){return null;}
     
        return id;
    }   
    
    public Long insertScriptInDB(Script script, String propertyId)
    { 
        Long id = null;
        
        id = Long.parseLong(script.getId());
        
        
        
        
        if(id < 0 || id==null)
            id = connection.getMaxId(Settings.SCRIPTS_TABLE_NAME);

            
            
        id++;
        
        HashMap<String, String> fields = new HashMap(); 
        fields.put("id_property", propertyId);
//            fields.put("id_property", script.getPropertyId());

        fields.put("id", id.toString());


        fields.put("name", script.getName());
        fields.put("value", script.getValue());
        fields.put("path_unit", script.getPathUnit());


            
        try{
            connection.insertRow(Settings.SCRIPTS_TABLE_NAME, fields);
        }catch(Exception ex){
            return null;
        }   
     
        
        return id;
    }
    
    
    
    
    
    
    
    
  
    
    public ArrayList<Model> getModelsFromDB(String userId)
    {
        
        JsonParser parser = new JsonParser();
        
        ArrayList<Model> models = new ArrayList();
          
        
        ArrayList<String> tables = new ArrayList();
        tables.add(Settings.MODELS_TABLE_NAME);

        ArrayList<String> whereConditions = new ArrayList();
        whereConditions.add("\"id_user\"=" + userId + "");       
        
        ArrayList<String> modelIds = connection.selectList(tables, "\"id\"", whereConditions);

        
        ArrayList<String> selectKeys = new ArrayList();
        selectKeys.add("\"id\"");
        selectKeys.add("\"id_user\"");
        selectKeys.add("\"name\"");
        selectKeys.add("\"path_unit\"");
        
 
        
        for(String id:modelIds){
            
            System.out.println("id: " + id);
            
            whereConditions = new ArrayList();
            whereConditions.add("\"id\"=" + id);
            
            HashMap<String, String> modelFields = connection.selectHash(tables, selectKeys, whereConditions);

            Model newModel = null;

            if(modelFields.size() != 4) {
                System.out.println("not enough arguments!");
                return null;
            }
            else{
                newModel = new Model(modelFields);
            } 
            
            
            newModel.setObjects(getObjectsFromDB(id));

            models.add(newModel);
        }
        
        

        
        return models;
    }
    
    
    public ArrayList<bat15.iot.entities.Object> getObjectsFromDB(String modelId)
    {
        ArrayList<bat15.iot.entities.Object> objects = new ArrayList();
        
        ArrayList<String> tables = new ArrayList();
        tables.add(Settings.OBJECTS_TABLE_NAME);

        ArrayList<String> whereConditions = new ArrayList();
        whereConditions.add("\"id_model\"=" + modelId + "");       
        
        ArrayList<String> objectIds = connection.selectList(tables, "\"id\"", whereConditions);

        
        ArrayList<String> selectKeys = new ArrayList();
        selectKeys.add("\"id\"");
        selectKeys.add("\"id_model\"");
        selectKeys.add("\"name\"");
        selectKeys.add("\"path_unit\"");
        
 
        
        for(String id:objectIds){
            
            System.out.println("id: " + id);

            whereConditions = new ArrayList();
            whereConditions.add("\"id\"=" + id);
        
            HashMap<String, String> objectFields = connection.selectHash(tables, selectKeys, whereConditions);

            bat15.iot.entities.Object newObject = null;

            if(objectFields.size() != 4) {
                System.out.println("not enough arguments!");
                return null;
            }
            else{
                newObject = new bat15.iot.entities.Object(objectFields);
            } 
            

            
            newObject.setProperties(getPropertiesFromDB(id));

            objects.add(newObject);
        }
            
        return objects;
    }
    
    
    public ArrayList<Property> getPropertiesFromDB(String objectId)
    {
        ArrayList<Property> properties = new ArrayList();
        
        ArrayList<String> tables = new ArrayList();
        tables.add(Settings.PROPERTIES_TABLE_NAME);
        

        ArrayList<String> whereConditions = new ArrayList();
        whereConditions.add("\"id_object\"=" + objectId + "");       
        
        ArrayList<String> propertyIds = connection.selectList(tables, "\"id\"", whereConditions);

        
        ArrayList<String> selectKeys = new ArrayList();
        selectKeys.add("\"id\"");
        selectKeys.add("\"id_object\"");
        selectKeys.add("\"name\"");
        selectKeys.add("\"path_unit\"");
        
 
        
        for(String id:propertyIds){
            
            System.out.println("id: " + id);

            whereConditions = new ArrayList();
            whereConditions.add("\"id\"=" + id);
        
            HashMap<String, String> propertyFields = connection.selectHash(tables, selectKeys, whereConditions);

            Property newProperty = null;

            if(propertyFields.size() != 4) {
                System.out.println("not enough arguments!");
                return null;
            }
            else{
                newProperty = new Property(propertyFields);
            } 
            

            
            newProperty.setScripts(getScriptsFromDB(id));

            properties.add(newProperty);
        }
            
        return properties;
    }
    
    
    
    public ArrayList<Script> getScriptsFromDB(String propertyId)
    {
        ArrayList<Script> scripts = new ArrayList();
        
        ArrayList<String> tables = new ArrayList();
        tables.add(Settings.SCRIPTS_TABLE_NAME);
        

        ArrayList<String> whereConditions = new ArrayList();
        whereConditions.add("\"id_property\"=" + propertyId + "");       
        
        ArrayList<String> scriptIds = connection.selectList(tables, "\"id\"", whereConditions);

        
        ArrayList<String> selectKeys = new ArrayList();
        selectKeys.add("\"id\"");
        selectKeys.add("\"id_object\"");
        selectKeys.add("\"name\"");
        selectKeys.add("\"path_unit\"");
        
 
        
        for(String id:scriptIds){
            
            System.out.println("id: " + id);

            whereConditions = new ArrayList();
            whereConditions.add("\"id\"=" + id);
        
            HashMap<String, String> scriptFields = connection.selectHash(tables, selectKeys, whereConditions);

            Script newScript = null;

            if(scriptFields.size() != 4) {
                System.out.println("not enough arguments!");
                return null;
            }
            else{
                newScript = new Script(scriptFields);
            } 
            

            scripts.add(newScript);
        }
            
        return scripts;
    }
    
    
    protected Model constructModelsFromJson(JsonObject jsonModel, String userId)
    {
        String idStr = jsonModel.get("id").getAsString();
        String name = jsonModel.get("name").getAsString();
        String pathUnit = jsonModel.get("pathUnit").getAsString();

        JsonArray jsonObjects = jsonModel.getAsJsonArray("objects");
     
        Long tmpLong = null;
        
        try{
            tmpLong = Long.parseLong(userId);
        }catch(Exception ex){}
        
        
        connection.deleteFromTableById(Settings.MODELS_TABLE_NAME, tmpLong);
        
        

        Model newModel = new Model(idStr,userId , name, pathUnit);


        //-------OBJECTS--------------------------------------------
        int objectsCount = 0;
        
        for(JsonElement object:jsonObjects)
        {
            
            bat15.iot.entities.Object newObject = constructObjectsFromJson(object.getAsJsonObject(), idStr);
            //insertObjectInDB(newObject);
            if(newObject != null) newModel.addObject(newObject);
            objectsCount++;
        }
            
        System.out.println("objectsCount: " + objectsCount);
        System.out.println("newModel.getObjects().size(): " + newModel.getObjects().size());
        
        return newModel;
    }
    
    
    protected bat15.iot.entities.Object constructObjectsFromJson(JsonObject jsonObject, String nestedModelId)
    {
        String idStr = jsonObject.get("id").getAsString();
        String name = jsonObject.get("name").getAsString();
        String pathUnit = jsonObject.get("pathUnit").getAsString();
        
        String modelId = jsonObject.get("modelId").getAsString();
        

        
        
        

        JsonArray jsonProperties = jsonObject.getAsJsonArray("properties");

        bat15.iot.entities.Object newObject = new bat15.iot.entities.Object(idStr, modelId, name, pathUnit);


        //-------PROPERTIES--------------------------------------------
        int propsCount = 0;
        
        for(JsonElement property:jsonProperties)
        {
            
            Property newProperty = constructPropertiesFromJson(property.getAsJsonObject(), idStr);
            //insertPropertyInDB(newProperty);
            if(newProperty != null) newObject.addProperty(newProperty);
            propsCount++;
        }
        
        System.out.println("propsCount: " + propsCount);
        System.out.println("newObject.getProperties().size(): " + newObject.getProperties().size());
            
        return newObject;
    }
    
    protected Property constructPropertiesFromJson(JsonObject jsonProperty, String nestedObjectId)
    {
        String idStr = jsonProperty.get("id").getAsString();
        String name = jsonProperty.get("name").getAsString();
        String pathUnit = jsonProperty.get("pathUnit").getAsString();
        String objectId = jsonProperty.get("objectId").getAsString();
        String value = jsonProperty.get("value").getAsString();
        String type = jsonProperty.get("type").getAsString();
        
        

   
        
        JsonArray jsonScripts = jsonProperty.getAsJsonArray("scripts");

        Property newProperty = new Property(idStr, objectId, name, pathUnit, value, type);


        //-------Scripts--------------------------------------------
        int scriptsCount = 0;
        
        for(JsonElement script:jsonScripts)
        {
            
            Script newScript = constructScriptFromJson(script.getAsJsonObject(), idStr);
            
            //insertScriptInDB(newScript);
            if(newScript != null) newProperty.addScript(newScript);
            
            scriptsCount++;
        }
        
        System.out.println("scriptsCount: " + scriptsCount);
        System.out.println("newProperty.getScripts().size(): " + newProperty.getScripts().size());
            
        return newProperty;
    }
    
    protected Script constructScriptFromJson(JsonObject jsonScript, String nestedPropertyId)
    {
        String idStr = jsonScript.get("id").getAsString();
        String propertyId = jsonScript.get("propertyId").getAsString();
        
        String name = jsonScript.get("name").getAsString();
        String pathUnit = jsonScript.get("pathUnit").getAsString();
        String value = jsonScript.get("value").getAsString();


        
        
        Script newScript = new Script(idStr, propertyId, name, pathUnit, value);
         
        return newScript;
    }    
    
   
    
    
    
    
    
    
    public String getProperty(String id)
    {
        String value = null;
        
        String resultKey = "value";
        
        
        
        try{
            value = connection.selectValue(Settings.PROPERTIES_TABLE_NAME, "\"" +resultKey + "\"", "id", id);
        } catch(Exception ex){
            System.out.println();
        }
        
        return value;
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