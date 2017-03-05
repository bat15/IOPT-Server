/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.iot.entities;

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
    String id;
    
    @XmlElement(name="Parent")
    Object parent;

    @XmlElement(name="Mappings")
    PropertyMap mappings;

    public class PropertyMap
    {
        public Property property;
        
        public boolean isControl;
        
        public int min;
        
        public int max;
    }
}