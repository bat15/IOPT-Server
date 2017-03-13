/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.iot.entities;

import java.util.ArrayList;
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
    
    public String geUsertId()
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
    
    
    @Override
    public String toString()
    {
        String result = "{";
        
        result += "\"id\":\"" + id + "\",";
        
        result += "\"pathUnit\":\"" + pathUnit + "\",";
        
        
        result += "\"objects\":\"";
        if(!objects.isEmpty())
        {
            result += "[";
            int i = 0;
            for(bat15.iot.entities.Object object:objects)
            {
                
                if(i < objects.size() - 1) result += object.toString() + ",";
                else result += object.toString();
                
                i++;
            }
            result += "],";
        }
        
        result += "\"name\":\"" + name + "\" ";
        
        result += "}";
        
        return result;
    }
    
           
}
