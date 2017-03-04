/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bat15.restful.requests;

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

/**
 *
 * @author Павел
 */
@Path("/")
public class ThingPropertyResource {
    
    @Resource(lookup = "iot-server")
    private Properties properties;
    
    @Context
    private UriInfo context;

//    @EJB (beanName="Result")
//    Result proc;

    @EJB (beanName="ThingPropertyProcessor")
    ThingPropertyProcessor proc;    
    
    /**
     * Creates a new instance of RestResource
     */
    public ThingPropertyResource() {
        
        
    }

    /**
     * Retrieves representation of an instance of ru.beeline.hadoop.webtechccboonline.ws.RestResource
     * @param ctn
     * @param fromtime
     * @param totime
     * @return an instance of ru.beeline.hadoop.webtechccboonline.ws.WsObject
     */
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
             
        String config_path = properties.getProperty("config_path"); //"/home/glassfish/glassfish4/glassfish/domains/domain1/config/test_model.json";
        String filePath = config_path + "/test_model.json";
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
    @Path("{path:.*}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getModelRequest(@Context UriInfo uriInfo,@Context HttpHeaders headers, @PathParam("path") String modelQuery) {
    
        //ui.getBaseUri();
        return proc.getModel(modelQuery);
    }
    
    
    


    
//    @GET
//    @Path("{path:.*}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getModelRequest(@PathParam("path") String modelQuery) {
//    
//        return proc.getModel(modelQuery);
//    }
  
  
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
    
    

    /**
     * PUT method for updating or creating an instance of RestResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(JsonObject snapshot) {
        
        
//        JsonArray models = snapshot.getJsonArray("Models");
//        
//        for(JsonValue model: models)
//        {
//            
//            
//            
//        }
//        
//        
//	List<JsonObject> matchingItems = new ArrayList<JsonObject>();
//	for (JsonObject item : candidates.getValuesAs(JsonObject.class)) {
//		JsonArray titles = item.getJsonArray("title");
//
//                
//	}
//        
//        proc.putPropertyValue(data);
    } 
    
    

    
//    //bean injection as parameter
//    @POST
//    public void post(@BeanParam MyBeanParam beanParam, String entity) {
//        final String pathParam = beanParam.getPathParam(); // contains injected path parameter "p"
//    }


    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createDataInJSON(@DefaultValue("green") @FormParam("name") String name) { 

        String result = "Data post: "+name;

        return Response.status(201).entity(result).build(); 
    }    
}

