package com.example.dcloud.utils;

import com.example.dcloud.pojo.RespBean;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;;import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class SmsUtils {

    @Value("${sms.secretId}")
    private String SECRET_ID;
    @Value("${sms.secretKey}")
    private String SECRET_KEY;
    @Value("${sms.endPoint}")
    private String END_POINT;
    @Value("${sms.templateId}")
    private  String TEMPLATE_ID;
    @Value("${sms.sign}")
    private  String SIGN;
    @Value("${sms.smsSdkAppid}")
    private  String SMS_SDK_APP_ID;
    @Value("${sms.timeLimit}")
    private  String TIME_LIMIT;

    public static Integer NO_PHONE = 0;
    public static Integer CODE_ERROR = 1;
    public static Integer CODE_CORRECT = 2;


    @Resource
    private RedisTemplate<String,String> redisTemplate;

    public SendSmsResponse SendSms(String phone) {
        SendSmsResponse resp = null;
        try {

            // 设置秘钥id 和秘钥key
            Credential cred = new Credential(SECRET_ID, SECRET_KEY);

            // http配置终端（平台
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(END_POINT);

            // 客户端设置http配置
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            // 初始化客户端
            SmsClient client = new SmsClient(cred, "", clientProfile);
            SendSmsRequest req = new SendSmsRequest();

            // 设置电话号码
            phone  = "+86" + phone.trim();
            String[] phoneNumberSet1 = {phone};
            req.setPhoneNumberSet(phoneNumberSet1);

            // 设置模板id及其内容
            req.setTemplateID(TEMPLATE_ID);
            String code = generatorCode();
            System.out.println("code is "+ code);
            String[] templateParam = new String[2];
            templateParam[0] = code;
            templateParam[1] = TIME_LIMIT;
            req.setTemplateParamSet(templateParam);

            // 设置签名
            req.setSign(SIGN);

            // 设置appid
            req.setSmsSdkAppid(SMS_SDK_APP_ID);

            // 存入redis 并设置过期时间
            redisTemplate.opsForValue().set(phone,code,Integer.parseInt(TIME_LIMIT),TimeUnit.MINUTES);

            // 发送短信
            resp = client.SendSms(req);
            System.out.println(SendSmsResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return resp;
    }


    private static String generatorCode() {
        String code = "";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            code += random.nextInt(10);
        }
        return code;
    }


    public Integer validateCode(String phone, String code){
        phone = "+86" + phone;
        ValueOperations<String, String> map = redisTemplate.opsForValue();
        String s = map.get(phone);
        if (s == null){
            return NO_PHONE;
        }
        if (s.equals(code)){
            // 删除
            redisTemplate.delete(phone);
            return CODE_CORRECT;
        }else{
            return CODE_ERROR;
        }
    }

}
