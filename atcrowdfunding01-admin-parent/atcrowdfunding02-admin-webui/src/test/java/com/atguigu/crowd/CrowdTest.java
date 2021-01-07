package com.atguigu.crowd;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.util.MyUtil;
import com.fasterxml.jackson.databind.deser.impl.ValueInjector;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

// 这里用junit5进行测试
@SpringJUnitConfig(locations = {"classpath:spring-persist-tx.xml", "classpath:spring-persist-mybatis.xml"})
public class CrowdTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleService roleService;

    @Test
    public void testTx(){
        Admin admin = new Admin(null, "mei Lee", CrowdUtil.md5("123456"), "mei", "mei@qq.com", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        adminService.saveAdmin(admin);
    }

    /**
     * 读取文件中的admin信息存入数据库,admin信息为伪造的管理员信息列表
     */
    @Test
    public void testCreateAdmin(){
        String fileName = "F:\\code\\spring\\MyProject\\SSM\\atcrowdfunding\\admin_list.dat";
        ArrayList<String> adminList = MyUtil.readName(fileName);
        for (String admin:adminList) {
            String[] message = admin.split(",");
            adminMapper.insert(new Admin(null,message[0],message[1],message[2],message[3],message[4]));
        }
//        Admin admin = new Admin(null, "mei Lee", CrowdUtil.md5("123456"), "mei", "mei@qq.com", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//        adminService.saveAdmin(admin);
    }

    @Test
    public void testInsertAdmin(){
        Admin admin;
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String password = "123123";
        String userPswd = CrowdUtil.md5(password);
        admin = new Admin(null,"Rachel",userPswd,"rui","rui@qq.com",date);
        int count = adminMapper.insert(admin);
        System.out.println(count);
    }
    @Test
    public void testCreateData(){
        Admin admin;
        for (int i = 0; i < 10; i++) {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String password = String.valueOf((int)(Math.random()*900000 + 100000));;
            String userPswd = CrowdUtil.md5(password);
            String login = getStringRandom(5);
            String userName = login.substring(0,3);
            admin = new Admin(null,captureName(login),userPswd,userName,userName+"@qq.com",date);
            adminMapper.insert(admin);
        }
/*        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String password = String.valueOf((int)(Math.random()*900000 + 100000));;
        String userPswd = CrowdUtil.md5(password);
        String login = getStringRandom(5);
        String userName = login.substring(0,3);
        admin = new Admin(null,captureName(login),userPswd,userName,"rui@qq.com",date);
        int count = adminMapper.insert(admin);
        System.out.println(count);*/
    }

    @Test
    public void  testConnection() throws SQLException{
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    @Test
    public void testLog(){

        // 1. 获取Logger对象，通常传入当前打印日志的类
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);

        // 2. 根据不同日志级别打印日志
        logger.debug("Hello I am DEBUG");
        logger.debug("Hello I am DEBUG");
        logger.debug("Hello I am DEBUG");

        logger.error("Hello I am ERROR");
        logger.error("Hello I am ERROR");
        logger.error("Hello I am ERROR");

        logger.info("Hello I am INFO");
        logger.info("Hello I am INFO");
        logger.info("Hello I am INFO");

        logger.warn("Hello I am WARN");
        logger.warn("Hello I am WARN");
        logger.warn("Hello I am WARN");

    }
    //生成随机用户名，数字和字母组成,
    public static String getStringRandom(int length) {
        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {

/*            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }*/
            val += (char)(random.nextInt(26)+97);
        }
        return val;
         }
    public static String captureName(String name) {
        /*
         name = name.substring(0, 1).toUpperCase() + name.substring(1);
         return  name;
        */
        char[] cs=name.toCharArray();
        cs[0]-=32;
        return String.valueOf(cs);

    }

    @Test
    public void testRandomName() {
        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < 6; i++) {

/*            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }*/
            val += (char)(random.nextInt(26)+97);
        }
        System.out.println(val);
    }

    //
    @Test
    public void createRole() throws IOException {
        String fileName = "F:\\code\\spring\\MyProject\\SSM\\atcrowdfunding\\name_list.dat";
        ArrayList<String> nameList = MyUtil.readName(fileName);
    }

    @Test
    public void testRoleSave() {
        String fileName = "F:\\code\\spring\\MyProject\\SSM\\atcrowdfunding\\name_list.dat";
        ArrayList<String> nameList = MyUtil.readName(fileName);
        for (String name:nameList) {
            roleMapper.insert(new Role(null,name));
        }
    }

 
}
