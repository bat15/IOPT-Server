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
    String user = "admin";
    Date date = new Date();
    String strDate = date.toString();

%>
<html lang="ru">
    <head>
        <jsp:include page="template/default/chunks/head.jsp" >
            <jsp:param name="pageName" value="<%=pageName%>" />
        </jsp:include>
    </head>
    <body>

    <h3>
        Интернет умных вещей 
    </h3>
    <div id="tabs">
      <ul>
          <li><a href="<%=request.getContextPath()%>/template/default/chunks/ajax/tables/models.jsp">Модели</a></li>
        <li><a href="<%=request.getContextPath()%>/template/default/chunks/ajax/tables/objects.jsp">Объекты</a></li>
        <li><a href="ajax/content2.html">Свойства</a></li>
        <li><a href="ajax/content3-slow.php">Скрипты</a></li>
      </ul>
      <div id="tabs-1">
          

      </div>
      <div id="tabs-2">
               

      </div>       
    </div>

<!--        <script>
        $('#datetimepicker1').datetimepicker({
                  lang:'ru'     ,
                  format:'H:i d.m.Y'
                });
        </script>  -->

    <footer>
        <jsp:include page="template/default/chunks/footer.jsp" >
            <jsp:param name="pageName" value="<%=pageName%>" />
        </jsp:include>
    </footer>

    </body>
</html>