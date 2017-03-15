/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.iot.entities;

import bat15.server.Settings;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Павел
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Model")
public class Model {
    
    
    public Model(String id, String userId, String name, String pathUnit){
        
        this.name = name;
        this.id = id;
        this.userId = userId;
        this.pathUnit = pathUnit;
        
        
        objects = new ArrayList();
    }
    
    public Model(String id, String name, String pathUnit){
        
        this.name = name;
        this.id = id;
        this.pathUnit = pathUnit;
        
        
        objects = new ArrayList();
    }
    
    
    public Model(HashMap<String, String> fields){
        
        
        this.name = fields.get("name");
        this.id = fields.get("id");
        this.userId = fields.get("id_user");
        this.pathUnit = fields.get("path_unit");
        
        
        objects = new ArrayList();
    }
    
    
    @XmlElement(name="name")
    String name;
    
    @XmlElement(name="id")
    String id;
    
    @XmlElement(name="userId")
    String userId;
    
    @XmlElement(name="pathUnit")
    String pathUnit;
    
    
    @XmlElement(name="objects")
    ArrayList<Object> objects;  

    
    
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
    
    public String getUsertId()
    {
        return userId;
    }
    
    public void setUserId(String id)
    {
        this.userId = userId;
    } 
    
    
    public String getPathUnit()
    {
        return pathUnit;
    }
    
    public void setPathUnit(String pathUnit)
    {
        this.pathUnit = pathUnit;
    }
    

    
    
    public ArrayList<Object> getObjects()
    {
        return objects;
    }    
    
    public void setObjects(ArrayList<Object> objects)
    {
        this.objects = objects;
    }        
    
    public void addObject(Object object)
    {
        this.objects.add(object);
    }
    
    public void addAllObjects(ArrayList<Object> objects)
    {
        this.objects.addAll(objects);
    }
    
    
    public static Model parseFromJson(String modelBody, String userId)
    {
        JsonParser parser = new JsonParser();
        JsonObject jsonModel = parser.parse(modelBody).getAsJsonObject();
        String idStr = jsonModel.get("id").getAsString();
        String name = jsonModel.get("name").getAsString();
        String pathUnit = jsonModel.get("pathUnit").getAsString();

        JsonArray jsonObjects = jsonModel.getAsJsonArray("objects");
     

        Model newModel = new Model(idStr,userId , name, pathUnit);

        return newModel;
    }
    
    public static Model parseJsonModelOnly(String modelBody)
    {
        JsonParser parser = new JsonParser();
        JsonObject jsonModel = parser.parse(modelBody).getAsJsonObject();
        String idStr = jsonModel.get("id").getAsString();
        String name = jsonModel.get("name").getAsString();
        String pathUnit = jsonModel.get("pathUnit").getAsString();


        Model newModel = new Model(idStr, name, pathUnit);

        return newModel;
    }
    
    public static Model parseJsonModelAndNested(String modelBody, String userId)
    {
        JsonParser parser = new JsonParser();
        JsonObject jsonModel = parser.parse(modelBody).getAsJsonObject();     
        String idStr = jsonModel.get("id").getAsString();
        String name = jsonModel.get("name").getAsString();
        String pathUnit = jsonModel.get("pathUnit").getAsString();

        JsonArray jsonObjects = jsonModel.getAsJsonArray("objects");
     


        Model newModel = new Model(idStr,userId , name, pathUnit);


        //-------OBJECTS--------------------------------------------
        int objectsCount = 0;
        
        for(JsonElement object:jsonObjects)
        {
            
            bat15.iot.entities.Object newObject = bat15.iot.entities.Object.parseJsonObjectAndNested(object.getAsJsonObject().toString());
            //insertObjectInDB(newObject);
            if(newObject != null) newModel.addObject(newObject);
            objectsCount++;
        }
            
        System.out.println("objectsCount: " + objectsCount);
        System.out.println("newModel.getObjects().size(): " + newModel.getObjects().size());
        
        return newModel;
    }
    
    
    public String toJsonString()
    {
        String result = "{";
        
        result += "\"id\":\"" + id + "\",";
        
        result += "\"pathUnit\":\"" + pathUnit + "\",";
        
        
        result += "\"objects\":";
        result += "[";
        if(objects != null && !objects.isEmpty())
        {
            
            int i = 0;
            for(bat15.iot.entities.Object object:objects)
            {
                
                if(i < objects.size() - 1) result += object.toJsonString() + ",";
                else result += object.toJsonString();
                
                i++;
            }
            
        }
        result += "],";
        
        result += "\"name\":\"" + name + "\" ";
        
        result += "}";
        
        return result;
    }
    
           
}
