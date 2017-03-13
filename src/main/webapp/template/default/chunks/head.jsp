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
    <title><%=pageName.trim()=="main"?"Главная страница":pageName+" page"%></title>

    

