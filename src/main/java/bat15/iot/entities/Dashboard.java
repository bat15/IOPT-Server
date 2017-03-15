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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Павел
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="dashboard")
public class Dashboard {

    @XmlElement(name="id")
    protected String id;
    
    @XmlElement(name="parent")
    protected Object parent;

    @XmlElement(name="mappings")
    protected ArrayList<PropertyMap> mappings;

    @XmlTransient
    public class PropertyMap
    {
        @XmlTransient
        protected Integer id;
        
        @XmlTransient
        protected  Property property;
        
        @XmlTransient
        protected  Boolean isControl;
        
        @XmlTransient
        protected  Integer min;
        
        @XmlTransient
        protected  Integer max;
        
        
        
        public String toJsonString()
        {
            String result = "{";

            if(this.id != null)result += "\"id\":\"" + this.id + "\",";

            if(isControl!=null) result += "\"isControl\":\"" + isControl + "\",";

            if(min!=null) result += "\"min\":\"" + min + "\",";


            if(max!=null) result += "\"max\":\"" + max + "\",";

            if(property!=null) result += "\"property\":\"" + property.getName() + "\" ";

            result += "}";

            return result;
        }
    }
    
    
    public String toJsonString()
    {
        String result = "{";
        
        if(id != null) result += "\"id\":\"" + id + "\",";
        result += "\"mappings\":";
        result += "[";
        if(mappings != null && !mappings.isEmpty())
        {
            
            int i = 0;
            for(PropertyMap mapping:mappings)
            {
                
                if(i < mappings.size() - 1) result += mapping.toJsonString() + ",";
                else result += mapping.toJsonString();
                
                i++;
            }
            
        }
        result += "]";
       
        
        result += "}";
        
        return result;
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