/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.iot.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    
    public Snapshot()
    {
        this.id = "1";
        models = new ArrayList();
        dashboards = new ArrayList();
        
        lastUpdate = (new Date()).toString();
    }
    
    
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
    
    

    public String toJsonString()
    {
        String result = "{";
        
        if(id != null && !id.isEmpty()) result += "\"id\":\"" + id + "\",";
        else result += "\"id\":\"null\",";
        
        result += "\"models\":";
        result += "[";
        if(models != null && !models.isEmpty())
        {
            
            int i = 0;
            for(Model model:models)
            {
                
                if(i < models.size() - 1) result += model.toJsonString() + ",";
                else result += model.toJsonString();
                
                i++;
            }
            
        }
        result += "],";
        
        result += "\"dashboards\":";
        result += "[";
        if(!dashboards.isEmpty())
        {
            
            int i = 0;
            for(Dashboard dashboard:dashboards)
            {
                
                if(i < dashboards.size() - 1) result += dashboard.toJsonString() + ",";
                else result += dashboard.toJsonString();
                
                
                i++;
            }
            
        } 
        result += "],";
        
        result += "\"lastUpdate\":\"" + lastUpdate + "\"";
        
        result += "}";
        
        return result;
    }
    
    public static Snapshot parseJsonRootOnly(String snapshotBody)
    {
        JsonParser parser = new JsonParser();
        JsonObject jsonModel = parser.parse(snapshotBody).getAsJsonObject();
        
        
        String id = null;
        
        try{
            id = jsonModel.get("id").getAsString();
        }catch(Exception ex){
            throw new RuntimeException("Parse Exeption: no id fields found");
        }
        
        Snapshot newSnapshot = new Snapshot(id);

        return newSnapshot;
    } 

    public static ArrayList<Model> parseJsonModelAndNested(String snapshotBody)
    {
        JsonParser parser = new JsonParser();
        
        
        ArrayList<Model> models = new ArrayList();
        
        JsonArray jsonModels = parser.parse(snapshotBody).getAsJsonObject().getAsJsonArray("models"); 
        
        JsonObject jsonModel = parser.parse(snapshotBody).getAsJsonObject();
        
        
        String id = null;
        
        try{
            id = jsonModel.get("id").getAsString();
        }catch(Exception ex){
            throw new RuntimeException("Parse Exeption: no id fields found");
        }

        Snapshot newSnapshot = new Snapshot(id);


        //-------Models--------------------------------------------
        int objectsCount = 0;
        
        for(JsonElement model:jsonModels)
        {
            String modelStr = model.getAsJsonObject().toString();
            
            if(modelStr== null) throw new RuntimeException("model: " + modelStr);
            
            Model newModel = Model.parseJsonModelAndNested(modelStr, id);
            //insertObjectInDB(newObject);
            if(newModel != null) models.add(newModel);
            objectsCount++;
        }
            
        System.out.println("objectsCount: " + objectsCount);
        System.out.println("newModel.getObjects().size(): " + newSnapshot.getModels().size());
        
        return models;
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