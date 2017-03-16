<%-- 
    Document   : table
    Created on : Mar 16, 2017, 5:12:06 AM
    Author     : Павел
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

      
    <% 
    String tableHead = "";
    String tableName = request.getParameter("tableName");
    String user = request.getParameter("user");
    String pageName = request.getParameter("pageName");
    
    String modelHead = 
    "<tr>" +
        "<th>Идентификатор</th>"+
        "<th>Имя</th>"+
        "<th>Часть пути</th>"+
     "</tr>";

    String objectHead = 
    "<tr>" +
        "<th>Имя</th>"+
        "<th>Идентификатор</th>"+
        "<th>ID Модели</th>"+  
        "<th>Родительская модель</th>"+    
        "<th>Часть пути</th>"+
     "</tr>";
    
    String propertyHead = 
    "<tr>" +
        "<th>Имя</th>"+
        "<th>Идентификатор</th>"+
        "<th>ID Модели</th>"+  
        "<th>Родительская модель</th>"+    
        "<th>Часть пути</th>"+
        "<th>Значение</th>"+
        "<th>Тип</th>"+
     "</tr>";

    String scriptHead = 
    "<tr>" +
        "<th>Имя</th>"+
        "<th>Идентификатор</th>"+
        "<th>ID Модели</th>"+  
        "<th>Родительская модель</th>"+    
        "<th>Часть пути</th>"+
        "<th>Значение</th>"+
     "</tr>";
    

    if(tableName.equals("models")) tableHead = modelHead;
    else if(tableName.equals("objects")) tableHead = objectHead;
    else if(tableName.equals("properties")) tableHead = propertyHead;
    else if(tableName.equals("scripts")) tableHead = scriptHead;
    

    %>



    <jsp:useBean id="ModelRequest" scope="request" class="bat15.iot.client.ModelRestClient" />
    <jsp:setProperty name="ModelRequest" property="tablename" value="<%=tableName%>" />
    <jsp:setProperty name="ModelRequest" property="result" value="<%=user%>" />


    
    
    <table id="iot-table" class="features-table">
        <thead>
           <% out.println(tableHead); %>
        </thead>
        <tbody>

            <jsp:getProperty name="ModelRequest" property="result" />

        </tbody>
        <tfoot>
           <% out.println(tableHead); %>
        </tfoot>
    </table>
    <script type="text/javascript" charset="utf8" src="<%=request.getContextPath()%>/template/table/js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" charset="utf8" src="<%=request.getContextPath()%>/template/table/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript" charset="utf8" src="<%=request.getContextPath()%>/template/table/js/dataTables.tableTools.min.js"></script>



    <script>
            $(document).ready(function() {
                    var filename = 'IOPT';
                    var oTable = $('#iot-table').DataTable({
                    "iDisplayLength": 50,
                    "order": [[ 0, "desc" ]],
                    "dom": 'T<"clear">lfrtip',
                    "language" : { "url" : "<%=request.getContextPath()%>/template/default/json/russian.json" },
                    "tableTools": {
                        "sSwfPath": "<%=request.getContextPath()%>/template/table/swf/copy_csv_xls_pdf.swf",
                        "aButtons": [ {"sExtends": "xls","sFileName": filename+".csv","bFooter": false}, {"sExtends": "pdf","sFileName": filename+".pdf","bFooter": false}, "print" ]
                    }
                });
            } );
    </script>