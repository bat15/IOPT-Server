/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.iot.rest.resources;

import bat15.iot.entities.Model;
import bat15.iot.entities.Snapshot;
import bat15.iot.rest.interfaces.PATCH;
import bat15.iot.rest.process.ProcessorAuth;
import bat15.iot.rest.process.ProcessorGUIClient;
import bat15.iot.rest.process.ProcessorSaveModel;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

//import javax.ws.rs.core.HttpResponse;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Request;


import java.io.File;
import java.io.FileWriter;


import java.nio.file.StandardOpenOption;
import java.io.PrintWriter;
import java.io.IOException;

import java.nio.charset.Charset;
import java.nio.file.Files;

import java.nio.file.Paths;

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Павел
 */
@Path("/models")
public class ModelResource {
    
    @Resource(lookup = "IOPT-Server")
    private Properties properties;
    

    
    @Context
    private UriInfo context;

//    @EJB (beanName="Result")
//    Result proc;

    @EJB (beanName="ProcessorGUIClient")
    ProcessorGUIClient GUIProc;  
    
    @EJB (beanName="ProcessorAuth")
    ProcessorAuth authProc;   
    
    @EJB (beanName="ProcessorSaveModel")
    ProcessorSaveModel modelProc;    
    /**
     * Creates a new instance of Rest Resource
     */
    public ModelResource() {
    }
    
    
    

    String getTestFile = "test_model.json";
    String postTestFile = "post_test.json";
    String putTestFile = "put_test.json";

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getTestRequest(@QueryParam("json") String jsonType) {
////        return new WsObject("examp.ru","/usrl/fhsj", (new Date()).getTime());
//        
//
//        System.out.println("json: "+jsonType);
//        
//        System.out.println("test");
//        
//        int type = 0;
//        
//        if(jsonType!=null) 
//            if(jsonType.equals("short")) type = 1;
//            else if(jsonType.equals("full")) type = 0;
//        
//        return proc.getPropertyValue(type);
//    }
    
    
    
    //getServletContext().getRealPath("/") +"configs/test_model.json";
    
//    @GET
//    @Path("{path:}")
//    @Produces("text/html")
//    public Response redirectToIndex(@Context HttpServletRequest request, @Context HttpServletResponse response)
//            throws IOException {
//        String myIndexPage = "/index.html";
//        String contextPath = request.getContextPath();
//        response.sendRedirect(contextPath + myIndexPage);
//        return Response.status(Status.ACCEPTED).build();
//    }
//        
    @GET
    @Path("/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModelByPath(
            @PathParam("path") String path, 
            @QueryParam("min") String minParam,
            @Context HttpServletRequest requestContext,
            @Context HttpHeaders headers) 
    {
        
        
        String hash = null;
        String userId = null;
        String name = null;
        
        
        
        
        try{
            hash = headers.getCookies().get("session").getValue();//hash from cookie

            if(hash==null || hash.isEmpty()){
                hash = headers.getHeaderString("Content-Sidkey");//appkey
                userId = authProc.getUserIdByHash(hash, true);
            }
            else 
                userId = authProc.getUserIdByHash(hash, false); //cookie
            
        }catch(Exception ex){}
        
        if(userId == null) return Response.status(Status.FORBIDDEN).build();
         
        boolean isMinimal = false;
        
        if(minParam!= null) {
            isMinimal = true;
            System.out.println("GET minimal json: ");
        }
        else System.out.println("GET Simple json");     
        
        String procResultJson = GUIProc.getModel(path, userId, null, isMinimal);
        JsonParser parser = new JsonParser();
        
        if(procResultJson == null || procResultJson.isEmpty()) return Response.status(Status.NOT_FOUND).build();
        
//        try{
//            name = procResultJson.substring(procResultJson.indexOf(":")+1, procResultJson.indexOf(",")).trim().replace("\"", "");
//        }catch(Exception ex){}
//        
//        System.out.println("name: " + name);
        

        
        
        if(procResultJson.contains(":")) return Response.status(Status.FOUND).entity(procResultJson).build();
        else return Response.status(Status.NOT_FOUND).build();
    }
    
    
    @PUT
    @Path("/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response putModelByPath(
            String body,
            @PathParam("path") String path, 
            @Context HttpServletRequest requestContext,
            @Context HttpHeaders headers) 
    {
        
        
        String hash = headers.getCookies().get("session").getValue();//hash from cookie
        
        String userId = null;
        
        if(hash==null || hash.isEmpty()){
            hash = headers.getHeaderString("Content-Sidkey");//appkey
            userId = authProc.getUserIdByHash(hash, true);
        }
        else 
            userId = authProc.getUserIdByHash(hash, false); //cookie
        
        if(userId == null) return Response.status(Status.FORBIDDEN).build();
         
        JsonParser parser = new JsonParser();
//        String newValue = parser.parse(body).getAsJsonObject().get("value").getAsString();

        String newValue = body.substring(body.indexOf(":")+1); //delete all, what goes befor first comma
        
        
        
        newValue=newValue.substring(0, newValue.lastIndexOf("}")).trim().replace("\"", "");
       
            
            
        System.out.println("PUT newValue: " + newValue);
        
        String resultStatus = GUIProc.getModel(path, userId, newValue, false);
        

        
        
//        if(resultStatus != null && resultStatus.equals("OK")) return Response.status(Status.FOUND).build();
//        else return Response.status(Status.NOT_FOUND).build();
        
        return Response.status(Status.FOUND).build();
    }
    
    
    @POST
    @Path("/sync")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED,MediaType.TEXT_PLAIN})
