package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.service.api.AdminService;
import com.github.pagehelper.PageInfo;
import crowd.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "/admin/do/login.do")
    public String doLogin(
            @RequestParam("loginAcct") String loginAcct,
            @RequestParam("userPswd") String userPswd,
            HttpSession session
        ) {
        // 1.调用service方法执行登录检查
        // 返回admin对象说明登录成功，否则会抛出异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);

        // 2.将登录成功返回的admin对象存入Session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
        return "redirect:/admin/to/main.do";
    }

    @RequestMapping(value = "/admin/do/logout.do")
    public String doLogout(HttpSession session) {

        // 强制Session失效
        session.invalidate();
        return "redirect:/admin/to/login.do";
    }

    @RequestMapping(value = "/admin/get/page.do")
    public String getPageInfo(
            // 使用defaultValue指定默认值
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            ModelMap modelMap
        ) {
        // 调用service方法获取PageInfo对象
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        // 将PageInfo对象存入模型
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        return "admin-page";
    }
    // 删除成员
    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.do")
    public String remove(@PathVariable("adminId") Integer adminId,
                         @PathVariable("pageNum") Integer pageNum,
                         @PathVariable("keyword") String keyword,
                         ModelMap modelMap,
                         HttpSession session) {
        Admin admin = (Admin) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN);
        // System.out.println("删除用户"+admin.getUserName());
        // 判断是否为当前用户
        if (admin.getId()==adminId) {
            session.setAttribute("msg","删除失败，不能删除当前登录用户！");
        }
        else {
            // 执行删除
            adminService.remove(adminId);
            session.setAttribute("msg","用户"+admin.getUserName()+"已被删除");
        }
        // 页面跳转：回到分页页面
        return "redirect:/admin/get/page.do?pageNum="+pageNum+"&keyword="+keyword;
    }

    // 新增成员
    @RequestMapping("/admin/save.do")
    public String save(Admin admin) {
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.do?pageNum="+Integer.MAX_VALUE;
    }

    // 修改页面
    @RequestMapping("/admin/to/edit.do")
    public String toEdit(@RequestParam("adminId") Integer adminId,
                         ModelMap modelMap) {
        Admin admin = adminService.getAdminById(adminId);
        modelMap.addAttribute("admin",admin);
        return "admin-edit";
    }

    // 提交更新信息
    @RequestMapping(value = "/admin/update.do")
    public String update(Admin admin,
                         @RequestParam(value = "keyword",defaultValue = "") String keyword,
                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        adminService.update(admin);
        return "redirect:/admin/get/page.do?pageNum="+pageNum+"&keyword="+keyword;
    }
}
