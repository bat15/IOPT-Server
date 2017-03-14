/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.iot.entities;

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
@XmlRootElement(name="Object")
public class Object {

    
    public Object(String id, String modelId, String name, String pathUnit){
        
        this.name = name;
        this.id = id;
        this.pathUnit = pathUnit;
        this.modelId = modelId;
        
        properties = new ArrayList();
    }

    public Object(HashMap<String, String> fields){
        
        this.name = fields.get("name");
        this.id = fields.get("id");
        this.modelId = fields.get("id_model");
        this.pathUnit = fields.get("path_unit");
        
        
        properties = new ArrayList();
    }
    
    @XmlElement(name="name")
    String name;
    
    @XmlElement(name="id")
    String id;
    
    @XmlElement(name="pathUnit")
    String pathUnit;   
    
    @XmlElement(name="modelId")
    String modelId;  
    
    
    
    @XmlElement(name="properties")
    ArrayList<Property> properties;   
    

    public String toJsonString()
    {
        String result = "{";
        
        result += "\"id\":\"" + id + "\",";
        
        result += "\"modelId\":\"" + modelId + "\",";
        
        result += "\"pathUnit\":\"" + pathUnit + "\",";
        
        result += "\"properties\":\"";
        result += "[";
        if(properties != null && !properties.isEmpty())
        {
            
            int i = 0;
            for(Property property:properties)
            {
                
                if(i < properties.size() - 1) result += property.toString() + ",";
                else result += property.toString();
                
                i++;
            }
            
        }
        result += "]";
        
        result += "\"name\":\"" + name + "\" ";
        
        result += "}";
        
        return result;
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
    
    

    
    public String getModelId()
    {
        return modelId;
    }
    
    public void setModelId(String modelId)
    {
        this.modelId = modelId;
    }       
    
    
    
    
    public ArrayList<Property> getProperties()
    {
        return properties;
    }    
    
    public void setProperties(ArrayList<Property> properties)
    {
        this.properties = properties;
    }        
    
    public void addProperty(Property property)
    {
        this.properties.add(property);
    }
    
    public void addAllProperties(ArrayList<Property> properties)
    {
        this.properties.addAll(properties);
    }
    
    
    
    
        
}
