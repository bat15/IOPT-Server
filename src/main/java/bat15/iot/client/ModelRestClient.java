/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.iot.client;

import bat15.iot.entities.Model;
import bat15.iot.entities.Property;
import bat15.iot.entities.Script;
import bat15.iot.entities.Snapshot;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:SyncResource [/]<br>
 * USAGE:
 * <pre>
        ModelRestClient client = new ModelRestClient();
        Object response = client.XXX(...);
        // do whatever with response
        client.close();
 </pre>
 *
 * @author Павел
 */
public class ModelRestClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://193.32.20.242:8080/IOPT-Server/service";

    
    String tableName;
    String modelId, objectId, propertyId, scriptId;
    
    private String result;
    
    public ModelRestClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI);
    }
    
    
    String modelsHtmlTable, objectsHtmlTable, propertiesHtmlTable, scriptsHtmlTable;

    
    
    public void setResult(String user)
    {
        
        WebTarget resource = webTarget;
        if (user != null) {
            resource = resource.queryParam("user", user);
        }
        resource = resource.path("snapshot");

        Invocation.Builder invocationBuilder =  resource.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();


        String snapshotJson = response.readEntity(String.class);
        
        List<Model> models = Snapshot.parseJsonModelAndNested(snapshotJson);

        System.out.println("Response OK: " + snapshotJson);
        System.out.println(response.getStatus());
        
        
        
        String restStr = "";
        
        try{
            for(Model model:models)
            {
                restStr+= "[";
                restStr+= model.toJsonString();
                restStr+= "[";
            }
        }catch(Exception ex){
            System.out.println("!!!!!!Exception: "+ ex.getMessage());
            ex.printStackTrace();
        } 
        
        System.out.println("!!!!!!Client: "+ restStr);
//        System.out.println(Arrays.toString( listOfWsObjects.toArray(new WsObject[listOfWsObjects.size()]) ));

//        wsoresult = restWsObject;


        result = snapshotToHttpTable(models, tableName);
    }
    
    
    
    public String getResult()
    {
        return result;
    }
    
    
    private String snapshotToHttpTable(List<Model> models, String tableName){
        
        modelsHtmlTable = "";
        objectsHtmlTable = "";
        propertiesHtmlTable = "";
        scriptsHtmlTable = "";
        prepareTablesFromModels(models);
        
        
        switch(tableName)
        {
            case "models":
                
                if(modelsHtmlTable!=null) {
                    
                    System.out.println("modelsHtmlTable: "+ modelsHtmlTable);
                    return modelsHtmlTable;
                }
                else return "NULL_RESPONSE";
            case "objects":
                
                if(objectsHtmlTable!=null){
                    System.out.println("objectsHtmlTable: "+ objectsHtmlTable);
                    return objectsHtmlTable;
                }
                else return "NULL_RESPONSE";
            case "properties":
                
                if(propertiesHtmlTable!=null){
                    System.out.println("objectsHtmlTable: "+ propertiesHtmlTable);
                    return propertiesHtmlTable;
                }
                else return "NULL_RESPONSE";
            case "scripts":
                
                if(scriptsHtmlTable!=null){
                    System.out.println("objectsHtmlTable: "+ scriptsHtmlTable);
                    return scriptsHtmlTable;
                }
                else return "NULL_RESPONSE";
            default: 
            {
                System.out.println("NULL_RESPONSE");
                return "NULL_RESPONSE";
            }
        }
    }
    
    
    
    private void prepareTablesFromModels(List<Model> models)
    {
        StringBuilder modelsBuilder = new StringBuilder();
        
        HashMap<String, String> modelFields = new HashMap();
        
        System.out.println("!!!prepareTablesFromModels size: " + models.size());
        for (Model model:models){
            
            try {
                
                
                modelsBuilder.append("<tr>\n");
                
                modelsBuilder.append("<td>").append(model.getId()).append("</td>\n");
                modelsBuilder.append("<td>").append(model.getName()).append("</td>\n");
                modelsBuilder.append("<td>").append(model.getPathUnit()).append("</td>\n");

                modelsBuilder.append("</tr>\n");
                
            } catch (Exception e){
                System.out.println("!!!!!Exception "+ e.getMessage() +"  = ");
            }
            
            
            modelFields.put("parentId", model.getId());
            modelFields.put("parentName", model.getName()) ;
            
            prepareObjects(model.getObjects(), modelFields);
            
        }
//        String modelsHtmlTable, objectsHtmlTable, propertiesHtmlTable, scriptsHtmlTable;
        modelsHtmlTable += modelsBuilder.toString();
        
    }
    
    private void prepareObjects(List<bat15.iot.entities.Object> objects, HashMap<String,String> parentFields)
    {

        StringBuilder objectsBuilder = new StringBuilder();
        HashMap<String, String> objectFields = new HashMap();
        
        StringBuilder propertiesBuilder = new StringBuilder();
        StringBuilder scriptsBuilder = new StringBuilder();
        System.out.println("!!!prepareObjects objects size: " + objects.size());
        for (bat15.iot.entities.Object object:objects){

            try {
                
                objectsBuilder.append("<tr>\n");
                
                objectsBuilder.append("<td>").append(object.getId()).append("</td>\n");
                objectsBuilder.append("<td>").append(object.getName()).append("</td>\n");
                objectsBuilder.append("<td>").append(object.getModelId()).append("</td>\n");
                objectsBuilder.append("<td>").append(parentFields.get("parentName")).append("</td>\n");
                objectsBuilder.append("<td>").append(object.getPathUnit()).append("</td>\n");

                objectsBuilder.append("</tr>\n");
                
            } catch (Exception e){
                System.out.println("!!!!!Exception "+ e.getMessage() +"  = ");
            }
            
            objectFields.put("parentId", object.getId());
            objectFields.put("parentName", object.getName()) ;
            
            prepareProperties(object.getProperties(), objectFields);
        }
        objectsHtmlTable += objectsBuilder.toString();
        
    }
    
    private void prepareProperties(List<Property> properties, HashMap<String,String> parentFields)
    {

        StringBuilder propertiesBuilder = new StringBuilder();
        HashMap<String, String> objectFields = new HashMap();
        

        System.out.println("!!!prepareObjects properties size: " + properties.size());
        for (Property property:properties){

            try {
                
                String type = null;
                
                switch(property.getType())
                {
                    case "3":
                        type = "Boolean";
                        break;
                    case "9":
                        type = "Integer";
                        break;
                    case "14":
                        type = "Double";
                        break;
                    case "18":
                        type = "String";
                        break;
                    default:
                        type = "undefined";
                }
                
                propertiesBuilder.append("<tr>\n");
                
                propertiesBuilder.append("<td>").append(property.getId()).append("</td>\n");
                propertiesBuilder.append("<td>").append(property.getName()).append("</td>\n");
                propertiesBuilder.append("<td>").append(property.getObjectId()).append("</td>\n");
                propertiesBuilder.append("<td>").append(parentFields.get("parentName")).append("</td>\n");
                propertiesBuilder.append("<td>").append(property.getPathUnit()).append("</td>\n");
                propertiesBuilder.append("<td>").append(property.getValue()).append("</td>\n");
                propertiesBuilder.append("<td>").append(type).append("</td>\n");

                propertiesBuilder.append("</tr>\n");
                
            } catch (Exception e){
                System.out.println("!!!!!Exception "+ e.getMessage() +"  = ");
            }
            
            objectFields.put("parentId", property.getId());
            objectFields.put("parentName", property.getName()) ;
            
            prepareScripts(property.getScripts(), objectFields);
        }
        propertiesHtmlTable += propertiesBuilder.toString();
        
    }
    
    
    private void prepareScripts(List<Script> scripts, HashMap<String,String> parentFields)
    {

        StringBuilder scriptsBuilder = new StringBuilder();
        HashMap<String, String> objectFields = new HashMap();
        
        
        
        System.out.println("!!!prepareObjects scripts size: " + scripts.size());
        for (Script script:scripts){

            try {
                
                scriptsBuilder.append("<tr>\n");
                
                scriptsBuilder.append("<td>").append(script.getId()).append("</td>\n");
                scriptsBuilder.append("<td>").append(script.getName()).append("</td>\n");
                scriptsBuilder.append("<td>").append(script.getPropertyId()).append("</td>\n");
                scriptsBuilder.append("<td>").append(parentFields.get("parentName")).append("</td>\n");
                scriptsBuilder.append("<td>").append(script.getPathUnit()).append("</td>\n");
                scriptsBuilder.append("<td>").append(script.getValue()).append("</td>\n");

                scriptsBuilder.append("</tr>\n");
                
            } catch (Exception e){
                System.out.println("!!!!!Exception "+ e.getMessage() +"  = ");
            }
            
            objectFields.put("parentId", script.getId());
            objectFields.put("parentName", script.getName()) ;
            
        }
        scriptsHtmlTable += scriptsBuilder.toString();
        
    }
    
    
    

