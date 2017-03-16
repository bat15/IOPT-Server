<%-- 
    Document   : main
    Created on : Mar 14, 2017, 11:48:08 PM
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
    String pageName = "main"; 
    String user = "iks";
    Date date = new Date();
    String strDate = date.toString();

%>
<html>
    <head>
        <jsp:include page="template/default/chunks/head.jsp" >
            <jsp:param name="pageName" value="<%=pageName%>" />
        </jsp:include>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script>
      $( function() {
        $( "#tabs" ).tabs({
          beforeLoad: function( event, ui ) {
            ui.jqXHR.fail(function() {
              ui.panel.html(
                "Couldn't load this tab. We'll try to fix this as soon as possible. " +
                "If this wouldn't be a demo." );
            });
          }
        });
      } );
    </script>   
    </head>
    <body>
   <jsp:include page="template/default/chunks/header.jsp" >
        <jsp:param name="pageName" value="<%=pageName%>" />
    </jsp:include>
    <h3>
        Интернет умных вещей 
    </h3>
    <div id="tabs">
      <ul>
          <li><a href="<%=request.getContextPath()%>/template/default/chunks/ajax/tables/models.jsp">Модели</a></li>
        <li><a href="<%=request.getContextPath()%>/template/default/chunks/ajax/tables/objects.jsp">Объекты</a></li>
        <li><a href="<%=request.getContextPath()%>/template/default/chunks/ajax/tables/properties.jsp">Свойства</a></li>
        <li><a href="<%=request.getContextPath()%>/template/default/chunks/ajax/tables/scripts.jsp">Скрипты</a></li>
      </ul>
      <div id="tabs-1">
          

      </div>
      <div id="tabs-2">
               

      </div>       
    </div>



    </body>
</html>