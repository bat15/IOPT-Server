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
@XmlRootElement(name="script")
public class Script {
  
    public Script(String id, String propertyId, String name, String pathUnit, String value){
        
        this.name = name;
        this.id = id;
        this.pathUnit = pathUnit;
        this.value = value;
        
        this.propertyId = propertyId;

    }     
    
    public Script(HashMap<String, String> fields){
        
        this.name = fields.get("name");
        this.id = fields.get("id");
        this.propertyId = fields.get("id_property");
        
        this.pathUnit = fields.get("path_unit");    
        this.value = fields.get("value");
        
    }
    
    
    
    @XmlElement(name="name")
    String name;
    
    @XmlElement(name="id")
    String id; 

    @XmlElement(name="value")
    String value;
    
    @XmlElement(name="pathUnit")
    String pathUnit;  
    
    @XmlElement(name="propertyId")
    String propertyId;  
    

    public String toJsonString()
    {
        String result = "{";
        
        result += "\"id\":\"" + id + "\",";
        
        result += "\"propertyId\":\"" + propertyId + "\",";
        
        result += "\"value\":\"" + value + "\",";

        
        result += "\"pathUnit\":\"" + pathUnit + "\",";
        
        result += "\"name\":\"" + name + "\" ";
        
        result += "}";
        
        return result;
    }
    
    
    public static Script parseJsonScriptOnly(String scriptBody)
    {
        JsonParser parser = new JsonParser();
        JsonObject jsonScript = parser.parse(scriptBody).getAsJsonObject();    
        
        String idStr = jsonScript.get("id").getAsString();
        String propertyId = jsonScript.get("propertyId").getAsString();
        
        String name = jsonScript.get("name").getAsString();
        String pathUnit = jsonScript.get("pathUnit").getAsString();
        String value = jsonScript.get("value").getAsString();
   
        Script newScript = new Script(idStr, propertyId, name, pathUnit, value);
         
        return newScript;
    }
    

    
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
    
    
 
    
    public String getPropertyId()
    {
        return propertyId;
    }
    
    public void setPropertyId(String propertyId)
    {
        this.propertyId = propertyId;
    }    
    
    
    
    
    public String getValue()
    {
        return value;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    } 
    
    
}
