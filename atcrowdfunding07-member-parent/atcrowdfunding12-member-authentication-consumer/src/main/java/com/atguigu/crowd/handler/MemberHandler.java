package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.api.RedisRemoteService;
import com.atguigu.crowd.config.ShortMessageProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.MemberVO;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@Controller
public class MemberHandler {

    @Autowired
    private ShortMessageProperties shortMessageProperties;

    @Autowired
    private RedisRemoteService redisRemoteService;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }

    @RequestMapping("/auth/member/do/login")
    public String doLogin(@RequestParam("loginacct") String logincacct,
                          @RequestParam("userpswd") String userpswd,
                          ModelMap modelMap,
                          HttpSession session) {

        // 1.调用远程接口查询登录账号
        ResultEntity<MemberPO> resultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(logincacct);

        // 2.失败则返回登录页面
        if (ResultEntity.FAILED.equals(resultEntity.getResult())) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_EXCEPTION, resultEntity.getMessage());
            return "member-login";
        }

        MemberPO memberPO = resultEntity.getData();

        if (memberPO == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_EXCEPTION, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        // 3.比较密码
        String userpswdDB = memberPO.getUserpswd();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(userpswd, userpswdDB);
        if (!matches) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_EXCEPTION, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        // 4.创建MemberLoginVO对象存入Session域
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);

        return "redirect:http://www.crowd.com/auth/member/to/center/page";
    }

    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap) {


        // 1.获取用户输入的手机号
        String phoneNum = memberVO.getPhoneNum();

        // 2.拼接Redis中存储的Key
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

        // 3.从Redis读取Key对应的value
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKey(key);

        // 4.检查查询操作是否有效
        String result = resultEntity.getResult();
        if (ResultEntity.FAILED.equals(result)) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-reg";
        }

        String redisCode = resultEntity.getData();
        if (redisCode == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-reg";
        }

        // 5.能从Redis查询到，则比较两者是否一致
        String formCode = memberVO.getCode();
        if (!formCode.equals(redisCode)) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_INVALID);
        }
        // 6.一致，将value从Redis中删除
        ResultEntity<String> removeResultEntity = redisRemoteService.removeRedisKeyRemote(key);
        /*if (ResultEntity.FAILED.equals(removeResultEntity.getResult())) {
            return "";
        }*/

        // 7.执行密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberVO.setUserpswd(passwordEncoder.encode(memberVO.getUserpswd()));

        // 8. 执行保存
        MemberPO memberPO = new MemberPO();
        BeanUtils.copyProperties(memberVO, memberPO);
        ResultEntity<String> saveMemberResultEntity = mySQLRemoteService.saveMember(memberPO);

        if (ResultEntity.FAILED.equals(saveMemberResultEntity)) {

            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveMemberResultEntity.getMessage());

            return "member-reg";
        }

        return "redirect:/auth/member/to/login/page";
    }

    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum) {

        // 1.发送验证码到 phoneNum
        ResultEntity<String> resp = CrowdUtil.sendSmsSimulation(
                shortMessageProperties.getSignName(),
                shortMessageProperties.getTemplateCode(),
                phoneNum
        );

        // 2.判断是否发送成功
        if (ResultEntity.SUCCESS.equals(resp.getResult())) {

            // 3.如果成功，则将验证码存入Redis
            String code = resp.getData();
            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code, 100, TimeUnit.SECONDS);

            // 4. 判断是否成功存入 Redis
            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())) {
                return ResultEntity.successWithoutData();
            }else {
                return saveCodeResultEntity;
            }
        }else {
            return resp;
        }
    }
}
