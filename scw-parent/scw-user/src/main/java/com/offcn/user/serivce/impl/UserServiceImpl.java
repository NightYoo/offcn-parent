package com.offcn.user.serivce.impl;

import com.offcn.user.enums.UserExceptionEnum;
import com.offcn.user.exception.UserException;
import com.offcn.user.mapper.TMemberAddressMapper;
import com.offcn.user.mapper.TMemberMapper;
import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;
import com.offcn.user.pojo.TMemberAddressExample;
import com.offcn.user.pojo.TMemberExample;
import com.offcn.user.serivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private TMemberMapper memberMapper;

    @Resource
    private TMemberAddressMapper addressMapper;

    @Override
    public void registerUser(TMember member) {
        // 获取是否有账号重复的操作
        TMemberExample example = new TMemberExample();
        TMemberExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(member.getLoginacct());
        long l = memberMapper.countByExample(example);
        if(l > 0){
            throw new UserException(UserExceptionEnum.LOGINACCT_EXIST);
        }
        // 获取邮箱是否重复 （判断时注意大小写区分）

        // 完成注册功能
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 密码加密
        member.setUserpswd(encoder.encode(member.getUserpswd()));
        // 认证
        member.setAuthstatus("0"); // 未认证
        member.setUsertype("0");  // 个人用户
        member.setAccttype("2"); // 个人
        System.out.println("插入数据:" + member.getLoginacct());
        // 记录插入
        memberMapper.insert(member);

    }

    @Override
    public TMember login(String username, String password) {
        TMemberExample example = new TMemberExample();
        TMemberExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(username); // 根据登录账号来查询
        List<TMember> list = memberMapper.selectByExample(example);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(list != null && list.size() == 1){
            TMember tMember = list.get(0);
            // 密码匹配
            // 注意：密码验证不要使用 equals 进行判断
            // 注意：password 是明文的，没有加密
            boolean matches = encoder.matches(password, tMember.getUserpswd());
            return matches?tMember:null;
        }
        return null;
    }

    @Override
    public TMember findTmemberById(Integer id) {
        return memberMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TMemberAddress> findAddressList(Integer memberId) {
        TMemberAddressExample example = new TMemberAddressExample();
        TMemberAddressExample.Criteria criteria = example.createCriteria();
        criteria.andMemberidEqualTo(memberId);
        return addressMapper.selectByExample(example);
    }
}
