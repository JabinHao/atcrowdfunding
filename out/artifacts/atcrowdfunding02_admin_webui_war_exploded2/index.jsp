<%--
  Created by IntelliJ IDEA.
  User: hjp
  Date: 2020/12/31
  Time: 18:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>测试页面</title>
    <script type="text/javascript" src="jquery-3.4.1.js"></script>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<%--    <script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js">--%>
    <script type="text/javascript">
      $(function (){
        $("#btn01").click(function (){
          $.ajax({
            "url": "send/array.html",
            "type": "post",
            "data": {
              "array":[5,8,12]
            },
            "dataType": "text",
            "success": function (response){
              alert(response);
            },
            "error": function (response){
              alert(response);
            }
          });
        });
      })
    </script>
  </head>
  <body>
    <a href="test/ssm.html">测试SSM整合环境</a>
    <br>
    <button id="btn01">Send [5,8,12] One</button>
  </body>
</html>
