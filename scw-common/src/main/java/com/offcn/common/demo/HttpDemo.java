package com.offcn.common.demo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpDemo {

    public static void main(String[] args) throws IOException {

        // 创建一个HttpClient的客户端对象
        DefaultHttpClient httpClient = new DefaultHttpClient();

        // 创建一个请求对象
        HttpGet httpGet = new HttpGet("http://www.baidu.com");

        // 通过客户端发送请求
        HttpResponse response = httpClient.execute(httpGet);

        // 获取响应数据
        HttpEntity entity = response.getEntity();

        // 将响应数据的内容转为字符串格式
        String str = EntityUtils.toString(entity,"utf-8");

        System.out.println(str);

    }
}
