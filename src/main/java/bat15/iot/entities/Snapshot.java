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
@XmlRootElement(name="Snapshot")
public class Snapshot {
    
    @XmlElement(name="Models")
    protected ArrayList<Model> models;
    
    @XmlElement(name="Dashboards")
    protected ArrayList<Dashboard> dashboards;

    @XmlElement(name="LastUpdate")
    protected String lastUpdate; 
    
    public Snapshot()
    {
        models = new ArrayList();
        dashboards = new ArrayList();
    }
    
    public void setLastUpdate(String lastUpdate)
    {
        this.lastUpdate = lastUpdate;
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
        this.models.add(model);
    }
    
    public void addAllModels(ArrayList<Model> models)
    {
        this.models.addAll(models);
    }
    
    
    public void addDashboard(Dashboard dashboard)
    {
        this.dashboards.add(dashboard);
    }
    
    public void addAllDashboards(ArrayList<Dashboard> dashboards)
    {
        this.dashboards.addAll(dashboards);
    }    
    
}