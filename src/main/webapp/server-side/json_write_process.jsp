<%-- 
    Document   : json_write
    Created on : Mar 4, 2017, 10:53:42 AM
    Author     : Павел
--%>

<%@page import="java.io.File"%>
<%@page import="java.io.FileWriter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.nio.file.StandardOpenOption" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.io.IOException" %>

<%@ page import="java.nio.charset.Charset" %>
<%@ page import="java.nio.file.Files" %>
<%@ page import="java.nio.file.Path" %>
<%@ page import="java.nio.file.Paths" %>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Arrays" %>


<%
    String testModelJson= new String(request.getParameter("test_model").getBytes("ISO-8859-1"), "UTF-8");
    
    
%>
<html>
<body>

<%
    String fileName = "test_model.json";
    
    testModelJson = testModelJson.trim();
    
    try{
        if(testModelJson.length() > 500000) {
            testModelJson = "Your input limited to 500000 symbols excluded this row, please re-enter your json in smaller way\n" + testModelJson.substring(0,499999);
        }
    }catch(Exception ex){
        out.println("EXCEPTION ");
    }
    List<String> lines = Arrays.asList(testModelJson);
    Path file = Paths.get(fileName);
       
         
    try{
        
        Files.write(file, lines, Charset.forName("UTF-8"),StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
        //out.println("<h1> written json </h1> <br /><br />" +testModelJson);
        out.println("<h1> json written successfuly  </h1> <br /><br />");
    }catch(Exception ex){
        out.println("EXCEPTION " + file.getFileName() + " | fileName: " + fileName);
    }
    
    String redirectURL = request.getRequestURL().toString();
    
    redirectURL = redirectURL.substring(0, redirectURL.length() - "server-side/json_write_process.jsp".length() - 1);
    
    redirectURL = redirectURL + "/tools/json_write.jsp";
//json_write_process.jsp
    
    
    out.println("<br /><br />" + redirectURL);
    
    response.sendRedirect(redirectURL);
%>
</body>
</html>