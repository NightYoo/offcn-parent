package com.offcn.user.serivce;


import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;

import java.util.List;

public interface UserService {

    public void registerUser(TMember member);

    public TMember login(String username,String password);

    public TMember findTmemberById(Integer id);

    public List<TMemberAddress> findAddressList(Integer memberId);

}
