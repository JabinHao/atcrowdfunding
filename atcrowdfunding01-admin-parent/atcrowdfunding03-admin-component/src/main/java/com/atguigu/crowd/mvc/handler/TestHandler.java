package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import crowd.entity.Admin;
import crowd.entity.Student;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TestHandler {

    @Autowired
    private AdminService adminService;

    private final Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @ResponseBody
    @RequestMapping("/send/array1.do")
    public String testReceiveArrayOne(@RequestParam("array[]") List<Integer> array){

        for (Integer number : array){
            System.out.println(number);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("/send/array2.do")
    public String testReceiveArrayTwo(@RequestBody List<Integer> array){

        for (Integer number : array){
            logger.info("number="+number); //注意是 org.slf4j.Logger，不是jul中的Logger
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping("/send/compose/object.do")
    public ResultEntity<Student> testReceiveComplicatedObject(@RequestBody Student student, HttpServletRequest request){

        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        logger.info("judgeResult = "+judgeResult);

        logger.info(student.toString());

/*        // 制造空指针异常
        String a = null;
        System.out.println(a.length());*/

        return ResultEntity.successWithData(student);
    }

    @RequestMapping("/test/ssm.html")
    public String testSSM(ModelMap modelMap, HttpServletRequest request){

        boolean judgeResult = CrowdUtil.judgeRequestType(request);
         logger.info("judgeResult = "+judgeResult);

        List<Admin> adminList = adminService.getAll();
        modelMap.addAttribute("adminList",adminList);

/*        String a = null;
        System.out.println(a.length());*/
        System.out.println(10/0);

        return "target";
    }
}
