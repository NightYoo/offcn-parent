package com.offcn.user;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserApp.class})
public class UserAppTests {

    @Autowired
    private StringRedisTemplate template;

    @Test
    public void contextLoads() {
        template.opsForValue().set("msg","中文redis测试");
    }
}
