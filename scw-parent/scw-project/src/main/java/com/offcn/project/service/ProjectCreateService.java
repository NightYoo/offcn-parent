package com.offcn.project.service;

import com.offcn.common.enume.ProjectStatusEnume;
import com.offcn.project.vo.req.ProjectRedisStorageVo;

public interface ProjectCreateService {
    // 第一步  初始化项目
    public String initCreateProject(Integer memeberId); // 通过用户来发起项目

    // 保存项目数据
    public void saveProjectInfo(ProjectStatusEnume status, ProjectRedisStorageVo storageVo);

}
