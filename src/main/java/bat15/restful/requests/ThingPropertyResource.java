/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.restful.requests;

import bat15.iot.entities.Snapshot;
import bat15.restful.interfaces.PATCH;
import bat15.restful.process.ThingPropertyProcessor;
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import javax.ws.rs.core.Response.Status;
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

/**
 *
 * @author Павел
 */
@Path("/")
public class ThingPropertyResource {
    
    @Resource(lookup = "IOPT-Server")
    private Properties properties;
    
    @Context
    private UriInfo context;

//    @EJB (beanName="Result")
//    Result proc;

    @EJB (beanName="ThingPropertyProcessor")
    ThingPropertyProcessor proc;    
    
    /**
     * Creates a new instance of Rest Resource
     */
    public ThingPropertyResource() {
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
    @Path("/sync/{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getModelRequest(@PathParam("path") String modelQuery) {
    
        return proc.getModel(modelQuery);
    }
  
  
//    
//    @GET
//    @Path("model_name/{uuid}")
//    public String retrieveSomething(@PathParam("uuid") String uuid) {
//        if(uuid == null || uuid.trim().length() == 0) {
//            //return Response.serverError().entity("UUID cannot be blank").build();
//            return "no such model or property";
//        }
//        
//        return uuid;
//        //return Response.ok(uuid, MediaType.APPLICATION_JSON).build();
//    }   

    
//    //bean injection as parameter
//    @POST
//    public void post(@BeanParam MyBeanParam beanParam, String entity) {
//        final String pathParam = beanParam.getPathParam(); // contains injected path parameter "p"
//    }
    
    

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getNoModel(@QueryParam("json") String jsonType) {
//        return new WsObject("examp.ru","/usrl/fhsj", (new Date()).getTime());
                
        return proc.getNoModel();
    }

    @GET
    @Path("/get_test")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTestModelJson(@Context UriInfo uriInfo, @QueryParam("json") String jsonType) {
//        return new WsObject("examp.ru","/usrl/fhsj", (new Date()).getTime());
             
        return readJsonStringFromPath(properties.getProperty("config_path"), getTestFile);
    }

    @GET
    @Path("/get_post_file")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPostFile(@Context UriInfo uriInfo, @QueryParam("json") String jsonType) {
//        return new WsObject("examp.ru","/usrl/fhsj", (new Date()).getTime());
             
        return readJsonStringFromPath(properties.getProperty("config_path") + "/iot", postTestFile);
    }  
    
    @GET
    @Path("/get_put_file")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPutFile(
            @PathParam("path") String path, @Context UriInfo uriInfo, @QueryParam("pretty") String prettyQuery,
            @Context HttpServletRequest requestContext,
            @QueryParam("min") String minQueryParam) 
    {
//        return new WsObject("examp.ru","/usrl/fhsj", (new Date()).getTime());
        String yourIP = requestContext.getRemoteAddr();

        String filePath = properties.getProperty("config_path");
        filePath += "/iot";         
        
        if(yourIP.equals("111.0.0.1")) yourIP = "VIP INVISIBLE IP ";
        else {
            
            ArrayList<String> ipLines = null;
                    
            ipLines = checkIps(yourIP);
            
            if(ipLines != null) fileWrite(ipLines, filePath, "ips_get_put_file.txt", true);
            
        }
        
        
        
        boolean pretty = false;
        
        String readFromFileStr = readJsonStringFromPath(properties.getProperty("config_path") + "/iot", putTestFile);
        
        
//        //query
//        String input = "";
//        
//        input = prettyQuery;
//        
//        
//        
//        String pathTail = "";
//        
//        try{
//            if(input.lastIndexOf("?") != -1) pathTail = input.substring(input.lastIndexOf("?"), input.length());
//            
//            if(pathTail.equals("pretty")) {
//                pretty = true;
//                System.out.println("true pathTail: " + pathTail);
//            }
//            else System.out.println("false pathTail: " + pathTail);
//            
//            System.out.println("OK input: " + input);
//            
//        }catch(Exception ex){
//            System.out.println("Exception input: " + input);
//        }

        if(prettyQuery != null) {
            pretty = true;
            System.out.println("true pretty: " + prettyQuery);
        }
        else System.out.println("false pretty: " + prettyQuery);
        
        if(minQueryParam!= null)
        {
            String minJson = "";
            
            
            String tmpRead = readFromFileStr;
            
            
            minJson = tmpRead.substring(tmpRead.indexOf("{"),tmpRead.length()).replace(" ", "");
                    
            System.out.println("minJson: " + minJson);
            return minJson;
        }
        else if(pretty== true) {
            
            String prettyJson = "";
            
            
//            String[] strArray = readFromFileStr.split("\n\r");
//
//            for(String line: strArray)
//            {
//                prettyJson += line;
//            }
            
            prettyJson = readFromFileStr.replace("{", "\n\r{\n\r");
            prettyJson = prettyJson.replace("}", "\n\r}");
            
            
            System.out.println("pretty: " + pretty);
            
            return prettyJson;
        }
        else {
            
            System.out.println("pretty: " + pretty);
            return readFromFileStr;
        }
        
        
    }
    
    @GET
//    @Path("{path:.*}")
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public String getModelRequest(@Context UriInfo uriInfo,
            @Context HttpHeaders headers, 
            @QueryParam("form") String form,
            @PathParam("path") String modelQuery) {
    
        //ui.getBaseUri();
        return proc.getModel(modelQuery);
    }


    /**
     * PUT method for updating or creating an instance of RestResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(JsonObject snapshotJson) {
        
        JsonArray models = (JsonArray) snapshotJson.getJsonObject("Models");
        
        models.getJsonObject(0);
        
        Snapshot snapshot = new Snapshot();
        
        for(JsonValue model: models)
        {
            JsonObject modelToObject = (JsonObject)model;
            
        }
    } 
    
    
    
    @PATCH
    @Path("/data/{keyspace}")
    @Produces({ "application/json" })
    public String putProperty()
    {
        
        return "";
    }
    

    
    @PUT
    @Path("/put_test")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED,MediaType.TEXT_PLAIN})
    @Produces(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public String putTestModelJson(@Context UriInfo uriInfo, @FormParam("put_test_json") String jsonInput) {
    public Response putTestModelJson(String body, @Context UriInfo uriInfo, @QueryParam("put_test_json") String queryInput, @Context HttpServletRequest requestContext) {
//        return new WsObject("examp.ru","/usrl/fhsj", (new Date()).getTime());
            
        String yourIP = requestContext.getRemoteAddr();
        
        String filePath = properties.getProperty("config_path");
    
        filePath += "/iot";        
        
        
        if(yourIP.equals("111.0.0.1")) yourIP = "VIP INVISIBLE IP ";
        else {
            
            ArrayList<String> ipLines = null;
                    
            ipLines = checkIps(yourIP);
            
            if(ipLines != null) fileWrite(ipLines, filePath, "ips_put_test.txt", true);
        }
        
        String jsonInput = "";
        
        
        if(body != null) jsonInput = body.trim();
        else jsonInput = "FAIL";




//        try{
//            if(jsonInput.length() > 500000) {
//                jsonInput = "Your input limited to 500000 symbols excluded this row, please re-enter your json in smaller way\n" + jsonInput.substring(0,499999);
//            }
//        }catch(Exception ex){
//            System.out.println("EXCEPTION ");
//        }

        ArrayList<String> writeLines = new ArrayList();
        
        
        if(jsonInput.equals("FAIL"))
        {
            writeLines.add("[FAIL POST] Query parameter of Form Parameter name is 'put_test_json'; Stubar loshara!");
            
//            fileWrite(
//                    "[FAIL POST] Query parameter of Form Parameter name is 'put_test_json'; Stubar loshara!" + body, 
//                    filePath, putTestFile, true
//            );
        }
        else writeLines.add(yourIP + "\n\r" + jsonInput);
        
        fileWrite(writeLines, filePath, putTestFile,true);
        
        
        return Response.status(200).entity("IOPT-a works!\n").build(); 
//        return readJsonStringFromPath(filePath,fileName);
    }    

    
    @POST
    @Path("/post_test")
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED,MediaType.TEXT_PLAIN})
//    @Produces(MediaType.APPLICATION_JSON)
//    public String postTestModelJson(@Context UriInfo uriInfo, @FormParam("put_test_json") String jsonInput) {
    public Response postTestModelJson(
            String body, @Context UriInfo uriInfo, 
            @FormParam("put_test_json") String formInput, 
            @QueryParam("put_test_json") String queryInput, 
            @Context HttpServletRequest requestContext) 
    {
//        return new WsObject("examp.ru","/usrl/fhsj", (new Date()).getTime());
            

        String yourIP = requestContext.getRemoteAddr();
        
        String filePath = properties.getProperty("config_path");
        filePath += "/iot";     
        
        
        if(yourIP.equals("111.0.0.1")) yourIP = "VIP INVISIBLE IP ";
        else {
            
            ArrayList<String> ipLines = null;
            
            ArrayList<String> curIpLines = new  ArrayList();
            curIpLines.add(yourIP);
            
            ipLines = checkIps(yourIP);
            
            if(ipLines != null) fileWrite(ipLines, filePath, "ips_put_test.txt", true);
            
            fileWrite(curIpLines, filePath, "ips_put_test_all.txt", true);
        }

        String jsonInput = "";
        
        if(body != null) jsonInput = body.trim();
        else if(formInput != null) jsonInput = formInput.trim();
        else jsonInput = "FAIL";
        

         

//        try{
//            if(jsonInput.length() > 500000) {
//                jsonInput = "Your input limited to 500000 symbols excluded this row, please re-enter your json in smaller way\n" + jsonInput.substring(0,499999);
//            }
//        }catch(Exception ex){
//            System.out.println("EXCEPTION ");
//        }


        
        ArrayList<String> writeLines = new ArrayList();
        
        if(jsonInput.equals("FAIL")) writeLines.add("[FAIL POST] Query parameter of Form Parameter name is 'put_test_json'; Stubar loshara!");
        else writeLines.add(jsonInput);
        
//        if(jsonInput.equals("FAIL")) ;
//        else fileWrite(jsonInput, filePath, postTestFile,true);
        
        fileWrite(writeLines, filePath, postTestFile,true);
        
        return Response.status(200).entity("IOPT-a works!\n").build(); 
//        return readJsonStringFromPath(filePath,fileName);
    }
    

    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDataInJSON(@DefaultValue("green") @FormParam("name") String name) { 

        String result = "Data post: "+name;

        return Response.status(201).entity(result).build(); 
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

