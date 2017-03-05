/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.iot.entities;

import java.util.ArrayList;
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

    @XmlElement(name="Properties")
    ArrayList<Property> properties;
    
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
    
    public ArrayList<Property> getProperties()
    {
        return properties;
    }    
    
    public void setObjects(ArrayList<Property> properties)
    {
        this.properties = properties;
    }        
    
    public void addObject(Property property)
    {
        this.properties.add(property);
    }
    
    public void addAllObjects(ArrayList<Property> properties)
    {
        this.properties.addAll(properties);
    }
        
}