//    public <T> T loadModels(Class<T> responseType, String user) throws ClientErrorException {
//        WebTarget resource = webTarget;
//        if (user != null) {
//            resource = resource.queryParam("user", user);
//        }
//        resource = resource.path("snapshot");
//        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
//    }

    public Response saveModels(Object requestEntity) throws ClientErrorException {
        return webTarget.path("snapshot").request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Response.class);
    }

    public void close() {
        client.close();
    }
    
    
    
    public String getTablename() {
        return tableName;
    }

    public void setTablename(String tableName) {
        this.tableName = tableName;
        System.out.println("!!!! setting tableName = " + tableName);
    }


    
//    public void setModelid(String id)
//    {
//        modelId = id;
//    }
//
//    public String getModelid() {
//        return modelId;
//    }
//    
//    public void setObjectid(String id)
//    {
//        objectId = id;
//    }
//    
//    public String getObjectid() {
//        return objectId;
//    }
//    
//    public void setPropertyid(String id)
//    {
//        propertyId = id;
//    }
//    
//    public String getPropertyid() {
//        return propertyId;
//    }
//    
//    
//    public void setScriptid(String id)
//    {
//        scriptId = id;
//    }
//    
//    public String getScriptid() {
//        return scriptId;
//    }
//
//    public String getUserName() {
//        return tableName;
//    }
//
//    public void setUserName(String tableName) {
//        this.tableName = tableName;
//        System.out.println("!!!! setting tableName = " + tableName);
//    }
}
