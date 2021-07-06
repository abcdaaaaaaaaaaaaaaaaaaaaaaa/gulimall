//package com.clay.gulimall.product;
//
//import com.alibaba.fastjson.JSONObject;
//import com.aliyun.oss.OSSClient;
//import com.aliyun.oss.model.PutObjectResult;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.File;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class GulimallProductApplicationTests {
//
//    @Autowired
//    private OSSClient ossClient;
//
//
//    @Test
//    public void testUploadFile(){
//
//        PutObjectResult result = ossClient.putObject("clay-gulimall", "ff00779ac8.jpg", new File("C:\\Users\\work_\\Pictures\\ff00779ac8.jpg"));
//        System.out.println(JSONObject.toJSONString(result));
//    }
//
//}
