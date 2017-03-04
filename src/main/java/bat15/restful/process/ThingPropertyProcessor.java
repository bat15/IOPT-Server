/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.restful.process;


import java.io.BufferedWriter;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Павел
 */
@Stateless
@LocalBean
public class ThingPropertyProcessor {
    
    

    private final Object lock = new Object();
    
    @PostConstruct
    @Schedule(second="0", minute="0", hour="*/6")
    public void init() {
        synchronized (lock) {           
            System.out.println("########## WebTechCCBO Bean created.  Creation time: "+new Date());
            // проверяем пропертисы, которые сохранены в glassfish'е
            //AppProperties.checkProperties(properties);
            
            try{
                //jdbcConnection = JDBCConnection.connect();   
            }catch(Exception ex){
                System.out.println("jdbcConnection connect try");
                ex.printStackTrace();
            }            
//            try{
//                hbaseConnection = HbaseConnection.connect(properties);   
//            }catch(Exception ex){ex.printStackTrace();}
        }
    } 
    
    
    @PreDestroy
    public void shutdown()
    {
        try{
            //jdbcConnection.disconnect();
        }catch(Exception ex){
            System.out.println("jdbcConnection disconnect try");
            ex.printStackTrace();
        }        
        
//        try{
//            hbaseConnection.disconnect();
//        }catch(Exception ex){ex.printStackTrace();}
//        
//        System.out.println("########## WebTechCcbo Bean shutted down.  Shutdown at : "+new Date());
    }    
    
    public void putPropertyValue(String data) {
        
        
    }
    
    public String getPropertyValue(int type){
        
        String data = "";
        
        if(type == 0) data = "{test_json: {name: value}}";
        else if(type ==1) data = "{name: value}";
                
                
        return data;
    }
    
    public String getNoModel(){
//        Path file = Paths.get("the-file-name.txt");
//        Files.write(file, lines, Charset.forName("UTF-8"));
        return getModel("");
    }    
    
    public String getModel(String modelQuery){
        
        String testData = "";
        
        if(modelQuery == null) {
            testData = "[{error: null}]";
            return testData;
        }
        else if(modelQuery.isEmpty()) {
            testData = "[{error : no_model_name_defined}]";
            return testData;
        }
        
        ArrayList<String> paths = new ArrayList();
        
        paths.addAll(Arrays.asList(modelQuery.split("/")));
        
        
        switch (paths.size()) {
            case 0://error - no model_name defined
                testData = "[{error : no_model_name_defined}]";
                System.out.println("case 0 size: " + paths.size());
                System.out.println("!!! " + testData);
                
                break;
            case 1://model_name
                System.out.println("case 1 size: " + paths.size());
                try{
                    testData = "[{model: " + paths.get(0) + "}]";
                    System.out.println("!!! " + testData);
                }catch(Exception ex){}
                break;
            case 2://object_name
                System.out.println("case 2 size: " + paths.size());
                
                try{
                    testData = "[{model: " + paths.get(0) + "}, {object:" + paths.get(1) + "}]";
                    System.out.println("!!! " + testData);
                }catch(Exception ex){}
                
                break;
            case 3://property_name
                try{
                    String tmpStr = (new Date()).toString() + (new Random()).nextInt(1000);
                    

                    tmpStr = "random" + (tmpStr.hashCode() + "").replace("-", "");                    
                    testData = "[{model: " + paths.get(0) + "}, {object:" + paths.get(1) + "}, {property: { name:" + paths.get(2)+ ", value:" + tmpStr + "}}]";
                    System.out.println("!!! " + testData);
                }catch(Exception ex){}
                break;
            case 4://script_name
                try{
                    testData = "[{model: " + paths.get(0) + "}, {object:" + paths.get(1) + "}, {property:" + paths.get(2)+ "}, {script: { name:" + paths.get(3) + ", code: js_code}}]";
                    System.out.println("!!! " + testData);
                }catch(Exception ex){}
                
                break;      
            default://error - to much arguments
                testData = "[{error : to_much_arguments}]";
                System.out.println("!!! " + testData);
                break;
        }
            
        String response;
        
                
        return testData;
    }    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

}