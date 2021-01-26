package com.atguigu.crowd.test;

import cn.hutool.core.util.RandomUtil;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.http.HttpResponse;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class UtilTest {

    @Test
    public void sendShortMessage() {
        //接收短信的手机号
        String phoneNumber = "18652959682";
        //随机4位数验证码，我这里使用的hutool随机数工具类进行生成
        String code = RandomUtil.randomNumbers(4);
        String SignName = "";
        String TemplateCode = "";
        //发送测试
        try {
            ResultEntity<String> response = CrowdUtil.sendShortMessage(SignName, TemplateCode,phoneNumber);
            System.out.println(response.getData());
        } catch (Exception e) {

            System.out.println("出错了");
        }
    }

    @Test
    public void testOSS() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream("2021.png");
        ResultEntity<String> resultEntity = CrowdUtil.uploadFileToOss("http://oss-cn-hangzhou.aliyuncs.com", "LTAI4GKG7FA4YiMvyP1PwxQm", "PAf8u1TmhXYm7edDkvwUTIq4RcQaux", inputStream, "atguigu20210126", "http://atguigu20210126.oss-cn-hangzhou.aliyuncs.com", "2021.png");

        System.out.println(resultEntity);
    }
}
