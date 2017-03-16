<%-- 
    Document   : sign-in
    Created on : Mar 11, 2017, 1:54:47 PM
    Author     : Павел
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*" %>
<% 
    String pageName = request.getParameter("pageName");
    
%>


        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=EDGE" />
        <!--<meta name="viewport" content="width=device-width, initial-scale=1">-->
        <title>IOPT - Internet Of Pretty Things</title>

  
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/template/default/css/style.css" media="screen, projection" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/template/datetimepicker/jquery.datetimepicker.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/template/table/css/dataTables.tableTools.min.css">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/template/table/css/jquery.dataTables.css">


