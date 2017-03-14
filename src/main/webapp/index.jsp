<%-- 
    Document   : main
    Created on : Mar 14, 2017, 11:48:08 PM
    Author     : Павел
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.*, java.text.*" %>

<%! 
    String pageName = "index";
%>
<html lang="ru">
    <head>
        <jsp:include page="template/default/chunks/head.jsp" >
            <jsp:param name="pageName" value="<%=pageName%>" />
        </jsp:include>
    </head>
  <body>
    
    
    <jsp:include page="template/default/chunks/header.jsp" >
        <jsp:param name="pageName" value="<%=pageName%>" />
    </jsp:include>
      <br />

    <jsp:include page="template/default/chunks/sign-in-form.jsp" >
        <jsp:param name="pageName" value="<%=pageName%>" />
    </jsp:include>      

    <br /><br /><br />
    <jsp:include page="template/default/chunks/sign-up-form.jsp" >
        <jsp:param name="pageName" value="<%=pageName%>" />
    </jsp:include>


      

  </body>
</html>