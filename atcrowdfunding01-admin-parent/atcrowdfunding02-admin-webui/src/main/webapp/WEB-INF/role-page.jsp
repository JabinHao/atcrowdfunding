<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN" >
<%@include file="include-head.jsp" %>
<link rel="stylesheet" href="css/pagination.css">
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<link rel="stylesheet" href="ztree/zTreeStyle.css">
<script type="text/javascript" src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript" src="js/my-role.js"></script>
<script type="text/javascript">
    $(function () {
        // 1.为分页操作准备初始化数据
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";

        // 2. 调用分页函数，实现分页效果
        generatePage();

        // 3. 查询操作
        $("#searchBtn").click(function () {
            window.keyword = $("#keywordInput").val();
            generatePage();
        });
        // 4. 点击新增打开模态框
        $("#showAddModalBtn").click(function () {
            $("#addModal").modal("show");
        });
        // 5.模态框数据保存
        $("#saveRoleBtn").click(function () {
            // 获取用户在模态框中输入的角色名，trim去前后空格
            var roleName = $.trim($("#addModal [name=roleName]").val());

            console.log("打印：roleName" + roleName);
            // 发送ajax请求
            $.ajax({
                url: "role/save.do",
                type: "post",
                data: {
                    roleName: roleName
                },
                dataType: "json",
                success: function (resp) {
                    let result = resp.result;
                    if (result === "SUCCESS") {
                        layer.msg("已保存");

                        // 重新加载分页
                        window.pageNum = 99999999;
                        generatePage();
                    }
                    if (result === "FAILED") {
                        layer.msg("操作失败！" + resp.message);
                    }
                },
                error: function (resp) {
                    layer.msg(resp.status + "" + resp.statusText);
                }
            });

            // 关闭模态框
            $("#addModal").modal("hide");

            // 清理模态框内容
            $("#addModal [name=roleName]").val("");
        });

        // 6. 更新
        /*$(".pencilBtn").click(function (){
            alert("AAAAAA")
        })*/
        $("#rolePageBody").on("click", ".pencilBtn", function () {
            // 打开模态框
            $("#editModal").modal("show");

            // 获取表格中当前行中的角色名称
            var roleName = $(this).parent().prev().text();

            // 获取当前角色id
            window.roleId = this.id;
            console.log("roleId:" + window.roleId)
            // 使用roleName的值设置模态框中的文本框
            $("#editModal [name=roleName]").val(roleName);
        });

        // 7.给更新模态框中的更新按钮绑定单机响应函数
        $("#updateRoleBtn").click(function () {
            // 从文本框中获取新的角色名称
            var roleName = $("#editModal [name=roleName]").val();

            // 发送ajax请求执行更新
            $.ajax({
                url: "role/update.do",
                type: "post",
                data: {
                    id: window.roleId,
                    name: roleName
                },
                dataType: "json",
                success: function (resp) {
                    let result = resp.result;
                    if (result === "SUCCESS") {
                        layer.msg("更新成功！");

                        // 重新加载分页
                        generatePage();
                    }
                    if (result === "FAILED") {
                        layer.msg("操作失败！" + resp.message);
                    }
                },
                error: function (resp) {
                    layer.msg(resp.status + "" + resp.statusText);
                }
            });
            // 关闭模态框
            $("#editModal").modal("hide");
        });
        // 8. 点击确认模态框中的确认删除按钮执行删除
        $("#removeRoleBtn").click(function () {

            let requestBody = JSON.stringify(window.roleIdArray);
            $.ajax({
                url: "role/remove/by/role/id/array.do",
                type: "post",
                data: requestBody,
                contentType: "application/json;charset=utf-8",
                dataType: "json",
                success: function (resp) {
                    let result = resp.result;
                    if (result === "SUCCESS") {
                        layer.msg("删除成功！");

                        // 重新加载分页
                        generatePage();
                    }
                    if (result === "FAILED") {
                        layer.msg("操作失败！" + resp.message);
                    }
                },
                error: function (resp) {
                    layer.msg(resp.status + "" + resp.statusText);
                }
            });
            // 关闭模态框
            $("#confirmModal").modal("hide");
        });

        // 9. 单条删除
        $("#rolePageBody").on("click", ".removeBtn", function () {

            // 从当前按钮出发获取角色名称
            let roleName = $(this).parent().prev().text();

            // 创建role对象存入数组
            var roleArray = [{
                roleId: this.id,
                roleName: roleName
            }];

            // 调用函数打开模态框
            showConfirmModal(roleArray);
        });

        // 10.给总的checkbox绑定单机响应函数
        $("#summaryBox").click(function () {

            // 获取当前多选框自身状态
            var currentStatus = this.checked;

            // 用当前多选框状态设置其它多选框
            $(".itemBox").prop("checked", currentStatus);
        });

        // 11.全选全不选的反向操作
        $("#rolePageBody").on("click", ".itemBox", function () {

            // 获取当前已经选中的.itemBox的数量
            var checkedBoxCount = $(".itemBox:checked").length;

            // 获取全部.itemBox的数量
            var totalBoxCount = $(".itemBox").length;

            // 使用两者的比较结果设置总的checkBox
            $("#summaryBox").prop("checked", checkedBoxCount === totalBoxCount)
        });

        // 12.给批量删除的按钮绑定单击响应函数
        $("#batchRemoveBtn").click(function () {

            // 创建数组对象用来存放后面获取到的角色对象
            var roleArray = [];

            // 遍历当前选中的多选框
            $(".itemBox:checked").each(function () {

                // 使用this引用当前遍历得到的多选框
                var roleId = this.id;
                console.log("roleId:" + roleId);

                // 通过DOM操作获取角色名称
                var roleName = $(this).parent().next().text();

                roleArray.push({
                    roleId: roleId,
                    roleName: roleName
                });
            });

            // 检查roleArray的长度是否为0
            if (roleArray.length === 0) {
                layer.msg("请至少选择一个执行删除");
                return;
            }

            // 调用专门的函数打开确认模态框
            showConfirmModal(roleArray);
        });

        // 13.给分配权限按钮绑定单击响应函数
        $("#rolePageBody").on("click", ".checkBtn", function () {

            window.roleId = this.id;
            // 打开模态框
            $("#assignModal").modal("show");

            //  在模态框中装载Auth的树形结构数据
            fillAuthTree();
        });

        // 14.给分配权限模态框中的“分配” 按钮绑定单击响应函数
        $("#assignBtn").click(function () {
            // ①收集树形结构的各个节点中被勾选的节点
            // [1]声明一个专门的数组存放 id
            var authIdArray = [];
            // [2]获取 zTreeObj 对象
            var zTreeObj = $.fn.zTree.getZTreeObj("authTreeDemo");
            // [3]获取全部被勾选的节点
            var checkedNodes = zTreeObj.getCheckedNodes();
            // [4]遍历 checkedNodes
            for (var i = 0; i < checkedNodes.length; i++) {
                var checkedNode = checkedNodes[i];
                var authId = checkedNode.id;
                authIdArray.push(authId);
            }
            // ②发送请求执行分配
            var requestBody = {
                "authIdArray": authIdArray,
                // 为了服务器端 handler 方法能够统一使用 List<Integer>方式接收数据， roleId 也存入数组
                "roleId": [window.roleId]
            };
            requestBody = JSON.stringify(requestBody);
            $.ajax({
                url: "assign/do/role/assign/auth.do",
                type: "post",
                data: requestBody,
                contentType: "application/json;charset=UTF-8",
                dataType: "json",
                success: function (response) {
                    var result = response.result;
                    if (result === "SUCCESS") {
                        layer.msg("操作成功！ ");
                    }
                    if
                    (result === "FAILED") {
                        layer.msg("操作失败！ " + response.message);
                    }
                },
                error: function (response) {
                    layer.msg(response.status + " " + response.statusText);
                }
            });
            $("#assignModal").modal("hide");
        });
    })

</script>

<body>
<%@include file="include-nav.jsp" %>
<div class="container-fluid">
    <div class="row">
        <%@include file="include-sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="keywordInput" class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button type="button" id="batchRemoveBtn" class="btn btn-danger" style="float:right;margin-left:10px;"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <button type="button" id="showAddModalBtn" class="btn btn-primary" style="float:right;" ><i class="glyphicon glyphicon-plus"></i> 新增</button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryBox" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="rolePageBody"></tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><!--这里显示分页--></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%@include file="/WEB-INF/modal-role-add.jsp"%>
<%@include file="/WEB-INF/modal-role-edit.jsp"%>
<%@include file="/WEB-INF/modal-role-confirm.jsp"%>
<%@include file="/WEB-INF/modal-role-assign-auth.jsp"%>
</body>
</html>
    