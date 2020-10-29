package com.offcn.project.service;

import com.offcn.project.pojo.*;

import java.util.List;

public interface ProjectInfoService {

    // 根据项目的ID获取回报信息
    List<TReturn> getReturnByPId(Integer projectId);

    List<TProject> findAllProject();

    List<TProjectImages> getProjectImages(Integer projectId);

    TProject findProjectInfo(Integer projectId);

    List<TTag> findAllTag();

    List<TType> findAllType();

    TReturn findReturnByRid(Integer returnId);

}
