<%-- 
    Document   : main
    Created on : Mar 14, 2017, 11:48:08 PM
    Author     : Павел
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="java.util.*, java.text.*" %>

<%! 
    String pageName = "main";
%>
<html lang="ru">
    <head>
        <jsp:include page="template/default/chunks/head.jsp" >
            <jsp:param name="pageName" value="<%=pageName%>" />
        </jsp:include>
    </head>
    <body>

    <div id="tabs">
      <ul>
        <li><a href="#tabs-1">Preloaded</a></li>
        <li><a href="ajax/content1.html">Tab 1</a></li>
        <li><a href="ajax/content2.html">Tab 2</a></li>
        <li><a href="ajax/content3-slow.php">Tab 3 (slow)</a></li>
        <li><a href="ajax/content4-broken.php">Tab 4 (broken)</a></li>
      </ul>
      <div id="tabs-1">
        <p>Proin elit arcu, rutrum commodo, vehicula tempus, commodo a, risus. Curabitur nec arcu. Donec sollicitudin mi sit amet mauris. Nam elementum quam ullamcorper ante. Etiam aliquet massa et lorem. Mauris dapibus lacus auctor risus. Aenean tempor ullamcorper leo. Vivamus sed magna quis ligula eleifend adipiscing. Duis orci. Aliquam sodales tortor vitae ipsum. Aliquam nulla. Duis aliquam molestie erat. Ut et mauris vel pede varius sollicitudin. Sed ut dolor nec orci tincidunt interdum. Phasellus ipsum. Nunc tristique tempus lectus.</p>
      </div>
    </div>

        <script>
        $('#datetimepicker1').datetimepicker({
                  lang:'ru'     ,
                  format:'H:i d.m.Y'
                });
        </script>  


    </body>
</html>