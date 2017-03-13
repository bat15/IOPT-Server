/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.iot.entities;

import java.util.ArrayList;
import java.util.Date;
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
@XmlRootElement(name="snapshot")
public class Snapshot {
    
    
    public Snapshot(String id)
    {
        this.id = id;
        models = new ArrayList();
        dashboards = new ArrayList();
        
        lastUpdate = (new Date()).toString();
    }
    
    
    @XmlElement(name="models")
    protected ArrayList<Model> models;
    
    @XmlElement(name="dashboards")
    protected ArrayList<Dashboard> dashboards;

    @XmlElement(name="lastUpdate")
    protected String lastUpdate; 
    
    @XmlElement(name="id")
    protected String id;
    
    
    @Override
    public String toString()
    {
        String result = "{";
        
        result += "\"id\":\"" + id + "\",";
        
        
        
        
        
        
        result += "\"models\":\"";
        if(!models.isEmpty())
        {
            result += "[";
            int i = 0;
            for(Model model:models)
            {
                
                if(i < models.size() - 1) result += model.toString() + ",";
                else result += model.toString();
                
                i++;
            }
            result += "],";
        }
        
        result += "\"dashboards\":\"";
        if(!dashboards.isEmpty())
        {
            result += "[";
            int i = 0;
            for(Dashboard dashboard:dashboards)
            {
                
                if(i < dashboards.size() - 1) result += dashboard.toString() + ",";
                else result += dashboard.toString();
                
                
                i++;
            }
            result += "],";
        } 
        
        result += "\"lastUpdate\":\"" + lastUpdate + "\"";
        
        result += "}";
        
        return result;
    }
    
    
    

    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
        
        lastUpdate = (new Date()).toString();
    }     
    

    
    public String getLastUpdate()
    {
        return lastUpdate;
    }
    
    public ArrayList<Model> getModels()
    {
        return models;
    }
    
    public ArrayList<Dashboard> getDashboards()
    {
        return dashboards;
    }    
    
    public void addModel(Model model)
    {
        lastUpdate = (new Date()).toString();
        
        this.models.add(model);
    }
    
    public void addAllModels(ArrayList<Model> models)
    {
        lastUpdate = (new Date()).toString();
        
        this.models.addAll(models);
    }
    
    
    public void addDashboard(Dashboard dashboard)
    {
        lastUpdate = (new Date()).toString();
        
        this.dashboards.add(dashboard);
    }
    
    public void addAllDashboards(ArrayList<Dashboard> dashboards)
    {
        lastUpdate = (new Date()).toString();
        
        this.dashboards.addAll(dashboards);
    }    
    
}