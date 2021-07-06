package com.clay.gulimall.thirdparty.oss;

import com.alibaba.alicloud.context.AliCloudProperties;
import com.alibaba.alicloud.context.oss.OssProperties;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.clay.gulimall.common.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/thirdparty/oss")
public class OssController {

    @Autowired
    private OSS oss;

    @Autowired
    private OssProperties ossProperties;

    @Value("${spring.cloud.alicloud.oss.bucket}")
    private String bucket;

    @Autowired
    private AliCloudProperties cloudProperties;


    @RequestMapping("/policy")
    public R policy() {
        String accessId = cloudProperties.getAccessKey(); // 请填写您的AccessKeyId。
        String accessKey = cloudProperties.getSecretKey(); // 请填写您的AccessKeySecret。
        String endpoint = ossProperties.getEndpoint(); // 请填写您的 endpoint。
        String host = "https://" + bucket + "." + endpoint; // host的格式为 bucketname.endpoint
        // callbackUrl为 上传回调服务器的URL，请将下面的IP和Port配置为您自己的真实信息。
        String dir = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessId, accessKey);
        try {
            long expireTime = 30;
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8.name());
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            Map<String, Object> respMap = new LinkedHashMap<String, Object>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            return R.ok().put("data",respMap);

        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        } finally {
            ossClient.shutdown();
        }
        return R.error();


    }
}
