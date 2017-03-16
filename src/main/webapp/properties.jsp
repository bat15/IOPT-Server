<%-- 
    Document   : properties
    Created on : Mar 16, 2017, 5:19:13 AM
    Author     : Павел
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.*, java.text.*" %>


<%@ page import="bat15.iot.client.ModelRestClient" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Map.Entry" %>
<%@ page import="java.util.NavigableMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>



<%--<jsp:setProperty name="ModelRequest" property="user" value="admin" />--%>




<%!
    String pageName = "properties"; 
    String user = "iks";
    Date date = new Date();
    String strDate = date.toString();

%>
<html lang="ru">
    <head>
        <jsp:include page="template/default/chunks/head.jsp" >
            <jsp:param name="pageName" value="<%=pageName%>" />
        </jsp:include>
        <jsp:include page="template/default/chunks/head_scripts.jsp" />
    </head>
    <body>
   <jsp:include page="template/default/chunks/header.jsp" >
        <jsp:param name="pageName" value="<%=pageName%>" />
    </jsp:include>
    <h3>
        Интернет умных вещей 
    </h3>

  
    <% 


    String tableName = "properties";
    %>


     
        
    <jsp:include page="template/default/chunks/table.jsp" >
        <jsp:param name="pageName" value="<%=pageName%>" />
        <jsp:param name="tableName" value="<%=tableName%>" />
        <jsp:param name="user" value="<%=user%>" />
    </jsp:include>              


    <div id="footer">
        <jsp:include page="template/default/chunks/footer.jsp" >
            <jsp:param name="pageName" value="<%=pageName%>" />
        </jsp:include>
    </div>

    </body>
</html>