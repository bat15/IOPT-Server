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
@XmlRootElement(name="Dashboard")
public class Dashboard {

    @XmlElement(name="Id")
    protected String id;
    
    @XmlElement(name="Parent")
    protected Object parent;

    @XmlElement(name="Mappings")
    protected ArrayList<PropertyMap> mappings;

    public class PropertyMap
    {
        protected  Property property;
        
        protected  boolean isControl;
        
        protected  int min;
        
        protected  int max;
        
        
    }
    
    
    public Object getParent()
    {
        return parent;
    }
    
    public void setParent(Object parent)
    {
        this.parent = parent;
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }    
    
    
    public ArrayList<PropertyMap> getMappings()
    {
        return mappings;
    }    
    
    public void setMappings(ArrayList<PropertyMap> mappings)
    {
        this.mappings = mappings;
    }        
    
    public void addMapping(PropertyMap mapping)
    {
        this.mappings.add(mapping);
    }
    
    public void addAllMappings(ArrayList<PropertyMap> mappings)
    {
        this.mappings.addAll(mappings);
    }    
}