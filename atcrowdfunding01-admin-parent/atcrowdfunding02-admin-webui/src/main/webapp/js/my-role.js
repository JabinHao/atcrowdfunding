// 执行分页操作
function generatePage() {
    // 1.获取分页数据
    var pageInfo = getPageInfoRemote();
    // 2.填充表格
    fillTableBody(pageInfo);

}

// 远程访问服务器端程序获取pageInfo数据
function getPageInfoRemote() {

    var ajaxResult = $.ajax({
        url: "role/get/page/info.do",
        type: "post",
        data: {
            "pageNum": window.pageNum,
            "pageSize": window.pageSize,
            "keyword": window.keyword
        },
        async: false,
        dataType: "json"
    });
    // console.log(ajaxResult);
    // 判断当前响应状态码是否为200
    var statusCode = ajaxResult.status;
    // 如果当前响应状态码不是200，说明出现错误，显示提示信息，让当前函数停止执行
    if (statusCode != 200) {
        layer.msg("失败! 状态码="+statusCode+" 提示信息="+ajaxResult.statusText);
        return null;
    }
    // 如果响应码为200，说明请求处理成功，获取pageInfo
    var resultEntity = ajaxResult.responseJSON;
    // 从resultEntity属性中获取result属性
    var result = resultEntity.result;
    // 判断result是否成功
    if (result == "FAILED"){
        layer.msg(resultEntity);
        return null;
    }
    var pageInfo = resultEntity.data;
    return pageInfo;
}

// 填充表格
function fillTableBody(pageInfo) {
    // 清除原来的内容
    $("#rolePageBody").empty();
    // 没有结果时不显示导航条
    $("#Pagination").empty();
    // 判断pageInfo是否有效
    if (pageInfo == null || pageInfo.list == null || pageInfo.list.length === 0){
        $("#rolePageBody").append("<tr><td colspan='4' align='center'>抱歉！没有查询到您搜索的数据</td></tr>");
        return;
    }
    // 使用pageInfo的list属性填充tBody
    for (var i = 0; i < pageInfo.list.length; i++) {

        var role = pageInfo.list[i];
        var roleId = role.id;
        var roleName = role.name;

        var numberTd = "<td>"+(i+1)+"</td>";
        var checkboxTd = "<td><input class='itemBox' id='" + roleId + "' type='checkbox'></td>";
        var roleNameTd = "<td>"+roleName+"</td>";

        var checkBtn = "<button type=\"button\" class=\"btn btn-success btn-xs\"><i class=\" glyphicon glyphicon-check\"></i></button>";
        var pencilBtn = "<button id='" + roleId + "' type='button' class='btn btn-primary btn-xs pencilBtn'><i class='glyphicon glyphicon-pencil'></i></button>";
        var removeBtn = "<button id='" + roleId + "' type=\"button\" class=\"btn btn-danger btn-xs removeBtn\"><i class=\" glyphicon glyphicon-remove\"></i></button>";

        var buttonTd = "<td>"+checkBtn+" "+pencilBtn+" "+removeBtn+"</td>"
        var tr = "<tr>"+numberTd+checkboxTd+roleNameTd+buttonTd+"</tr>";

        $("#rolePageBody").append(tr);
    }
    // 生成分页导航条
    generateNavigator(pageInfo);
}

// 生成分页页码导航条
function generateNavigator(pageInfo) {

    // 获取总记录数
    var totalRecord = pageInfo.total;

    // 声明相关属性
    var properties = {
        "num_edge_entries": 3, // 边缘页数
        "num_display_entries": 5, // 主体页数
        "callback": paginationCallBack,
        "items_per_page":pageInfo.pageSize, // 每页显示1项
        "current_page": pageInfo.pageNum - 1, // Pagination内部使用pageIndex来管理页码，从0开始，而pageNum从1开始
        "prev_text": "上一页",
        "next_text": "下一页"
    };

    // 调用pagination()函数
    $("#Pagination").pagination(totalRecord, properties);

}

// 翻页时的回调函数
function paginationCallBack(pageIndex,jQuery) {
    // 根据pageIndex计算得到pageNum
    window.pageNum = pageIndex + 1;

    // 调用分页函数
    generatePage();

    // 由于每一个页码按钮都是超链接，所以在这个函数最后取消超链接的默认行为
    return false;
}

// 声明专门的函数，显示确认模态框
function showConfirmModal(roleArray) {

    console.log(roleArray);
    // 打开模态框
    $("#confirmModal").modal("show");

    // 清除旧数据
    $("#roleNameDiv").empty();

    // 全局变量，存放角色id
    window.roleIdArray = [];

    // 遍历roleArray数组
    for (let i = 0; i < roleArray.length; i++) {
        var role = roleArray[i];
        var roleName = role.roleName;
        $("#roleNameDiv").append(roleName+"<br/>");

        var roleId = role.roleId;

        window.roleIdArray.push(roleId);
    }
}