package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleHandler {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasRole('部长')")
    @RequestMapping("/role/get/page/info.do")
    public ResultEntity<PageInfo<Role>> getPageInfo(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                    @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        // 调用service获取分页数据
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum,pageSize,keyword);
        return ResultEntity.successWithData(pageInfo);

    }

    //更新
    @RequestMapping("/role/save.do")
    public ResultEntity<String> saveRole(@RequestParam("roleName") String roleName) {

        roleService.saveRole(new Role(null, roleName));

        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/role/update.do")
    public ResultEntity<String> updateRole(Role role) {

        roleService.updateRole(role);
        return ResultEntity.successWithoutData();
    }

    //删除
    @RequestMapping("/role/remove/by/role/id/array.do")
    public ResultEntity<String> removeByRoleIdArray(@RequestBody List<Integer> roleIdList) {

        roleService.removeRole(roleIdList);

        return ResultEntity.successWithoutData();
    }
}
