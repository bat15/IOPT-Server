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
    
    @XmlElement(name="Objects")
    ArrayList<Object> objects;    
    
    @XmlElement(name="Name")
    String name;
    
    @XmlElement(name="Id")
    String id;
    
    
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
    
           
}
