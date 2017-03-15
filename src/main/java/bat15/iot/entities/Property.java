/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.iot.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Павел
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="property")
public class Property {
    
    public Property(String id, String objectId,  String name, String pathUnit, String value, String type){
        
        this.name = name;
        this.id = id;
        this.pathUnit = pathUnit;
        
        this.value = value;
        this.type = type;       
        this.objectId = objectId;
        
        scripts = new ArrayList();
    } 
    
    public Property(HashMap<String, String> fields){
        
        this.name = fields.get("name");
        this.id = fields.get("id");
        this.objectId = fields.get("id_object");
        
        this.pathUnit = fields.get("path_unit");    
        this.value = fields.get("value");
        this.type = fields.get("type");
        
        
        scripts = new ArrayList();
    }
    
    

    public String toJsonString()
    {
        String result = "{";
        
        result += "\"id\":\"" + id + "\",";
        
        result += "\"objectId\":\"" + objectId + "\",";
        
        result += "\"value\":\"" + value + "\",";
        
        result += "\"type\":\"" + type + "\",";
        
        result += "\"pathUnit\":\"" + pathUnit + "\",";
        
        
        result += "\"scripts\":";
        result += "[";
        if(scripts != null && !scripts.isEmpty())
        {
            int i = 0;
            for(Script script:scripts)
            {
                
                if(i < scripts.size() - 1) result += script.toJsonString() + ",";
                else result += script.toJsonString();
                
                i++;
            }
        }
        result += "],";
        
        
        result += "\"name\":\"" + name + "\" ";
        
        result += "}";
        
        return result;
    }
    
    
    @XmlElement(name="name")
    String name;  
    
    @XmlElement(name="id")
    String id; 
    
    @XmlElement(name="type")
    String type;          

    @XmlElement(name="value")
    String value;   
 
    @XmlElement(name="pathUnit")
    String pathUnit;  
    
    
    @XmlElement(name="objectId")
    String objectId;   
    
    
    @XmlElement(name="scripts")
    ArrayList<Script> scripts;      
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    } 
    



     
    
    public String getPathUnit()
    {
        return pathUnit;
    }
    
    public void setPathUnit(String pathUnit)
    {
        this.pathUnit = pathUnit;
    }
    
    
  
    
    public String getObjectId()
    {
        return objectId;
    }
    
    public void setObjectId(String objectId)
    {
        this.objectId = objectId;
    }       
    
    
    
    
    
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }


    public String getValue()
    {
        return value;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }    
    
    
    public ArrayList<Script> getScripts()
    {
        return scripts;
    }    
    
    public void setScripts(ArrayList<Script> scripts)
    {
        this.scripts = scripts;
    }        
    
    public void addScript(Script script)
    {
        this.scripts.add(script);
    }
    
    public void addAllScripts(ArrayList<Script> scripts)
    {
        this.scripts.addAll(scripts);
    }
            
    public static Property parseJsonPropertyOnly(String propertyBody)
    {
        JsonParser parser = new JsonParser();
        JsonObject jsonProperty = parser.parse(propertyBody).getAsJsonObject();     
        
        String idStr = jsonProperty.get("id").getAsString();
        String name = jsonProperty.get("name").getAsString();
        String pathUnit = jsonProperty.get("pathUnit").getAsString();
        String objectId = jsonProperty.get("objectId").getAsString();
        String value = jsonProperty.get("value").getAsString();
        String type = jsonProperty.get("type").getAsString();
   
        Property newProperty = new Property(idStr, objectId, name, pathUnit, value, type);

        return newProperty;
    }
    
    
    public static Property parseJsonPropertyAndNested(String propertyBody)
    {
        JsonParser parser = new JsonParser();
        JsonObject jsonProperty = parser.parse(propertyBody).getAsJsonObject();    
        
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
            
            Script newScript = Script.parseJsonScriptOnly(script.getAsJsonObject().toString());
            
            //insertScriptInDB(newScript);
            if(newScript != null) newProperty.addScript(newScript);
            
            scriptsCount++;
        }
        
        System.out.println("scriptsCount: " + scriptsCount);
        System.out.println("newProperty.getScripts().size(): " + newProperty.getScripts().size());
            
        return newProperty;
    }
    
    
}
