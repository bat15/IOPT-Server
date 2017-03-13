<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*" %>

<%! 
    String pageName = "main";
%>
<html>
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