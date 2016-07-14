<%--
  Created by IntelliJ IDEA.
  User: dqwork
  Date: 2016/7/14
  Time: 16:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
    <script src="<%=request.getContextPath()%>/resources/jquery-1.12.0.min.js"></script>
</head>
<body>
<input id="wsdl" name="wsdl"><input type="button" onclick="getOperations()" value="find">
<br>
<select id="operation"><option value="">请选择</option></select>
<table>
    <tr></tr>
</table>
</body>
<script>
    $(function () {
        $('#operation').change(function(){
            if($(this).val()!=''){
                getParams()
            }
        })
    })
    function getOperations() {
        $.ajax({
            url: '<%=request.getContextPath()%>/wscaller/getOperations',
            data: {wsdl: $('#wsdl').val()},
            dataType: 'json',
            success: function (data) {
                $('#operation').empty();
                $('#operation').append('<option>请选择</option>');
                for (var i = 0; i < data.length; i++) {
                    $('#operation').append('<option value="'+data[i]+'">'+data[i]+'</option>')
                }
            }
        });
    }
    function getParams(){
        $.ajax({
            url: '<%=request.getContextPath()%>/wscaller/getParams',
            data: {wsdl: $('#wsdl').val(),operation:$('#operation').val()},
            dataType: 'json',
            success: function (data) {
                console.log(data)
            }
        });
    }
</script>
</html>
