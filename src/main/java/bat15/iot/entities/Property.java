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
@XmlRootElement(name="Property")
public class Property {
    
    @XmlElement(name="Scripts")
    ArrayList<Script> scripts;    
    
    @XmlElement(name="Name")
    String name;  
    
    @XmlElement(name="Id")
    String id; 
    
    @XmlElement(name="Type")
    String type;          

    @XmlElement(name="Value")
    String value;   
    
    
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
            
    
}
