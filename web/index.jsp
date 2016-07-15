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
    <link href="css/index.css" rel="stylesheet" type="text/css"/>
    <script src="<%=request.getContextPath()%>/resources/jquery-1.12.0.min.js"></script>
    <style>

    </style>
</head>
<body>
<div class="body">
    <div>
        <table class="optionTable">
            <tr>
                <td>WSDL Location</td>
                <td><input id="wsdl" name="wsdl"></td>
                <td><input class="btn" type="button" onclick="getOperations()" value="find" ></td>
            </tr>
            <tr>
                <td>Operation</td>
                <td><select id="operation">
                    <option value="">请选择</option>
                </select></td>
            </tr>
        </table>
    </div>
    <div class="div_param">
        <table id="paramTable">
        </table>
    </div>
    <div>
        <input value="invoke" class="btn" type="button" onclick="invoke()"/>
    </div>
<textarea id="result">

</textarea>
</div>
</body>
<script>
    var operations = [];
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
                operations = data;
                $('#operation').empty();
                $('#operation').append('<option>请选择</option>');
                for (var i = 0; i < data.length; i++) {
                    $('#operation').append('<option value="' + data[i]['operationName'] + '">' + data[i]['operationName'] + '</option>')
                }
            }
        });
    }
    function getParams() {
        $('#paramTable').empty();
        for (var i = 0; i < operations.length; i++) {
            if ($('#operation').val() == operations[i]['operationName']) {
                var params = operations[i]['params']
                for (var j = 0; j < params.length; j++) {
                    var tr = '<tr>' +
                            '<td>Parameter' + (j + 1) + ':<input disabled value="' + params[j]['FIELD_NAME'] + '"/></td>' +
                            '<td>Type:<input disabled value="' + params[j]['FIELD_TYPE'] + '"/></td>' +
                            '<td>value:<input class="valinput"/></td>' +
                            '</tr>';
                    $('#paramTable').append(tr)
                }
            }
        }
    }
    function invoke() {
        var paramData = []
        $('.valinput').each(function (i, element) {
            paramData.push($(this).val())
        })
        var paramStr = JSON.stringify(paramData);
        $.ajax({
            url: '<%=request.getContextPath()%>/wscaller/invoke',
            data: {wsdl: $('#wsdl').val(), operation: $('#operation').val(), paramStr: paramStr},
            dataType: 'text',
            success: function (data) {
                $('#result').val(data)
            }
        });
    }
</script>
</html>
