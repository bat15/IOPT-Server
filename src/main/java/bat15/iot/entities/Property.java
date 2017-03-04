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

    @XmlElement(name="Type")
    String type;        
    
    @XmlElement(name="Id")
    String id;    

    @XmlElement(name="Value")
    String value;        
}
