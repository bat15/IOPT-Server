/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.iot.rest.requests;

import bat15.iot.entities.Snapshot;
import bat15.iot.rest.interfaces.PATCH;
import bat15.iot.rest.process.ProcessorAuth;
import bat15.iot.rest.process.ProcessorGUIClient;
import bat15.security.Security;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import javax.ws.rs.DELETE;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Павел
 */
@Path("/")
public class AuthResource {
    
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
    
    /**
     * Creates a new instance of Rest Resource
     */
    public AuthResource() {
    }


    
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response autenticateUser(String jsonBody, @Context HttpHeaders headers,@Context Request rsRequest, @Context HttpServletRequest request)
    {
        String body;
        
        
                
//        body = "{ \"login\": \"admin\", \"password\":\"test_pass\"}";
        
        body = jsonBody;

        String newCookieValue = null;
        
        String ipAddress = request.getRemoteAddr();
        
        if(ipAddress == null) return Response.status(Status.BAD_GATEWAY).build();


        JsonParser parser = new JsonParser(); 
        JsonObject authPair = null;
    
        Cookie sessionCookie = headers.getCookies().get("session");
        
        if(sessionCookie == null){
            
            //HttpHeaders.SET_COOKIE
            //javax.ws.rs.core.Cookie
            try{
                authPair = parser.parse(body).getAsJsonObject();
            }catch(Exception ex){
                System.out.println("Exception body: " + body);
                System.out.println("Exception authPair: " + authPair);
            }

            if(authPair == null) return Response.status(Status.NOT_FOUND).build();
            
            String login = "";
            String password = "";
            
            try{
               
                login = authPair.get("login").getAsString(); 
                password = authPair.get("password").getAsString(); 
            }catch(Exception ex){
                System.out.println("login: " + login);
                System.out.println("password: " + password);
            }
            
            System.out.println("login: " + login);
            System.out.println("password: " + password);
            
            
            if(authProc.authByPassword(login, password)){
                
                newCookieValue = authProc.upsertCookie(request.getSession().getId(), login, ipAddress);

                return Response.ok()
                    .cookie(new NewCookie("session", newCookieValue)).build();
            }
            else return Response.status(Status.FORBIDDEN).build();
        }
        else{
            return Response.status(Status.FORBIDDEN).build();
        }
        
    }
    


    @DELETE
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@Context HttpHeaders headers, @Context HttpServletRequest request)
    {
        
        
        
        String ipAddress = request.getRemoteAddr();
        
        if(ipAddress == null) return Response.status(Status.BAD_GATEWAY).build();
        

        Cookie sessionCookie = headers.getCookies().get("session");
        
        request.getSession().invalidate();
        
        
        if(sessionCookie != null){
            
            
            
        }

    
        return Response.status(Status.FORBIDDEN).build();
    }    
    

    
    @GET
    @Path("/key")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED,MediaType.TEXT_PLAIN, MediaType.TEXT_HTML})
    public Response getAppkeyByCookie(String jsonBody,
            @Context HttpHeaders headers,
            @Context Request request
    ){
        
        
        String idModel = null;

        
        try{
            idModel = headers.getHeaderString("Content-Sidkey");
        }catch(Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }

        if(idModel == null) return Response.status(Response.Status.FORBIDDEN).entity("FAIL\n\r").build();
        
        
        System.out.println("idModel: " + idModel);
        
        String cookieValue  = null;
        
        Map<String, Cookie> cookies = headers.getCookies();
        try{
            cookieValue = cookies.get("session").getValue();
        }catch(Exception ex){}
        
        if(cookieValue == null)
            return Response.status(Response.Status.FORBIDDEN).entity("NO_COOKIE\n\r").build();
        
        

        if(authProc.authByHash(cookieValue, false)) {
            
            
            
            String appKey = null;

            try{
                appKey = authProc.getAppkey(idModel, cookieValue);
            }catch(Exception ex){}
            
            if(appKey == null) return Response.status(Response.Status.NOT_FOUND).entity("NO_APPKEY\n\r").build();
            else return Response.status(Response.Status.OK).entity("{\"appkey\":\"" + appKey + "\"}\n\r").build();
            
        }
         
        
        return Response.status(Response.Status.FORBIDDEN).entity("AUTH_FAIL\n\r").build();
    }
 
    
    @POST
    @Path("/key")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_FORM_URLENCODED,MediaType.TEXT_PLAIN, MediaType.TEXT_HTML})
    public Response generateAppkeyByCookie(String jsonBody,
            @Context HttpHeaders headers,
            @Context Request request
    ){
        String body = "{ \"id_model\": \"1\"}";
        
        body = jsonBody;
        
        JsonParser parser = new JsonParser(); 
        String idModel = null;
        
        
        System.out.println("body: " + body);
        
        
        try{
            idModel = ((JsonObject)parser.parse(body)).get("id_model").getAsString();
        }catch(Exception ex){
            System.out.println("Exception: " + ex.getMessage());
        }

        System.out.println("idModel: " + idModel);
        
        String cookieValue  = null;
        
        Map<String, Cookie> cookies = headers.getCookies();
        try{
            cookieValue = cookies.get("session").getValue();
        }catch(Exception ex){}
        
        if(cookieValue == null)
            return Response.status(Response.Status.FORBIDDEN).entity("NO_COOKIE\n\r").build();
        
        

        if(authProc.authByHash(cookieValue, false)) {
            
            String appKey = null;
            int i = 0;
            while(appKey == null && i < 1000)
            {
                i++;
                try{
                     appKey = (new Random()).nextLong() + Security.getHashFromString((new Date()).getTime());
                }catch(Exception ex){

                }
            }
            
            if(appKey != null) appKey = appKey.replace("-", "");

            
            authProc.generateAppkey(idModel, cookieValue, appKey);
            
            if(appKey == null) Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("AUTH_FAIL\n\r").build();
            else return Response.status(Response.Status.OK).entity("{\"appkey\":\"" + appKey + "\"}\n\r").build();
            
        }
         
        
        return Response.status(Response.Status.FORBIDDEN).entity("AUTH_FAIL\n\r").build();
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

