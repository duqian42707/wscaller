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
<table>
    <tr>
        <td>WSDL Location</td>
        <td><input id="wsdl" name="wsdl"></td>
        <td><input type="button" onclick="getOperations()" value="find"></td>
    </tr>
    <tr>
        <td>Operation</td>
        <td><select id="operation">
            <option value="">请选择</option>
        </select>
        </td>
    </tr>
</table>
<table id="paramTable">
</table>
<input value="invoke" type="button" onclick="invoke()"/>
<textarea id="result">

</textarea>
</body>
<script>
    $(function () {
        $('#operation').change(function () {
            if ($(this).val() != '') {
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
                    $('#operation').append('<option value="' + data[i] + '">' + data[i] + '</option>')
                }
            }
        });
    }
    function getParams() {
        $.ajax({
            url: '<%=request.getContextPath()%>/wscaller/getParams',
            data: {wsdl: $('#wsdl').val(), operation: $('#operation').val()},
            dataType: 'json',
            success: function (data) {
                $('#paramTable').empty();
                for (var i = 0; i < data.length; i++) {
                    var tr = '<tr>' +
                            '<td>Parameter'+(i+1)+':<input readonly value="'+data[i]['FIELD_NAME']+'"/></td>' +
                            '<td>Type:<input readonly value="'+data[i]['FIELD_TYPE']+'"/></td>' +
                            '<td>value:<input class="valinput"/></td>' +
                            '</tr>';
                    $('#paramTable').append(tr)
                }
            }
        });
    }
    function invoke(){
        var paramData=[]
        $('.valinput').each(function(i,element){
            paramData.push($(this).val())
        })
        console.log(paramData)
        /*
        $.ajax({
            url: '<%=request.getContextPath()%>/wscaller/invoke',
            data: {wsdl: $('#wsdl').val(), operation: $('#operation').val()},
            dataType: 'json',
            success: function (data) {
                $('#paramTable').empty();
                for (var i = 0; i < data.length; i++) {
                    var tr = '<tr>' +
                            '<td>Parameter'+(i+1)+':<input readonly value="'+data[i]['FIELD_NAME']+'"/></td>' +
                            '<td>Type:<input readonly value="'+data[i]['FIELD_TYPE']+'"/></td>' +
                            '<td>value:<input id="value'+(i+1)+'"/></td>' +
                            '</tr>';
                    $('#paramTable').append(tr)
                }
            }
        });*/
    }
</script>
</html>
