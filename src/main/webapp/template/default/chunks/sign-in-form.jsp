<%-- 
    Document   : sign-in-form
    Created on : Mar 11, 2017, 2:19:17 PM
    Author     : Павел
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*" %>
<% 
    String pageName = request.getParameter("pageName");
%>

<div id="sigh-in-wrapper" name="sigh-in-wrapper" class="sigh-in-wrapper">
    <label for="auth-form" > Вход </label>
    <form name="auth" method="post" id="auth-form" class="auth-form" action="sign-in.jsp">

        <input name="user" type="text" size="30"><br />
        <input name="password" type="password" size="30"><br />
        <input name="login" type="submit" size="30" value="Войти">


    </form>
</div>
