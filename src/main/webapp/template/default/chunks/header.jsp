<%-- 
    Document   : header
    Created on : Mar 11, 2017, 1:55:10 PM
    Author     : Павел
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*" %>
<% 
    String pageName = request.getParameter("pageName");
%>

<%! 
    String getFormattedDate() 
    { 
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss"); 
        return sdf.format(new Date()); 
    }
%>

<div id="header">
    
    <h1>Добро пожаловать <% if(pageName.equals("main")) out.print(" на главной странице"); %>!</h1>
    <i>Сегодня <%= getFormattedDate() %></i>

	<div id="main_navigation">
            <a class="button" href="<%=request.getContextPath()%>/index.jsp" > Вход </a>
            <a class="button" href="<%=request.getContextPath()%>/main.jsp" > Панель мониторинга </a>
            <a class="button" href="<%=request.getContextPath()%>/models.jsp" > Модели </a>
            <a class="button" href="<%=request.getContextPath()%>/objects.jsp" > Объекты </a>
            <a class="button" href="<%=request.getContextPath()%>/properties.jsp" > Свойства </a>
            <a class="button" href="<%=request.getContextPath()%>/scripts.jsp" > Скрипты </a>
	</div>    
    
    
</div>