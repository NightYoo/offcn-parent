package com.offcn.order.serivce;

import com.offcn.order.pojo.TOrder;
import com.offcn.order.vo.req.OrderInfoSubmitVo;

public interface OrderService {

    TOrder saveOrder(OrderInfoSubmitVo vo);
}
