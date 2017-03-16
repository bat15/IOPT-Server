<%-- 
    Document   : models
    Created on : Mar 15, 2017, 9:17:26 PM
    Author     : Павел
--%>


<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*" %>


<% 
    String tableName = "properties";
    String user = "iks";
    Date date = new Date();
    String strDate = date.toString();

%>

    <jsp:useBean id="ModelRequest" scope="request" class="bat15.iot.client.ModelRestClient" />
    <jsp:setProperty name="ModelRequest" property="tablename" value="<%=tableName%>" />
    <jsp:setProperty name="ModelRequest" property="result" value="<%=user%>" />

      
                <% 
                String tableHead = 
                "<tr>" +
                    "<th>Имя</th>"+
                    "<th>Идентификатор</th>"+
                    "<th>ID Модели</th>"+  
                    "<th>Родительская модель</th>"+    
                    "<th>Часть пути</th>"+
                    "<th>Значение</th>"+
                    "<th>Тип</th>"+
                 "</tr>";
                %>
                
 	<table id="iot-panel">
	    <thead>
               <% out.println(tableHead); %>
	    </thead>
	    <tbody>
        
                <jsp:getProperty name="ModelRequest" property="result" />

	    </tbody>
	    <tfoot>
               <% out.println(tableHead); %>
	    </tfoot>
  	</table>

