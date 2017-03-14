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


<meta http-equiv="Content-Type" content="text/html">
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>jQuery UI Tabs - Content via Ajax</title>

        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
        <link rel="stylesheet" href="/resources/demos/style.css">
  
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>template/default/css/style.css" media="screen, projection" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>template/datetimepicker/jquery.datetimepicker.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>template/table/css/dataTables.tableTools.min.css">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>template/table/css/jquery.dataTables.css">

    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

    <script src="<%=request.getContextPath()%>template//datetimepicker/jquery.datetimepicker.js"></script>
    


    <script>
    $( function() {
      $( "#tabs" ).tabs({
        beforeLoad: function( event, ui ) {
          ui.jqXHR.fail(function() {
            ui.panel.html(
              "Ошибка!  " +
              "Нет данных." );
          });
        }
      });
    } );
    </script>
    

