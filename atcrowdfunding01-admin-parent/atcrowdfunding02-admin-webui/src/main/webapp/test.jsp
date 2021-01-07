<%--
  Created by IntelliJ IDEA.
  User: hjp
  Date: 2021/1/6
  Time: 20:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="text/javascript" src="jquery/jquery-3.5.1.js"></script>
    <title>测试页面</title>
    <script type="text/javascript">
        $(function (){
            $("#asyncBtn").click(function (){
                console.log("Before ajax request");
                $.ajax({
                    url: "test/ajax.do",
                    type: "post",
                    dataType: "text",
                    async: false,
                    success: function (resp){
                        console.log("ajax请求函数"+resp);
                    }
                });
                console.log("After ajax request");
            });
        });
    </script>

</head>
<body>
    <br>
    <br>
    <br>
    <button id="asyncBtn">Send ajax request</button>
</body>
</html>
