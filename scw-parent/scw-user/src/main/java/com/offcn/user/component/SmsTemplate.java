package com.offcn.user.component;

import com.offcn.common.util.HttpUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsTemplate {
    @Value("${sms.path}")
    private String path ;
    @Value("${sms.method}")
    private String method ;
    @Value("${sms.host}")
    private String host ;
    @Value("${sms.appcode}")
    private String appcode ;

    public  String sendCode(Map<String,String> params){
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","APPCODE " + appcode);
        HttpResponse response = null;
        try {
            response = HttpUtils.doPost(host, path, method, headers, params, "");
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }
}