//    @Produces(MediaType.APPLICATION_JSON)
//    public String postTestModelJson(@Context UriInfo uriInfo, @FormParam("put_test_json") String jsonInput) {
    public Response syncModels(
            String body, 
            @Context UriInfo uriInfo, 
            @FormParam("put_test_json") String formInput, 
            @QueryParam("put_test_json") String queryInput, 
            @Context HttpServletRequest requestContext,
            @Context HttpHeaders headers) 
    {
        
        String hash = headers.getCookies().get("session").getValue();//hash from cookie
        
        String userId = null;
        
        if(hash==null || hash.isEmpty()){
            hash = headers.getHeaderString("Content-Sidkey");//appkey
            userId = authProc.getUserIdByHash(hash, true);
        }
        else 
            userId = authProc.getUserIdByHash(hash, false); //cookie
        
        if(userId == null) Response.status(Status.FORBIDDEN).build();
        
        
        ArrayList<Model> models = modelProc.delsertModelsFromShanpshot(body, userId);
        
        System.out.println("models.size(): " + models.size());
        
        return Response.noContent().build();
    }    
    
    
    
 
    
   
    
    public String readJsonStringFromPath(String path, String fileName)
    {
        
        
        String config_path = path;
        

        
        String filePath = config_path + "/" + fileName;
        StringBuilder strBuilder = new StringBuilder();
        

        // 
	try {
		File fileDir = new File(filePath);

		BufferedReader in = new BufferedReader(
		   new InputStreamReader(
                      new FileInputStream(fileDir), "UTF8"));

		String str;

		while ((str = in.readLine()) != null) {
		    //System.out.println(str);
                    strBuilder.append(str);
		}

                in.close();
	    }
	    catch (UnsupportedEncodingException e)
	    {
                System.out.println(e.getMessage());
	    }
	    catch (IOException e)
	    {
                System.out.println(e.getMessage());
	    }
	    catch (Exception e)
	    {
                System.out.println(e.getMessage());
	    }
        
        return strBuilder.toString();    
    }
    
    
    public String fileWrite(ArrayList<String> inputStrings, String inputFilePath, String inputFileName, boolean isOverwrite)
    {
        //String[] array = inputString.split("\n\r");
        
//        List<String> lines = Arrays.asList(array);
        
        
        List<String> lines = inputStrings;
        
        
        java.nio.file.Path file = Paths.get(inputFilePath + "/" + inputFileName);


        try{
            
            if(isOverwrite) Files.write(file, lines, Charset.forName("UTF-8"),StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
            else Files.write(file, lines, Charset.forName("UTF-8"),StandardOpenOption.CREATE,StandardOpenOption.APPEND);
            
            
            //out.println("<h1> written json </h1> <br /><br />" +testModelJson);
            System.out.println("json written successfuly - input: " + inputStrings);
            System.out.println("inputFilePath: " + inputFilePath);
            System.out.println("inputFileName: " + inputFileName);
            
        }catch(Exception ex){
            System.out.println("EXCEPTION " + file.getFileName() + " | fileName: " + inputFileName);
        }        
        
        return "write_successful";
    }
    
    public ArrayList<String> checkIps(String inputIp)
    {
        
        HashMap<String, Integer> ipCounters = new HashMap();

        ArrayList<String> newLines = new ArrayList();

        String[] strArrays = readJsonStringFromPath(properties.getProperty("config_path") + "/iot", "ips_get_put_file.txt").split("\n");

        Integer counter = -1;

        boolean foundFlag = false;

        for(String strLine:strArrays)
        {
            if(strLine == null) {
                System.out.println("strLine: " + strLine);
                continue;
            }
            
            if(strLine.isEmpty() || strLine.indexOf(":") == -1)
            {
                System.out.println("strLine: " + strLine);
                continue;
            }
            
            
            String line = strLine.trim().replace(" ", "");
            String counterStr = "";

            String lineIp = line.substring(0, line.indexOf(":"));
            
            

            try{
                counterStr = line.substring(line.indexOf(":") + 1, line.indexOf(";"));
                counter = Integer.parseInt(counterStr);
            }catch(Exception ex){
                System.out.println("Exception counterStr: " + counterStr);
            }

            System.out.println("counterStr: " + counterStr);
            System.out.println("lineIp: " + lineIp);

            if(lineIp.equals(inputIp)) {
                foundFlag = true;
                ipCounters.put(lineIp, counter + 1);
            }
            else ipCounters.put(lineIp, counter);

            //String newLine = yourIP + ":" + counter + "|;\n\r";


            //newLines.add(newLine);
        }

        if(!foundFlag) ipCounters.put(inputIp, 1);

        //yourIP = "IP: " + yourIP + "\n\r";


        Set<String> keys = ipCounters.keySet();

        for(String key:keys)
        {
            newLines.add(key + ":" + ipCounters.get(key) + ";\n");
        }
        
        return newLines;
    }
    
    
}

