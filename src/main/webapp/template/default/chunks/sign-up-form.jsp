<%-- 
    Document   : sign-up-form
    Created on : Mar 11, 2017, 2:22:21 PM
    Author     : Павел
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*" %>
<% 
    String pageName = request.getParameter("pageName");
%>

<div id="sign-up-wrapper" name="sign-up-wrapper" class="sign-up-wrapper">
    <label for="sign-up-form" > Регистрация </label>
    <form method="post" name="sign-up" id="sign-up-form" class="sign-up-form" action="sign-up.jsp">


        <input name="user" type="text" size="30"><br />
        <input name="password" type="password" size="30"><br />
        <input name="email" type="email" size="30"><br />
        <input name="reg" type="button" size="30" onclick="alert('Регистрация временно не доступна, обратитесь к администратору!')" value="Регистрация">
        
        
    </form>    
</div>
