<%-- 
    Document   : footer
    Created on : Mar 16, 2017, 3:02:28 AM
    Author     : Павел
--%>


<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*" %>
<% 
    String pageName = request.getParameter("pageName");
    Date date = new Date();
    String strDate = date.toString();

%>

  	<script type="text/javascript" charset="utf8" src="<%=request.getContextPath()%>/template/table/js/jquery-1.11.1.min.js"></script>
  	<script type="text/javascript" charset="utf8" src="<%=request.getContextPath()%>/template/table/js/jquery.dataTables.min.js"></script>
  	<script type="text/javascript" charset="utf8" src="<%=request.getContextPath()%>/template/table/js/dataTables.tableTools.min.js"></script>
        
        
    <!--<script src="https://code.jquery.com/jquery-1.12.4.js"></script>-->
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

    <script src="<%=request.getContextPath()%>/template/datetimepicker/jquery.datetimepicker.js"></script>
    


    <script>
        
$(document).ready(function() {
    var filename = "";
    var oTable = ""
});

$("#iot-panel").on("load",function() {
        filename = 'IOPT';
        oTable = $('#iot-panel').DataTable({
        "iDisplayLength": 50,
        "order": [[ 0, "desc" ]],
        dom: 'T<"clear">lfrtip',
        "tableTools": {
          "sSwfPath": "<%=request.getContextPath()%>/template/table/swf/copy_csv_xls_pdf.swf",
      "aButtons": [ {"sExtends": "xls","sFileName": filename+".csv","bFooter": false}, {"sExtends": "pdf","sFileName": filename+".pdf","bFooter": false}, "print" ]
        }
    });
} ); 
        
        
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


	
<!--        <script>
	  	$(document).ready(function() {
	  		var filename = 'IOPT_<%=strDate==null?"":strDate%>';
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
	</script> -->
        
        
        

