<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<%@include file="include-head.jsp" %>
<script type="text/javascript">
    $(function () {
        // 1.左侧选择移到右侧列表
        $("#toRightBtn").click(function (){
            /*
            select 是标签选择器
            :eq(0)表示选择页面上的第一个
            :eq(1)表示选择页面上的第二个
            “>” 表示选择子元素
            :selected 表示选择“被选中的” option
            appendTo()能够将 jQuery 对象追加到指定的位置
            */
            $("select:eq(0)>option:selected").appendTo("select:eq(1)");
        });
        // 2.右侧选择移到左侧列表
        $("#toLeftBtn").click(function () {
            $("select:eq(1)>option:selected").appendTo("select:eq(0)");
        });

        $("#submitBtn").click(function () {
            $("select:eq(1)>option").prop("selected","selected");

            // 为了看到上面代码的效果， 暂时不让表单提交
            // return false;
        });
    })
</script>


<body>
<%@include file="include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">分配角色</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-body">
                    <form action="assign/do/role/assign.do" method="post" role="form" class="form-inline">
                        <input type="hidden" name="adminId" value="${param.adminId}">
                        <input type="hidden" name="pageNum" value="${param.pageNum}">
                        <input type="hidden" name="keyword" value="${param.keyword}">
                        <div class="form-group">
                            <label>未分配角色列表</label><br>
                            <select class="form-control" multiple="" size="10" style="width:100px;overflow-y:auto;">
                                <c:forEach items="${requestScope.unAssignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name }</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li id="toRightBtn" class="btn btn-default glyphicon glyphicon-chevron-right"></li>
                                <br>
                                <li id="toLeftBtn" class="btn btn-default glyphicon glyphicon-chevron-left" style="margin-top:20px;"></li>
                            </ul>
                        </div>
                        <div class="form-group" style="margin-left:40px;">
                            <label>已分配角色列表</label><br>
                            <select name="roleIdList" class="form-control" multiple="multiple" size="10" style="width:100px;overflow-y:auto;">
                                <c:forEach items="${requestScope.assignedRoleList}" var="role">
                                    <option value="${role.id}">${role.name }</option>
                                </c:forEach>
                            </select>
                        </div>
                        <br/><br><br>
                        <button id="submitBtn" type="submit" style="width: 150px;" class="btn btn-lg btn-success btn-block">提交</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>