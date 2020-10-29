package com.offcn.user.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.user.component.SmsTemplate;
import com.offcn.user.pojo.TMember;
import com.offcn.user.serivce.UserService;
import com.offcn.user.vo.req.UserRegistVo;
import com.offcn.user.vo.resp.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Api(tags = "用户登录，注册模块")
@Slf4j
@RestController
public class UserLoginController {

    @Autowired
    private SmsTemplate smsTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation("发送验证码的操作")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "phoneNum",value="接收验证码的手机号",required = true)
    })
    @PostMapping("/sendCode")
    public AppResponse<Object> sendCode(String phoneNum){
        // 1.  生成验证码
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 4);
        // 2.  存储在redis中，5分钟
        redisTemplate.opsForValue().set(phoneNum,code,60 * 5, TimeUnit.SECONDS);
        // 3. 发送短信
        Map<String,String> map = new HashMap<>();
        map.put("mobile",phoneNum);
        map.put("param","code:" + code);
        map.put("tpl_id","TP1711063");
        String result = smsTemplate.sendCode(map);
        if(result.equals("") || result.equals("fali") ){
            return AppResponse.fail("短信发送失败");
        }else{
            return AppResponse.ok(result);
        }

    }


    @ApiOperation("用户注册的操作")
    @PostMapping("/regist")
    public AppResponse<Object> userRegist(UserRegistVo vo){
        // 根据用户登录账号（手机号） 获取redis中存储的验证码
        String code = redisTemplate.opsForValue().get(vo.getLoginacct());
        if(code != null && !code.equals("")){
            boolean b = code.equalsIgnoreCase(vo.getCode());
            if(b){
                TMember member = new TMember();
                try {
                    // 复制属性值 将vo中的属性值复制给member对应的属性
                    BeanUtils.copyProperties(vo,member);
                    userService.registerUser(member);
                    log.debug("注册成功");
                    return AppResponse.ok("注册成功");
                } catch (BeansException e) {
                    e.printStackTrace();
                    return AppResponse.fail("注册失败");
                }

            }else{
                return AppResponse.fail("验证码错误");
            }
        }else{
            return AppResponse.fail("验证码错误，请重新获取");
        }
    }


    @ApiOperation("用户登录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username",value="用户注册的手机号",required = true),
            @ApiImplicitParam(name = "password",value="用户密码",required = true)
    })
    @PostMapping("/login")
    public AppResponse<UserRespVo> login(String username,String password){
        // 登录
        TMember member = userService.login(username, password);
        // 登录失败
        if(member == null){
            AppResponse<UserRespVo> fail = AppResponse.fail(null);
            fail.setMsg("用户登录失败");
            return fail;
        }
        // 登录成功 ==> 生成令牌 （没有规则,不重复）
        String token = UUID.randomUUID().toString().replace("-", "");
        UserRespVo vo = new UserRespVo();
        // 复制对象的属性
        BeanUtils.copyProperties(member,vo);
        vo.setAccessToken(token);
        // 需要将令牌的数据加入到redis中
        redisTemplate.opsForValue().set(token,member.getId()+"",2,TimeUnit.HOURS);
        return AppResponse.ok(vo);
    }

    @GetMapping("/findUser/{id}")
    public AppResponse<UserRespVo> findUser(@PathVariable("id") Integer id){
        TMember tMember = userService.findTmemberById(id);
        UserRespVo vo = new UserRespVo();
        BeanUtils.copyProperties(tMember,vo);
        return AppResponse.ok(vo);
    }



}
