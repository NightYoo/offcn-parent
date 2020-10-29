package com.offcn.user.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.user.pojo.TMemberAddress;
import com.offcn.user.serivce.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = "获取用户信息")
public class UserInfoController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/findAddress")
    public AppResponse<List<TMemberAddress>> findAddress(String accessToken){
        // 根据令牌获取用户ID
        String memberId = redisTemplate.opsForValue().get(accessToken);
        if(StringUtils.isEmpty(memberId)){
            AppResponse fail = AppResponse.fail(null);
            fail.setMsg("用户没有登录");
            return fail;
        }
        // 用户登录
        List<TMemberAddress> addressList = userService.findAddressList(Integer.parseInt(memberId));
        AppResponse<List<TMemberAddress>> ok = AppResponse.ok(addressList);
        ok.setMsg("查询成功");
        return ok;
    }

}
