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
    <script type="text/javascript" src="js/jquery-3.5.1.js"></script>
    <base href="http://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<%--    <script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js">--%>
    <script type="text/javascript">
      $(function (){
        $("#btn01").click(function (){
          $.ajax({
            "url": "send/array1.do",
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
        $("#btn02").click(function (){
          // 准备好要发送到服务端的数组
          var array = [5,8,12];

          // 将JSON数组转换为JSON字符串
          var requestBody = JSON.stringify(array);
          $.ajax({
            "url": "send/array2.do",
            "type": "post",
            "data": requestBody,
            contentType: "application/json;character=UTF-8",
            "dataType": "text",
            "success": function (response){
              alert(response);
            },
            "error": function (response){
              alert(response);
            }
          });
        });

        $("#btn03").click(function (){
          // 准备要发送的数据
          var student = {
            stuId: 5,
            stuNmae: "tom",
            address: {
              province: "江苏",
              city: "南京",
              street: "秣陵街道"
            },
            subjectList: [
              {
                subName: "java",
                subScore: 100
              },
              {
                subName: "c++",
                subScore: 98
              }
            ],
            map:{
              key1: "value1",
              key2: "value2"
            }
          };
          // 将JSON对象转换为JSON字符串
          var requestBody = JSON.stringify(student);

          // 发送Ajax请求
          $.ajax({
            url: "send/compose/object.do",
            type: "post",
            data: requestBody,
            contentType: "application/json;character=UTF-8",
            dataType: "json",
            success: function (resp){
              console.log(resp)
            },
            error: function (resp) {
              console.log(resp)
            }
          })
        })

      })
    </script>
  </head>
  <body>
    <a href="test/ssm.html">测试SSM整合环境</a>
    <br/>
    <button id="btn01">Send text</button>
    <br/>
    <button id="btn02">Send json</button>
    <br/>
    <button id="btn03">Send Object</button>
  </body>
</html>
