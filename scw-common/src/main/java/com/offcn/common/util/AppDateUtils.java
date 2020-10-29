package com.offcn.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppDateUtils {

    // 1.将当前时间默认格式化 yyyy-MM-dd HH:mm:ss
    public static String getFormatTime(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }

    // 2.将当前时间转换为用户自定义格式
    public static String getFormatTime(String pattern){
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date());
    }

    // 3.将任意时间转换为用户自定义格式
    public static String getFormatTime(Date date,String pattern){
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }


}
