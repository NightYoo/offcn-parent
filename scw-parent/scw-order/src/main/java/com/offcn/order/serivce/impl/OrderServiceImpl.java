package com.offcn.order.serivce.impl;

import com.offcn.common.enume.OrderStatusEnume;
import com.offcn.common.response.AppResponse;
import com.offcn.common.util.AppDateUtils;
import com.offcn.order.pojo.TOrder;
import com.offcn.order.serivce.OrderService;
import com.offcn.order.serivce.ProjectServiceFeign;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private ProjectServiceFeign projectServiceFeign;

    @Override
    public TOrder saveOrder(OrderInfoSubmitVo vo) {
        // 创建返回对象
        TOrder tOrder = new TOrder();
        BeanUtils.copyProperties(vo,tOrder);
        // vo没有的数据
        // 获取用户ID  登录的时候在redis中存储，键是=> 令牌
        String accessToken = vo.getAccessToken();
        String memberId = redisTemplate.opsForValue().get(accessToken);
        tOrder.setMemberid(Integer.parseInt(memberId));
        // 订单编号
        String ordernum = UUID.randomUUID().toString().replace("-", "");
        tOrder.setOrdernum(ordernum);
        // 状态
        tOrder.setStatus(OrderStatusEnume.UNPAY.getCode() + "");
        // 创建时间
        tOrder.setCreatedate(AppDateUtils.getFormatTime());
        // money = rtncount * 支持金额 + 运费  ==>TReturn

        AppResponse<List<TReturn>> response = projectServiceFeign.getReturnList(vo.getProjectid());
        List<TReturn> data = response.getData();
        for(TReturn tReturn : data){
           if(tReturn.getId().equals(vo.getReturnid())){
               // 支持金额 tReturn.getSupportmoney();
               // 运费 tReturn.getFreight();
               Integer money = vo.getRtncount() * tReturn.getSupportmoney() + tReturn.getFreight();
               tOrder.setMoney(money);
           }
        }

       // tOrder.setMoney(0);

        return tOrder;
    }
}
