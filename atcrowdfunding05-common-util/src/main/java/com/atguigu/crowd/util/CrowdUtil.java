package com.atguigu.crowd.util;

import cn.hutool.core.util.RandomUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.*;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.atguigu.crowd.constant.CrowdConstant;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 尚筹网通用工具类
 * @author hjp
 */
public class CrowdUtil {

    private static final String ACCESSKEYID = "LTAI4GHgiE9tMgR6bMkSxfn9";
    private static final String ACCESSKEYSECRET = "y3HYWPmLH7D9w4AJsIL5jch8XIR1j9";

    /**
     * 模板号
     */
    public static final String VALIDATE_CODE = "SMS_210062882";

    /**
     * 阿里官方新API
     * @param SignName 签名
     * @param TemplateCode 模板编号
     * @param phoneNumbers 手机号码
     * @return 成功则返回验证码，失败返回错误信息
     */
    public static ResultEntity<String> sendShortMessage(String SignName, String TemplateCode, String phoneNumbers) {
        DefaultProfile profile = DefaultProfile.getProfile( "cn-hangzhou", ACCESSKEYID, ACCESSKEYSECRET );
        IAcsClient client = new DefaultAcsClient( profile );
        CommonRequest request = new CommonRequest();
        String authCode = RandomUtil.randomNumbers( 4 );
        request.setSysMethod( MethodType.POST );
        request.setSysDomain( "dysmsapi.aliyuncs.com" );
        request.setSysVersion( "2017-05-25" );
        request.setSysAction( "SendSms" );
        request.putQueryParameter( "RegionId", "cn-hangzhou" );
        request.putQueryParameter( "PhoneNumbers", phoneNumbers );
        request.putQueryParameter( "SignName", SignName );//短信签名名称。请在控制台签名管理页面签名名称一列查看。
        request.putQueryParameter( "TemplateCode", TemplateCode );
        request.putQueryParameter( "TemplateParam", "{\"code\":\"" + authCode + "\"}" ); //拼接成官网指定格式
        try {
            CommonResponse response = client.getCommonResponse( request );
//            System.out.println( response.getData() ); //回显消息
            if(response.getHttpResponse().isSuccess()) {
                //请求成功
                return ResultEntity.successWithData(authCode);
            }else{
                return ResultEntity.failed(response.getData());
            }
//            return ResultEntity.successWithData(response);
        } catch (ServerException e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        } catch (ClientException e) {
//            System.out.println("ClientException");
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 阿里旧版的官方API
     * @param signName 签名
     * @param templateCode 模板编号
     * @param phoneNumbers 手机号码
     * @return 成功则返回验证码，失败返回错误信息
     */
    public static ResultEntity<String> sendSms(String signName, String templateCode, String phoneNumbers) {
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
        //替换成你的AK
        final String accessKeyId = CrowdUtil.ACCESSKEYID;//你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = CrowdUtil.ACCESSKEYSECRET;//你的accessKeySecret，参考本文档步骤2
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 随机验证码
        String authCode = RandomUtil.randomNumbers( 4 );

        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
        request.setPhoneNumbers(phoneNumbers);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        //参考：request.setTemplateParam("{\"变量1\":\"值1\",\"变量2\":\"值2\",\"变量3\":\"值3\"}")
        request.setTemplateParam("{\"code\":\"" + authCode + "\"}");
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//        request.setOutId("yourOutId");

        try {
            //请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                //请求成功
                return ResultEntity.successWithData(authCode);
            }else{
                return ResultEntity.failed(sendSmsResponse.getMessage());
            }
        } catch (ClientException e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    /**
     * 短信服务不可用时的模拟方法
     * @param signName 签名，随便填
     * @param templateCode 模板编号，随便填
     * @param phoneNumbers 手机号码
     * @return 返回生成的随机验证码
     */
    public static ResultEntity<String> sendSmsSimulation(String signName, String templateCode, String phoneNumbers){

        String authCode = RandomUtil.randomNumbers( 4 );
        return ResultEntity.successWithData(authCode);
    }

    /**
     * 对明文字符串进行MD5加密
     * @param source 明文字符串
     * @return 加密结果
     */
    public static String md5(String source) {
        // 1.判断source是否有效
        if (source == null || source.length() == 0) {
            // 2.如果不是有效数据则抛异常
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }

        try {
            // 3.获取MessageDigest对象
            String algorithm = "md5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            // 4.获取明文字符串对应的字节数组
            byte[] input = source.getBytes();
            // 5.执行加密
            byte[] output = messageDigest.digest(input);
            // 6.创建BigInteger对象
            int signum = 1;
            BigInteger bigInteger = new BigInteger(signum, output);
            // 7.按照16进制将bigInteger的值转换为字符串
            int radix = 16;
            String encoded = bigInteger.toString(radix).toUpperCase();

            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断当前请求是否为Ajax请求
     * @param request 请求对象
     * @return
     *      true：当前请求为Ajax请求
     *      false：当前请求不是Ajax请求
     */
    public static boolean judgeRequestType(HttpServletRequest request) {

        // 1. 获取请求头
        String acceptHeader = request.getHeader("Accept");
        String xRequestHeader = request.getHeader("X-Request-With");

        // 2. 判断
        return (acceptHeader != null && acceptHeader.contains("application/json"))
                ||
                (xRequestHeader != null && xRequestHeader.equals("XMLHttpRequest"));
    }


    /**
     * 专门负责上传文件到OSS服务器的工具方法
     * @param endpoint			OSS参数
     * @param accessKeyId		OSS参数
     * @param accessKeySecret	OSS参数
     * @param inputStream		要上传的文件的输入流
     * @param bucketName		OSS参数
     * @param bucketDomain		OSS参数
     * @param originalName		要上传的文件的原始文件名
     * @return	包含上传结果以及上传的文件在OSS上的访问路径
     */
    public static ResultEntity<String> uploadFileToOss(
            String endpoint,
            String accessKeyId,
            String accessKeySecret,
            InputStream inputStream,
            String bucketName,
            String bucketDomain,
            String originalName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 生成上传文件的目录
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());

        // 生成上传文件在OSS服务器上保存时的文件名
        // 原始文件名：beautfulgirl.jpg
        // 生成文件名：wer234234efwer235346457dfswet346235.jpg
        // 使用UUID生成文件主体名称
        String fileMainName = UUID.randomUUID().toString().replace("-", "");

        // 从原始文件名中获取文件扩展名
        String extensionName = originalName.substring(originalName.lastIndexOf("."));

        // 使用目录、文件主体名称、文件扩展名称拼接得到对象名称
        String objectName = folderName + "/" + fileMainName + extensionName;

        try {
            // 调用OSS客户端对象的方法上传文件并获取响应结果数据
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName, inputStream);

            // 从响应结果中获取具体响应消息
            ResponseMessage responseMessage = putObjectResult.getResponse();

            // 根据响应状态码判断请求是否成功
            if(responseMessage == null) {

                // 拼接访问刚刚上传的文件的路径
                String ossFileAccessPath = bucketDomain + "/" + objectName;

                // 当前方法返回成功
                return ResultEntity.successWithData(ossFileAccessPath);
            } else {
                // 获取响应状态码
                int statusCode = responseMessage.getStatusCode();

                // 如果请求没有成功，获取错误消息
                String errorMessage = responseMessage.getErrorResponseAsString();

                // 当前方法返回失败
                return ResultEntity.failed("当前响应状态码="+statusCode+" 错误消息="+errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();

            // 当前方法返回失败
            return ResultEntity.failed(e.getMessage());
        } finally {

            if(ossClient != null) {

                // 关闭OSSClient。
                ossClient.shutdown();
            }
        }
    }

}
