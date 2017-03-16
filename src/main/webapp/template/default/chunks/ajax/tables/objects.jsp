<%-- 
    Document   : models
    Created on : Mar 15, 2017, 9:17:26 PM
    Author     : Павел
--%>


<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*" %>


<% 
    String tableName = "objects";
    String user = "iks";
    Date date = new Date();
    String strDate = date.toString();

%>

    <jsp:useBean id="ModelRequest" scope="request" class="bat15.iot.client.ModelRestClient" />
    <jsp:setProperty name="ModelRequest" property="tablename" value="<%=tableName%>" />
    <jsp:setProperty name="ModelRequest" property="result" value="<%=user%>" />

      
                <% 
                String tableHead = 
                "<tr>" +
                    "<th>Имя</th>"+
                    "<th>Идентификатор</th>"+
                    "<th>ID Модели</th>"+  
                    "<th>Родительская модель</th>"+    
                    "<th>Часть пути</th>"+
                 "</tr>";
                %>
                
 	<table id="iot-panel">
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

<!--        <script>
	  	$(document).ready(function() {
	  		var filename = 'IOPT'<%=tableName.isEmpty()?"":"+'_"+tableName+"'"%>'_'<%=strDate==null?"":strDate%>;
	  		var oTable = $('#iot-panel').DataTable({
		    	"iDisplayLength": 50,
		    	"order": [[ 0, "desc" ]],
		    	dom: 'T<"clear">lfrtip',
	  	      	"tableTools": {
	  	      	  "sSwfPath": "<%=request.getContextPath()%>/template/table/swf/copy_csv_xls_pdf.swf",
	              "aButtons": [ {"sExtends": "xls","sFileName": filename+".csv","bFooter": false}, {"sExtends": "pdf","sFileName": filename+".pdf","bFooter": false}, "print" ]
	          	}
		    });
		} );
	</script>-->
