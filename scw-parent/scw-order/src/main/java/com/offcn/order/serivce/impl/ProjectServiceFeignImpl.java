package com.offcn.order.serivce.impl;

import com.offcn.common.response.AppResponse;
import com.offcn.order.serivce.ProjectServiceFeign;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectServiceFeignImpl implements ProjectServiceFeign {

    @Override
    public AppResponse<List<TReturn>> getReturnList(Integer projectId) {

        AppResponse<List<TReturn>> fail = AppResponse.fail(null);
        fail.setMsg("调用远程服务失败");
        return fail;
    }
}
