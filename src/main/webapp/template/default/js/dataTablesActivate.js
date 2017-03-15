/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


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
